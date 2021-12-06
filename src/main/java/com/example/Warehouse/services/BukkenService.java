package com.example.Warehouse.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Warehouse.dtos.SearchDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.BukkenStatus;
import com.example.Warehouse.entities.bukkenService.Station;
import com.example.Warehouse.exceptions.accountService.AccountNotFoundException;
import com.example.Warehouse.exceptions.accountService.EmptyException;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.exceptions.bukkenService.BukkenNotFoundException;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.bukkenService.BukkenRepository;
import com.example.Warehouse.repositories.bukkenService.StationRepository;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfJoins;
import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
public class BukkenService {
	@Autowired
	private BukkenRepository bukkenRepo;
	@Autowired
	private AccountRepository accRepo;
	@Autowired
	private StationRepository stationRepo;

	public List<Bukken> getAll() {
		List<Bukken> result = bukkenRepo.findAll();
		if (result.size() > 0) {
			return result;
		} else {
			return result;
		}
	}

	public Page<Bukken> getAllByPage(int pageIndex) {
		Page<Bukken> result = bukkenRepo.findAll(PageRequest.of(pageIndex, 12));
		if (result.getSize() > 0) {
			return result;
		} else {
			throw new EmptyException();
		}
	}

	public Bukken createBukken(Bukken bukken) {
		try {
//			Optional<Account> thisAccount = accRepo.findById(bukken.getAccount().getId());
//			bukken.setAccount(thisAccount.get());
			bukkenRepo.save(bukken);
			return bukken;
		} catch (Exception e) {
			return null;
		}
	}

	public long countAll() {
		return bukkenRepo.count();
	}

