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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Warehouse.dtos.SearchDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.BukkenStatus;
import com.example.Warehouse.entities.bukkenService.InterestedBukken;
import com.example.Warehouse.entities.bukkenService.Station;
import com.example.Warehouse.exceptions.accountService.AccountNotFoundException;
import com.example.Warehouse.exceptions.accountService.EmptyException;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.exceptions.bukkenService.BukkenNotFoundException;
import com.example.Warehouse.exceptions.bukkenService.InterestedExistException;
import com.example.Warehouse.exceptions.common.NullException;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.bukkenService.BukkenRepository;
import com.example.Warehouse.repositories.bukkenService.InterestedBukkenRepository;
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
	@Autowired
	private InterestedBukkenRepository interestedBukkenRepo;

	// get all bukkens
	public List<Bukken> getAll() {
		List<Bukken> result = bukkenRepo.findAll();
		result.sort(Comparator.comparing(Bukken::getName));
		if (result.size() > 0) {
			return result;
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countVisited
	public List<Bukken> getTopVisited() {
		List<Bukken> result = bukkenRepo.findAll();
		result.sort(Comparator.comparingInt(Bukken::getCountVisited).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countLike
	public List<Bukken> getTopLike() {
		List<Bukken> result = bukkenRepo.findAll();
		result.sort(Comparator.comparingInt(Bukken::getCountLike).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countSearch
	public List<Bukken> getTopSearch() {
		List<Bukken> result = bukkenRepo.findAll();
		result.sort(Comparator.comparingInt(Bukken::getCountSearch).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countSign
	public List<Bukken> getTopSign() {
		List<Bukken> result = bukkenRepo.findAll();
		result.sort(Comparator.comparingInt(Bukken::getCountSign).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countVisited by owner
	public List<Bukken> getTopVisited_ByOwner() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Set<Bukken> setBukkens = account.getBukkens();
		List<Bukken> result = new ArrayList<>(setBukkens);
		result.sort(Comparator.comparingInt(Bukken::getCountVisited).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the mose countLike by owner
	public List<Bukken> getTopLike_ByOwner() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Set<Bukken> setBukkens = account.getBukkens();
		List<Bukken> result = new ArrayList<>(setBukkens);
		result.sort(Comparator.comparingInt(Bukken::getCountLike).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the most countSearch by owner
	public List<Bukken> getTopSearch_ByOwner() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Set<Bukken> setBukkens = account.getBukkens();
		List<Bukken> result = new ArrayList<>(setBukkens);
		result.sort(Comparator.comparingInt(Bukken::getCountSearch).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get top 10 bukken have the most countSign by owner
	public List<Bukken> getTopSign_ByOwner() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Set<Bukken> setBukkens = account.getBukkens();
		List<Bukken> result = new ArrayList<>(setBukkens);
		result.sort(Comparator.comparingInt(Bukken::getCountSign).reversed());
		if (result.size() > 0) {
			return result.stream().limit(10).collect(Collectors.toList());
		} else {
			return result;
		}
	}

	// get all bukkens belong to ownerId
	public List<Bukken> getAll_ByOwnerId(int ownerId) {
		Optional<Account> thisAccount = accRepo.findById(ownerId);
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account thisOwner = thisAccount.get();
		List<Bukken> result = new ArrayList<Bukken>(thisOwner.getBukkens());
		result.sort(Comparator.comparing(Bukken::getName));
		return result;
	}

	public Page<Bukken> getAllByPage(int pageIndex) {
		Pageable pageable = PageRequest.of(pageIndex, 12, Sort.by("name"));
		Page<Bukken> result = bukkenRepo.findAll(pageable);
		if (result.getSize() > 0) {
			return result;
		} else {
			throw new EmptyException();
		}
	}

	// create bukken
	public Bukken createBukken(Bukken bukken) {
		try {
			List<Station> stationList = stationRepo.findAll();
			BigDecimal distance = calDistance(new BigDecimal(bukken.getLatitude()),
					new BigDecimal(bukken.getLongitude()), new BigDecimal(stationList.get(0).getLat()),
					new BigDecimal(stationList.get(0).getLng()));
			bukken.setStation(stationList.get(0));
			for (int i = 1; i < stationList.size(); i++) {
				BigDecimal temp = calDistance(new BigDecimal(bukken.getLatitude()),
						new BigDecimal(bukken.getLongitude()), new BigDecimal(stationList.get(i).getLat()),
						new BigDecimal(stationList.get(i).getLng()));
				if (temp.compareTo(distance) < 0) {
					distance = temp;
					bukken.setStation(stationList.get(i));
				}
			}

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if ("ROLE_OWNER".equals(authentication.getAuthorities().toArray()[0].toString())) {
				Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
				if (thisAccount.isPresent() == false) {
					throw new NullException();
				}
				bukken.setAccount(thisAccount.get());
			}
			bukkenRepo.save(bukken);

			List<Account> account_users = accRepo.findByRoleId(1);
			for (Account account : account_users) {
				if (account.getMatrix() != null) {
					account.getMatrix().put(bukken.getId(), 0);
				}
			}
			return bukken;
		} catch (Exception e) {
			return null;
		}
	}

	// update bukken
	public Bukken updateBukken(Bukken bukken) {
		try {
			List<Station> stationList = stationRepo.findAll();
			BigDecimal distance = calDistance(new BigDecimal(bukken.getLatitude()),
					new BigDecimal(bukken.getLongitude()), new BigDecimal(stationList.get(0).getLat()),
					new BigDecimal(stationList.get(0).getLng()));
			bukken.setStation(stationList.get(0));
			for (int i = 1; i < stationList.size(); i++) {
				BigDecimal temp = calDistance(new BigDecimal(bukken.getLatitude()),
						new BigDecimal(bukken.getLongitude()), new BigDecimal(stationList.get(i).getLat()),
						new BigDecimal(stationList.get(i).getLng()));
				if (temp.compareTo(distance) < 0) {
					distance = temp;
					bukken.setStation(stationList.get(i));
				}
			}
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
			bukkenRepo.save(bukken);
			return bukken;
		} catch (Exception e) {
			return null;
		}
	}

	public long countAll() {
		return bukkenRepo.count();
	}

	// get bukken by id
	public Bukken getById(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			Bukken bukkenResult = result.get();
			return bukkenResult;
		} else {
			throw new BukkenNotFoundException(bukkenId);
		}
	}

	// increase countvisited bukken by bukkenId
	public void increaseCountVisited(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			Bukken bukkenResult = result.get();
			int countTemp = bukkenResult.getCountVisited();
			bukkenResult.setCountVisited(countTemp + 1);
			bukkenRepo.save(bukkenResult);
		}
	}

	// increase countSign bukken by bukkenId
	public void increaseCountSign(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			Bukken bukkenResult = result.get();
			int countTemp = bukkenResult.getCountSign();
			bukkenResult.setCountSign(countTemp + 1);
			bukkenRepo.save(bukkenResult);
		}
	}

	// delete by bukkenId
	public void deleteBukken(int bukkenId) {
		Optional<Bukken> oldBukken = bukkenRepo.findById(bukkenId);
		if (!oldBukken.isPresent()) {
			throw new BukkenNotFoundException(bukkenId);
		}
		bukkenRepo.deleteById(bukkenId);
	}

	// delete matrix with key bukkenId
	public void deleteMatrix_ByBukkenId(int bukkenId) {
		List<Account> result = accRepo.findAll();
		for (Account account : result) {
			if (account.getMatrix() != null) {
				account.getMatrix().remove(bukkenId);
			}
		}
		accRepo.saveAll(result);
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
				BigDecimal distance = calDistance(new BigDecimal(bukken.getLatitude()),
						new BigDecimal(bukken.getLongitude()), new BigDecimal(stations.get(0).getLat()),
						new BigDecimal(stations.get(0).getLng()));
				bukken.setStation(stations.get(0));
				for (int i = 1; i < stations.size(); i++) {
					BigDecimal temp = calDistance(new BigDecimal(bukken.getLatitude()),
							new BigDecimal(bukken.getLongitude()), new BigDecimal(stations.get(i).getLat()),
							new BigDecimal(stations.get(i).getLng()));
					if (temp.compareTo(distance) < 0) {
						distance = temp;
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
			Account temp = new Account();
			temp = accountList.get(rand.nextInt(accountList.size()));
			bukken.setAccount(temp);
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

	// save list bukken
	public void save(List<Bukken> bukkenList) {
		bukkenRepo.saveAll(bukkenList);
	}

	// save bukken
	public void save(Bukken bukken) {
		bukkenRepo.save(bukken);
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
		result = bukkens.stream().filter(element -> calDistance(CenterLat, CenterLng,
				new BigDecimal(element.getLatitude()), new BigDecimal(element.getLongitude())).compareTo(radius) <= 0)
				.collect(Collectors.toList());
		for (Bukken bukken : result) {
			increaseCountSearch(bukken.getId());
		}
		return bukkenRepo.saveAll(result);
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

	// get bukkens by search shape
	public List<Bukken> getBukkensBySearchShape(SearchDto searchDto) {
		List<Bukken> bukkens = getAll();
		List<Bukken> circleResult = new ArrayList<Bukken>();
		List<Bukken> polygonResult = new ArrayList<Bukken>();
		List<Bukken> bukkensLikeName = new ArrayList<Bukken>();
		List<Bukken> bukkensOpening = new ArrayList<Bukken>();
		List<Bukken> bukkensComplete = new ArrayList<Bukken>();
		List<Bukken> bukkensNotOpening = new ArrayList<Bukken>();
		List<Bukken> bukkensNotComplete = new ArrayList<Bukken>();
		List<Bukken> bukkensConfigured = new ArrayList<Bukken>();
		List<Bukken> bukkensVR = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinFloor = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxFloor = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinFee = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxFee = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinArea = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxArea = new ArrayList<Bukken>();
		List<Bukken> bukkensByDeliveryDate = new ArrayList<Bukken>();
		Set<Bukken> resultSet = new HashSet<Bukken>();

		if (!searchDto.getCircleDraws().equals("")) {
			circleResult = getBukkensFromCircleDraws(searchDto.getCircleDraws());
			resultSet.addAll(circleResult);
		}
		if (!searchDto.getPolygonDraws().equals("")) {
			polygonResult = getBukkensFromPolygonDraws(searchDto.getPolygonDraws(), searchDto.getPoints());
			resultSet.addAll(polygonResult);
		}
		if (!searchDto.getCircleDraws().equals("") || !searchDto.getPolygonDraws().equals("")) {
			bukkens = bukkens.stream().distinct().filter(new ArrayList<Bukken>(resultSet)::contains)
					.collect(Collectors.toList());
		}
		if (!searchDto.getName().equals("")) {
			bukkensLikeName = getBukkenLikeName(searchDto.getName());
			bukkens = bukkens.stream().distinct().filter(bukkensLikeName::contains).collect(Collectors.toList());
		}
		if (searchDto.isOpening()) {
			bukkensOpening = getBukkenOpening();
			bukkens = bukkens.stream().distinct().filter(bukkensOpening::contains).collect(Collectors.toList());
		}
		if (searchDto.isNotopening()) {
			bukkensNotOpening = getBukkenNotOpening();
			bukkens = bukkens.stream().distinct().filter(bukkensNotOpening::contains).collect(Collectors.toList());
		}
		if (searchDto.isComplete()) {
			bukkensComplete = getBukkenComplete();
			bukkens = bukkens.stream().distinct().filter(bukkensComplete::contains).collect(Collectors.toList());
		}
		if (searchDto.isNotcomplete()) {
			bukkensNotComplete = getBukkenNotComplete();
			bukkens = bukkens.stream().distinct().filter(bukkensNotComplete::contains).collect(Collectors.toList());
		}
		if (searchDto.isConfigured()) {
			bukkensConfigured = getBukkenConfigured();
			bukkens = bukkens.stream().distinct().filter(bukkensConfigured::contains).collect(Collectors.toList());
		}
		if (searchDto.isVr()) {
			bukkensVR = getBukkenVR();
			bukkens = bukkens.stream().distinct().filter(bukkensVR::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinFloor() > 0) {
			bukkensByMinFloor = getBukkenByMinFloor(searchDto.getMinFloor());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinFloor::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxFloor() > 0) {
			bukkensByMaxFloor = getBukkenByMaxFloor(searchDto.getMaxFloor());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxFloor::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinFee() > 0) {
			bukkensByMinFee = getBukkenByMinFee(searchDto.getMinFee());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinFee::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxFee() > 0) {
			bukkensByMaxFee = getBukkenByMaxFee(searchDto.getMaxFee());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxFee::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinArea() > 0) {
			bukkensByMinArea = getBukkenByMinArea(searchDto.getMinArea());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinArea::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxArea() > 0) {
			bukkensByMaxArea = getBukkenByMaxArea(searchDto.getMaxArea());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxArea::contains).collect(Collectors.toList());
		}
		if (!"".equals(searchDto.getDeliveryDate())) {
			try {
				Date thisDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDto.getDeliveryDate());
				bukkensByDeliveryDate = getBukkenByDeliveryDate(thisDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bukkens = bukkens.stream().distinct().filter(bukkensByDeliveryDate::contains).collect(Collectors.toList());
		}
		for (Bukken bukken : bukkens) {
			increaseCountSearch(bukken.getId());
		}
		return bukkenRepo.saveAll(bukkens);
	}

	// get bukkens by search address
	public List<Bukken> getBukkensBySearchAddress(@PathVariable java.math.BigDecimal lat,
			@PathVariable java.math.BigDecimal lng, @PathVariable java.math.BigDecimal radius, SearchDto searchDto) {
		List<Bukken> bukkens = getAll();
		List<Bukken> bukkensAddress = filterByAddress(lat, lng, radius);
		List<Bukken> bukkensLikeName = new ArrayList<Bukken>();
		List<Bukken> bukkensOpening = new ArrayList<Bukken>();
		List<Bukken> bukkensComplete = new ArrayList<Bukken>();
		List<Bukken> bukkensNotOpening = new ArrayList<Bukken>();
		List<Bukken> bukkensNotComplete = new ArrayList<Bukken>();
		List<Bukken> bukkensConfigured = new ArrayList<Bukken>();
		List<Bukken> bukkensVR = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinFloor = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxFloor = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinFee = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxFee = new ArrayList<Bukken>();
		List<Bukken> bukkensByMinArea = new ArrayList<Bukken>();
		List<Bukken> bukkensByMaxArea = new ArrayList<Bukken>();
		List<Bukken> bukkensByDeliveryDate = new ArrayList<Bukken>();
		bukkens = bukkens.stream().distinct().filter(new ArrayList<Bukken>(bukkensAddress)::contains)
				.collect(Collectors.toList());
		if (!searchDto.getName().equals("")) {
			bukkensLikeName = getBukkenLikeName(searchDto.getName());
			bukkens = bukkens.stream().distinct().filter(bukkensLikeName::contains).collect(Collectors.toList());
		}
		if (searchDto.isOpening()) {
			bukkensOpening = getBukkenOpening();
			bukkens = bukkens.stream().distinct().filter(bukkensOpening::contains).collect(Collectors.toList());
		}
		if (searchDto.isNotopening()) {
			bukkensNotOpening = getBukkenNotOpening();
			bukkens = bukkens.stream().distinct().filter(bukkensNotOpening::contains).collect(Collectors.toList());
		}
		if (searchDto.isComplete()) {
			bukkensComplete = getBukkenComplete();
			bukkens = bukkens.stream().distinct().filter(bukkensComplete::contains).collect(Collectors.toList());
		}
		if (searchDto.isNotcomplete()) {
			bukkensNotComplete = getBukkenNotComplete();
			bukkens = bukkens.stream().distinct().filter(bukkensNotComplete::contains).collect(Collectors.toList());
		}
		if (searchDto.isConfigured()) {
			bukkensConfigured = getBukkenConfigured();
			bukkens = bukkens.stream().distinct().filter(bukkensConfigured::contains).collect(Collectors.toList());
		}
		if (searchDto.isVr()) {
			bukkensVR = getBukkenVR();
			bukkens = bukkens.stream().distinct().filter(bukkensVR::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinFloor() > 0) {
			bukkensByMinFloor = getBukkenByMinFloor(searchDto.getMinFloor());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinFloor::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxFloor() > 0) {
			bukkensByMaxFloor = getBukkenByMaxFloor(searchDto.getMaxFloor());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxFloor::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinFee() > 0) {
			bukkensByMinFee = getBukkenByMinFee(searchDto.getMinFee());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinFee::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxFee() > 0) {
			bukkensByMaxFee = getBukkenByMaxFee(searchDto.getMaxFee());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxFee::contains).collect(Collectors.toList());
		}
		if (searchDto.getMinArea() > 0) {
			bukkensByMinArea = getBukkenByMinArea(searchDto.getMinArea());
			bukkens = bukkens.stream().distinct().filter(bukkensByMinArea::contains).collect(Collectors.toList());
		}
		if (searchDto.getMaxArea() > 0) {
			bukkensByMaxArea = getBukkenByMaxArea(searchDto.getMaxArea());
			bukkens = bukkens.stream().distinct().filter(bukkensByMaxArea::contains).collect(Collectors.toList());
		}
		if (!"".equals(searchDto.getDeliveryDate())) {
			try {
				Date thisDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDto.getDeliveryDate());
				bukkensByDeliveryDate = getBukkenByDeliveryDate(thisDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bukkens = bukkens.stream().distinct().filter(bukkensByDeliveryDate::contains).collect(Collectors.toList());
		}
		for (Bukken bukken : bukkens) {
			increaseCountSearch(bukken.getId());
		}
		return bukkenRepo.saveAll(bukkens);
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
			if (S10 != 0) {
				station.setS10((double) Math.ceil(S10 / dataSize * 1000) / 1000);
			} else {
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
	public Page<Bukken> getMyBukkens(int accountId, int page) {
		Pageable pageable = PageRequest.of(page, 12, Sort.by("name"));
		return bukkenRepo.findByAccountId(pageable, accountId);
	}

	public int countMyBukkens(int accountId) {
		return bukkenRepo.findByAccountId(accountId).size();
	}

	public Page<Bukken> getMyBukkens_LikeName(int accountId, int page, String bukkenName) {
		return bukkenRepo.findByAccountId_BukkenName(PageRequest.of(page, 12), accountId, bukkenName);
	}

	public int countMyBukkens_LikeName(int accountId, String bukkenName) {
		return bukkenRepo.findByAccountId_BukkenName(accountId, bukkenName).size();
	}

	public String getMailOwner(int bukkenId) {
		Bukken thisBukken = getById(bukkenId);
		Account account = thisBukken.getAccount();
		return account.getEmail();
	}

	// get bukken like bukkenName by page
	public Page<Bukken> getBukkenLikeNameByPage(int pageIndex, String bukkenName) {
		Pageable pageable = PageRequest.of(pageIndex, 12, Sort.by("name"));
		return bukkenRepo.findByLikeBukkenNameByPage(pageable, bukkenName);
	}

	// get bukken like bukkenName
	public List<Bukken> getBukkenLikeName(String bukkenName) {
		return bukkenRepo.findByLikeBukkenName(bukkenName);
	}

	// get bukken opening
	public List<Bukken> getBukkenOpening() {
		return bukkenRepo.findByOpening();
	}

	// get bukken complete
	public List<Bukken> getBukkenComplete() {
		return bukkenRepo.findByComplete();
	}

	// get bukken not opening
	public List<Bukken> getBukkenNotOpening() {
		return bukkenRepo.findByNotOpening();
	}

	// get bukken not complete
	public List<Bukken> getBukkenNotComplete() {
		return bukkenRepo.findByNotComplete();
	}

	// get bukken configured
	public List<Bukken> getBukkenConfigured() {
		return bukkenRepo.findByConfigured();
	}

	// get bukken vr
	public List<Bukken> getBukkenVR() {
		return bukkenRepo.findByVR();
	}

	// get bukken by min floor
	public List<Bukken> getBukkenByMinFloor(int minFloor) {
		return bukkenRepo.findByMinFloor(minFloor);
	}

	// get bukken by max floor
	public List<Bukken> getBukkenByMaxFloor(int maxFloor) {
		return bukkenRepo.findByMaxFloor(maxFloor);
	}

	// get bukken by min fee
	public List<Bukken> getBukkenByMinFee(int minFee) {
		return bukkenRepo.findByMinFee(minFee);
	}

	// get bukken by max fee
	public List<Bukken> getBukkenByMaxFee(int maxFee) {
		return bukkenRepo.findByMaxFee(maxFee);
	}

	// get bukken by min area
	public List<Bukken> getBukkenByMinArea(int minArea) {
		return bukkenRepo.findByMinArea(minArea);
	}

	// get bukken by max area
	public List<Bukken> getBukkenByMaxArea(int maxArea) {
		return bukkenRepo.findByMaxArea(maxArea);
	}

	// get bukken by delivery date
	public List<Bukken> getBukkenByDeliveryDate(Date deliveryDate) {
		List<Bukken> result = bukkenRepo.findByDeliveryDate(deliveryDate);
		return result;
	}

	// count bukken like bukkenName by page
	public int countBukkenLikeName(String bukkenName) {
		return bukkenRepo.countByLikeBukkenName(bukkenName);
	}

	// add interested Bukken for my account
	public void addInterestedBukken(int bukkenId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Bukken thisBukken = getById(bukkenId);
		thisBukken.setCountLike(thisBukken.getCountLike() + 1);
		bukkenRepo.save(thisBukken);
		Optional<InterestedBukken> thisInterestedBukken = interestedBukkenRepo
				.findByAccountIdBukkenId(thisBukken.getId(), account.getId());
		if (!thisInterestedBukken.isPresent()) {
			InterestedBukken interestedBukken = new InterestedBukken();
			interestedBukken.setBukken(thisBukken);
			interestedBukken.setAccount(account);
			interestedBukkenRepo.save(interestedBukken);
		} else {
			throw new InterestedExistException();
		}
	}

	// increase countLike bukken by bukkenId
	public void increaseCountLike(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			Bukken bukkenResult = result.get();
			int countTemp = bukkenResult.getCountLike();
			bukkenResult.setCountLike(countTemp + 1);
			bukkenRepo.save(bukkenResult);
		}
	}

	// increase countSearch bukken by bukkenId
	public void increaseCountSearch(int bukkenId) {
		Optional<Bukken> result = bukkenRepo.findById(bukkenId);
		if (result.isPresent()) {
			Bukken bukkenResult = result.get();
			int countTemp = bukkenResult.getCountSearch();
			bukkenResult.setCountSearch(countTemp + 1);
		}
	}

	// change map user bukken in matrix
	public void changeMapUserBukken(int bukkenId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		Map<Integer, Integer> matrix = account.getMatrix();
		if (matrix.containsKey(bukkenId)) {
			matrix.put(bukkenId, matrix.get(bukkenId) + 1);
			accRepo.save(account);
		}
	}

	// get list Bukken bookmark by page
	public Page<Bukken> getListBukkenBookmark(int pageIndex, int accountId) {
		Pageable pageable = PageRequest.of(pageIndex, 12);
		Page<InterestedBukken> interestedBukkenList = interestedBukkenRepo.findByAccountIdNew(pageable, accountId);
		Page<Bukken> result = interestedBukkenList.map(new Function<InterestedBukken, Bukken>() {
			@Override
			public Bukken apply(InterestedBukken entity) {
				return entity.getBukken();
			}
		});
		return result;
	}

	// count list Bukken bookmark
	public int countListBukkenBookmark(int accountId) {
		List<InterestedBukken> interestedBukkenList = interestedBukkenRepo.findByAccountId(accountId);
		if (interestedBukkenList.size() > 0) {
			return interestedBukkenList.size();
		} else {
			throw new NullException();
		}
	}

	// get bukkens in 10km
	public List<Bukken> getNearestBukken10km(int bukkenId) {
		Bukken centerBukken = getById(bukkenId);
		List<Bukken> bukkens = getAll();
		List<Bukken> result = new ArrayList<Bukken>();
		result = bukkens.stream()
				.filter(element -> calDistance(new BigDecimal(centerBukken.getLatitude()),
						new BigDecimal(centerBukken.getLongitude()), new BigDecimal(element.getLatitude()),
						new BigDecimal(element.getLongitude())).compareTo(new BigDecimal(10)) <= 0
						&& calDistance(new BigDecimal(centerBukken.getLatitude()),
								new BigDecimal(centerBukken.getLongitude()), new BigDecimal(element.getLatitude()),
								new BigDecimal(element.getLongitude())).compareTo(new BigDecimal(0)) > 0)
				.collect(Collectors.toList());
		return result;
	}

	//////////////////////////////////////
	// generate random delivery date
	public void generateRandomDate() {
		List<Bukken> bukkens = getAll();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		for (Bukken bukken : bukkens) {
			LocalDate randomDate = createRandomDate(2000, 2030);
			Date date = Date.from(randomDate.atStartOfDay(defaultZoneId).toInstant());
			bukken.setDeliveryDate(date);
		}
		bukkenRepo.saveAll(bukkens);
	}

	public static LocalDate createRandomDate(int startYear, int endYear) {
		int day = createRandomIntBetween(1, 28);
		int month = createRandomIntBetween(1, 12);
		int year = createRandomIntBetween(startYear, endYear);
		return LocalDate.of(year, month, day);
	}

	public static int createRandomIntBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	//////////////////////////////////////
}