	public Bukken getById(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new BukkenNotFoundException(bukkenId);
		}
	}

	public void deleteBukken(int bukkenId) {
		Optional<Bukken> oldBukken = bukkenRepo.findById(bukkenId);
		if (!oldBukken.isPresent()) {
			throw new BukkenNotFoundException(bukkenId);
		}
		bukkenRepo.deleteById(bukkenId);
	}

	public void deleteAll() {
		bukkenRepo.deleteAll();
	}

	public void importBukken(List<Bukken> bukkens) {
		List<Bukken> result = null;
		for (Bukken bukken : bukkens) {
			if ("CompleteLaunching".equals(bukken.getStatus())) {
				bukken.setStatus(BukkenStatus.CompleteLaunching);
			}
			if ("NotCompleteLaunching".equals(bukken.getStatus())) {
				bukken.setStatus(BukkenStatus.NotCompleteLaunching);
			}
			if ("CompleteNotLaunching".equals(bukken.getStatus())) {
				bukken.setStatus(BukkenStatus.CompleteNotLaunching);
			}
			if ("NotCompleteNotLaunching".equals(bukken.getStatus())) {
				bukken.setStatus(BukkenStatus.NotCompleteNotLaunching);
			}
		}
		result = bukkenRepo.saveAll(bukkens);
		if (result == null) {
			throw new ImportFailException();
		}
	}

	public void importStation(List<Station> stations) {
		List<Station> result = null;
		result = stationRepo.saveAll(stations);
		if (result == null) {
			throw new ImportFailException();
		}
		List<Bukken> bukkens = bukkenRepo.findAll();
		if (bukkens != null) {
			for (Bukken bukken : bukkens) {
				BigDecimal distance =calDistance(new BigDecimal(bukken.getLatitude()),new BigDecimal(bukken.getLongitude()),new BigDecimal(stations.get(0).getLat()),new BigDecimal(stations.get(0).getLng()));
				bukken.setStation(stations.get(0));
				for(int i=1;i<stations.size();i++) {
					BigDecimal temp=calDistance(new BigDecimal(bukken.getLatitude()),new BigDecimal(bukken.getLongitude()),new BigDecimal(stations.get(i).getLat()),new BigDecimal(stations.get(i).getLng()));
					if (temp.compareTo(distance)<0) {
						distance=temp;
						bukken.setStation(stations.get(i));
					}
				}
			}
			bukkenRepo.saveAll(bukkens);
		}
	}
	public void triggerOwnerBukken() {
		List<Account> accountList = findByRoleId(2);
		List<Bukken> bukkenList = getAll();
		Random rand = new Random();
		for (Bukken bukken : bukkenList) {
			Set<Account> temp = new HashSet<>();
			temp.add(accountList.get(rand.nextInt(accountList.size())));
			bukken.setAccounts(temp);
		}
		bukkenRepo.saveAll(bukkenList);
	}
	
	
	public List<Account> findByRoleId(int roleId) {
		List<Account> result = accRepo.findByRoleId(roleId);
		if (result.size() == 0) {
			throw new EmptyException();
		}
		return result;
	}

	public void save(List<Bukken> bukkenList) {
		bukkenRepo.saveAll(bukkenList);
	}

	public java.math.BigDecimal calDistance(java.math.BigDecimal CenterLat, java.math.BigDecimal CenterLng,
			java.math.BigDecimal ThisLocationLat, java.math.BigDecimal ThisLocationLng) {
		double startLat = CenterLat.doubleValue() * Math.PI / 180;
		;
		double endLat = ThisLocationLat.doubleValue() * Math.PI / 180;
		double dLat = endLat - startLat;
		double dLong = (ThisLocationLng.doubleValue() - CenterLng.doubleValue()) * Math.PI / 180;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(startLat) * Math.cos(endLat) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double result = 6371 * c;
		BigDecimal b = new BigDecimal(result, MathContext.DECIMAL64);
		return b; // <-- d
	}

	public List<Bukken> filterByAddress(java.math.BigDecimal CenterLat, java.math.BigDecimal CenterLng,
			java.math.BigDecimal radius) {
		List<Bukken> result = new ArrayList<Bukken>();
		List<Bukken> bukkens = getAll();
		for (Bukken bukken : bukkens) {
			if (calDistance(CenterLat, CenterLng, new BigDecimal(bukken.getLatitude()),
					new BigDecimal(bukken.getLongitude())).compareTo(radius) <= 0) {
				result.add(bukken);
			} else {
				continue;
			}
		}
		return result;
	}

	public List<Bukken> getBukkensFromCircleDraws(String circleDraws) {
		try {
			JSONArray jArray = new JSONArray(circleDraws);
			List<Bukken> result = new ArrayList<Bukken>();
			for (int i = 0; i < jArray.length(); i++) {
				List<Bukken> tempList = filterByAddress(
						new BigDecimal(((Number) jArray.getJSONObject(i).get("lat")).doubleValue()),
						new BigDecimal(((Number) jArray.getJSONObject(i).get("lng")).doubleValue()),
						new BigDecimal(((Number) jArray.getJSONObject(i).get("radius")).doubleValue() / 1000));
				result.addAll(tempList);
			}
			Set uniqueResult = new HashSet<>(result);
			List<Bukken> finalResult = new ArrayList<Bukken>(uniqueResult);
			return finalResult;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Bukken> getBukkensFromPolygonDraws(String polygonJson, String pointsJson) {
		try {
			JSONArray jArrayPolygon = new JSONArray(polygonJson);
			JSONArray jArrayPoints = new JSONArray(pointsJson);
			List<Bukken> bukkenList = getAll();
			List<Bukken> result = new ArrayList<Bukken>();
			for (int i = 0; i < jArrayPolygon.length(); i++) {
				List<Point> peakList = new ArrayList<Point>();
				for (int j = 0; j < jArrayPoints.length(); j++) {
					if (((Number) jArrayPolygon.getJSONObject(i).get("identity"))
							.doubleValue() == ((Number) jArrayPoints.getJSONObject(j).get("identity")).doubleValue()) {
						peakList.add(com.mapbox.geojson.Point.fromLngLat(
								((Number) jArrayPoints.getJSONObject(j).get("lng")).doubleValue(),
								((Number) jArrayPoints.getJSONObject(j).get("lat")).doubleValue()));
					}
				}
				for (Bukken bukken : bukkenList) {
					if (checkPointInsidePolygon(
							com.mapbox.geojson.Point.fromLngLat(bukken.getLongitude(), bukken.getLatitude()),
							peakList)) {
						result.add(bukken);
					}
				}
			}
			Set uniqueResult = new HashSet<>(result);
			List<Bukken> finalResult = new ArrayList<Bukken>(uniqueResult);
			return finalResult;
		} catch (Exception e) {
			throw e;
		}

	}

	public List<Bukken> getBukkensBySearchShape(SearchDto searchDto) {
		List<Bukken> circleResult = getBukkensFromCircleDraws(searchDto.getCircleDraws());
		List<Bukken> polygonResult = getBukkensFromPolygonDraws(searchDto.getPolygonDraws(), searchDto.getPoints());
		circleResult.addAll(polygonResult);
		Set uniqueResult = new HashSet<>(circleResult);
		List<Bukken> finalResult = new ArrayList<Bukken>(uniqueResult);
		return finalResult;
	}

	public boolean checkPointInsidePolygon(Point point, List<Point> peakList) {
		List<List<Point>> listOfPointList = new ArrayList<List<Point>>();
		listOfPointList.add(peakList);
		Polygon polygon = com.mapbox.geojson.Polygon.fromLngLats(listOfPointList);
		return TurfJoins.inside(point, polygon);
	}
	// ***************************************CRAWL SALARY
	// DATA****************************************

	private static String dayPosted = "";
	private static final String[] LIST_JOB_TYPES = { "フルタイム", "パートタイム", "契約社員", "インターン" };
	private final static String[] LIST_EXCLUSION_FLAG = { "フォーク", "ﾌｫｰｸ" };
	private final static String AN_HOUR = "1 時間 ";
	private static final String[] HEADER_CSV = {
			// Title of the columns (column_names)
			"募集データID", "物件ID", "データ取得日", "距離条件", "除外フラグ", "募集主体", "提供元", "求人タイトル", "時給（from）", "時給（to）	雇用形態" };
	private static int countItem = 0;
	private static ArrayList<String> dataCsv;
	private static ArrayList<String> listHashRecruitmentID = new ArrayList<String>();

	public void crawlAllStation() {
		List<Station> stations = stationRepo.findAll();
		if (stations != null) {
			for (Station station : stations) {
				callApiCrawl(station.getName(), "10.0", station.getId(), station);
			}
			stationRepo.saveAll(stations);
		} else {
			return;
		}
	}

	public void callApiCrawl(String keySearch, String location, int bukken_id, Station station) {
		dataCsv = new ArrayList<String>();
		int sumInWhile = 0;
		int start = 0;
		int count = 10;
		int sum = 0;
		int dataSize = 0;
		Date date = new Date();
		double S10 = 0;
		double S2 = 0;
		while (sumInWhile < 150 && count > 0) {
			try {
				count = 10;
				String lrad = "&lrad=10.0"; // req.location != "2" ? "&lrad=10.0" :
																					// "";
				String chips = dayPosted != ""
						? ("&chips=date_posted:" + dayPosted + "&schips=date_posted;" + dayPosted)
						: "";
				String q = URLEncoder.encode(keySearch, StandardCharsets.UTF_8.toString());
				String urls = "https://www.google.com/search?yv=3&rciv=jb&" + lrad + chips + "nfpr=0&" + "q=" + q
						+ "&start=" + start + "&asearch=jb_list&async=_id:VoQFxe,_pms:hts,_fmt:pc";
				String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

				// call api
				Document document = Jsoup.connect(urls)
						.header("Accept-Language",
								"ja;q=0.9,fr;q=0.8,de;q=0.7,es;q=0.6,it;q=0.5,nl;q=0.4,sv;q=0.3,nb;q=0.2")
						.userAgent(userAgent).get();

				// get value crawl
				List<Double> crawlResult = mainCrawlData(document, date, location, bukken_id);
				S10 += crawlResult.get(1);
				S2 += crawlResult.get(2);
				dataSize += crawlResult.get(3);
				if (countItem != 0) {
					sum += countItem;
					System.out.println("Total number of rows crawled: " + (sum));
				} else {
					System.out.println(sum + "," + count);
				}
				sumInWhile += crawlResult.get(0);
				start += 10;
			} catch (Exception e) {
			}
		}
		if ("10.0".equals(location)) {
			if (S10!=0) {
				station.setS10((double) Math.ceil(S10 / dataSize * 1000) / 1000);
			}
			else {
				station.setS10(0);
			}
		}
		// write csv
		writeCsv(date, sum, keySearch);
	}

	public List<Double> mainCrawlData(Document document, Date date, String location, int bukken_id) {

		double count = 0;
		int dataSize = 0;
		String jobName = "";
		String postPerson = "";
		String salaryMin = "";
		String salaryMax = "";
		String jobType = "";
		String provider = "";
		String dateStr = "";
		String lradStr = "2.0".equals(location) ? "2km" : "10km";
		double averageSalary = 0;
		double S2 = 0;
		double S10 = 0;
		Elements elms = document != null ? document.getElementsByClass("PwjeAc") : null;
		countItem = (int) (count = elms.size());

		if (elms != null)
			for (Element element : elms) {

				jobName = element.getElementsByClass("BjJfJf PUpOsf").text();
				postPerson = element.getElementsByClass("vNEEBe").text();
				provider = getProvider(element.getElementsByClass("Qk80Jf").text());
				dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);

				String exclusionFlag = "N";
				if (jobName.indexOf(LIST_EXCLUSION_FLAG[0]) != -1 || jobName.indexOf(LIST_EXCLUSION_FLAG[1]) != -1) {
					exclusionFlag = "Y";
				}

				jobType = "";
				salaryMin = "";
				salaryMax = "";
				Element el = element.getElementsByClass("oNwCmf").get(0);
				for (Element elChild : el.getElementsByClass("SuWscb")) {
					String elStr = elChild.text();
					if (Arrays.asList(LIST_JOB_TYPES).indexOf(elStr) != -1) {
						jobType = elChild.text();
					} else {
						if (elStr.indexOf(AN_HOUR) != -1) {
							String[] salary = elStr.replace(AN_HOUR, "").split("～");
							salaryMin = salary[0] != null ? salary[0] : "";
							if (salary.length == 1) {
								salaryMin = salary[0] != null ? formatSalary(salary[0]) : "";
							} else if (salary.length == 2) {
								salaryMin = salary[0] != null ? formatSalary(salary[0]) : "";
								salaryMax = salary[1] != null ? formatSalary(salary[1]) : "";
							}
						}
					}
				}
				String recruitmentID = "1" + provider + "2" + jobName + "3" + salaryMin + "4" + salaryMax + "5"
						+ jobType;
				String hashRecruitmentID = hashMd5(recruitmentID);

				if (listHashRecruitmentID.indexOf(hashRecruitmentID) == -1) {
					listHashRecruitmentID.add(hashRecruitmentID);
					String data = hashRecruitmentID + "," + bukken_id + "," + dateStr + "," + lradStr + ","
							+ exclusionFlag + "," + postPerson + "," + provider + "," + jobName + "," + salaryMin + ","
							+ salaryMax + "," + jobType;
					// Cong add external code
					if (salaryMin.contains("万") || salaryMax.contains("千")) {
						continue;
					}
					if (!"".equals(salaryMin)) {
						dataSize += 1;
						if ("10.0".equals(location)) {
							S10 += Double.parseDouble(salaryMin);
						} else if ("2.0".equals(location)) {
							S2 += Double.parseDouble(salaryMin);
						}

					}
					if (!"".equals(salaryMax)) {
						dataSize += 1;
						if ("10.0".equals(location)) {
							S10 += Double.parseDouble(salaryMax);
						} else if ("2.0".equals(location)) {
							S2 += Double.parseDouble(salaryMax);
						}
					}
					dataCsv.add(data);
				} else {
					// sum item of file output-- if recruitmentID exists
					if (countItem > 0) {
						countItem--;
					} else {
						countItem = 0;
					}
				}

//				hashRecruitmentID, bukken_id, date, lradStr, exclusionFlag, postPerson, provider, jobName, salaryMin, salaryMax, jobType

			}
		// Cong add external code
		List<Double> result = new ArrayList<>();
		result.add(count);
		result.add(S10);
		result.add(S2);
		result.add((double) dataSize);
		// Cong add external code
		return result;
	}

	public static void writeCsv(Date date, int sum, String keySearch) {
		try {
			String fileType = ".csv";
			String dateStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(date);

			String fileName = keySearch + "_" + dateStr + "_" + sum + "_" + fileType;
			File file = new File("E:\\dataCrawl\\dataCrawl" + fileName);
			PrintWriter writer = new PrintWriter(file);
			StringBuilder sb = new StringBuilder();

			// format font utf-8 when open file with excel
			sb.append("\uFEFF");

			// write header
			for (int i = 0; i < HEADER_CSV.length; i++) {
				if (i == HEADER_CSV.length - 1) {
					sb.append(HEADER_CSV[i]);
				} else {
					sb.append(HEADER_CSV[i] + ",");
				}
			}
			sb.append('\n');

			// write content
			for (int i = 0; i < dataCsv.size(); i++) {
				sb.append(dataCsv.get(i));
				sb.append('\n');
			}
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			System.out.println("write csv done! " + fileName);

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String hashMd5(String hashStr) {
		String hashtext = "";
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(hashStr.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			hashtext = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtext;
	}

	public static String formatSalary(String salary) {
		if (salary != null) {
			salary = salary.replace(AN_HOUR, "");
			salary = salary.replace("￥", "");
			salary = salary.replace(",", "");
		}
		return salary;
	}

	public static String getProvider(String provider) {
		if (provider != null) {
			if (provider.split(": ").length >= 2) {
				provider = provider.split(": ")[1];
			}
		}
		return provider;
	}

	// ***************************************** END CRAWLING
	// *****************************************
	public Page<Bukken> getMyBukkens(int accountId,int page){
		return bukkenRepo.findByAccountId(PageRequest.of(page, 12),accountId);
	}
	public int countMyBukkens(int accountId){
		return bukkenRepo.findByAccountId(accountId).size();
	}
	public String getMailOwner(int bukkenId) {
		Bukken thisBukken=getById(bukkenId);
		Set<Account> accounts=thisBukken.getAccounts();
		List<Account> accountsFilter=  accounts.stream()                // convert list to stream
        .filter(element -> element.getRole().getId()==2)     // we dont like mkyong
        .collect(Collectors.toList());
		return accountsFilter.get(0).getEmail();
	}
}
