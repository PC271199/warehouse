package com.example.Warehouse.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.SearchDto;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.Station;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.repositories.systemService.FileRepository;
import com.example.Warehouse.services.AccountService;
import com.example.Warehouse.services.BukkenService;
import com.example.Warehouse.util.BukkenExcelExporter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// add comment here
@RestController
@RequestMapping(value = "/rest-bukken")
public class BukkenController {
	@Autowired
	private BukkenService bukkenservice;
	@Autowired
	private FileRepository fileRepo;
	@Autowired
	private AccountService accser;

	// get all bukkens
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenList() {
		List<Bukken> bukkens = bukkenservice.getAll();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most countvisited
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countVisited", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopVisited() {
		List<Bukken> bukkens = bukkenservice.getTopVisited();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most like
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countLike", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopLike() {
		List<Bukken> bukkens = bukkenservice.getTopLike();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most search
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countSearch", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopSearch() {
		List<Bukken> bukkens = bukkenservice.getTopSearch();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most countvisited by owner
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countVisited/owner", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopVisited_ByOwner() {
		List<Bukken> bukkens = bukkenservice.getTopVisited_ByOwner();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most like by owner
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countLike/owner", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopLike_ByOwner() {
		List<Bukken> bukkens = bukkenservice.getTopLike_ByOwner();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get top 10 bukken have the most search by owner
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/countSearch/owner", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getTopSearch_ByOwner() {
		List<Bukken> bukkens = bukkenservice.getTopSearch_ByOwner();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// get all bukkens belong to ownerId
	@PreAuthorize("hasRole('ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/owner", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenList_ByOwnerId(@RequestParam int ownerId) {
		List<Bukken> bukkens = bukkenservice.getAll_ByOwnerId(ownerId);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/address/{lat}/{lng}/{radius}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenByAddress(@PathVariable java.math.BigDecimal lat,
			@PathVariable java.math.BigDecimal lng, @PathVariable java.math.BigDecimal radius,
			@RequestBody SearchDto searchDto) {
//		List<Bukken> bukkens = bukkenservice.filterByAddress(lat, lng, radius);
		List<Bukken> bukkens = bukkenservice.getBukkensBySearchAddress(lat, lng, radius, searchDto);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// create bukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Bukken>> createBukken(@Valid @RequestBody Bukken bukken) {
		Bukken thisBukken = bukkenservice.createBukken(bukken);
		ResponseDto<Bukken> result = new ResponseDto<Bukken>(thisBukken, HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseDto<Bukken>>(result, HttpStatus.CREATED);
	}

	// update bukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<Bukken>> updateBukken(@Valid @RequestBody Bukken bukken) {
		Bukken thisBukken = bukkenservice.updateBukken(bukken);
		ResponseDto<Bukken> result = new ResponseDto<Bukken>(thisBukken, HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseDto<Bukken>>(result, HttpStatus.CREATED);
	}

	// get bukken by id
	@RequestMapping(value = "/bukkens/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Bukken>> getBukkenById(@PathVariable int bukkenId) {
		Bukken thisBukken = bukkenservice.getById(bukkenId);
		bukkenservice.increaseCountVisited(bukkenId);
		ResponseDto<Bukken> result = new ResponseDto<Bukken>(thisBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Bukken>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/{bukkenId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteBukkenById(@PathVariable int bukkenId) {
		bukkenservice.deleteBukken(bukkenId);
		bukkenservice.deleteMatrix_ByBukkenId(bukkenId);
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAll() {
		bukkenservice.deleteAll();
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/bukkens/page/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getBukkenListByPage(@PathVariable int pageIndex) {
		Page<Bukken> bukkens = bukkenservice.getAllByPage(pageIndex);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/bukkens/count", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Long>> countAll() {
		long count = bukkenservice.countAll();
		ResponseDto<Long> result = new ResponseDto<Long>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Long>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/bukkens/data", method = RequestMethod.POST)
	public ResponseEntity<Object> importDataBukken() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Bukken>> typeReference = new TypeReference<List<Bukken>>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/file/dataBukken.json");
		try {
			List<Bukken> bukkens = mapper.readValue(inputStream, typeReference);
			bukkenservice.importBukken(bukkens);
		} catch (Exception e) {
			throw new ImportFailException();
		}
		return new ResponseEntity<Object>("import bukkens successfully", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/bukkens/data/station", method = RequestMethod.POST)
	public ResponseEntity<Object> importDataStation() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Station>> typeReference = new TypeReference<List<Station>>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/file/dataStation.json");
		try {
			List<Station> stations = mapper.readValue(inputStream, typeReference);
			bukkenservice.importStation(stations);
		} catch (Exception e) {
			throw new ImportFailException();
		}
		return new ResponseEntity<Object>("import stations successfully", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/bukkens/trigger", method = RequestMethod.GET)
	public ResponseEntity<Object> triggerStation() {
		bukkenservice.triggerOwnerBukken();
		return new ResponseEntity<Object>("Trigger bukkens successfully", HttpStatus.OK);
	}

	// get my bukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/{accountId}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getMyBukkens(@PathVariable int accountId,
			@PathVariable int pageIndex) {
		Page<Bukken> bukkens = bukkenservice.getMyBukkens(accountId, pageIndex);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}

	// count my bukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/count/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Object>> countMyBukkens(@PathVariable int accountId) {
		int count = bukkenservice.countMyBukkens(accountId);
		ResponseDto<Object> result = new ResponseDto<Object>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	// get my bukken like bukken name
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/{accountId}/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getMyBukkens_LikeName(@PathVariable int accountId,
			@PathVariable int pageIndex, @RequestParam String bukkenName) {
		Page<Bukken> bukkens = bukkenservice.getMyBukkens_LikeName(accountId, pageIndex, bukkenName);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}

	// count my bukken like name
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/count/{accountId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Object>> countMyBukkens_LikeName(@PathVariable int accountId,
			@RequestParam String bukkenName) {
		int count = bukkenservice.countMyBukkens_LikeName(accountId, bukkenName);
		ResponseDto<Object> result = new ResponseDto<Object>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/circleDraws/{circleDrawJson}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenByCircleDraws(@PathVariable String circleDrawJson) {
		List<Bukken> bukkens = bukkenservice.getBukkensFromCircleDraws(circleDrawJson);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/polygonDraws/{polygonJson}/{pointsJson}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenByPolygonDraws(@PathVariable String polygonJson,
			@PathVariable String pointsJson) {
		List<Bukken> bukkens = bukkenservice.getBukkensFromPolygonDraws(polygonJson, pointsJson);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/searchShape", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenBySearchShape(@RequestBody SearchDto searchDto) {
		List<Bukken> bukkens = bukkenservice.getBukkensBySearchShape(searchDto);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/bukkens/crawlSalary", method = RequestMethod.GET)
	public ResponseEntity<Object> crawlSalary() {
		bukkenservice.crawlAllStation();
		return new ResponseEntity<Object>("Crawl data successfully", HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/getOwner/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<String>> getOwnerBukken(@PathVariable int bukkenId) {
		String mailOwner = bukkenservice.getMailOwner(bukkenId);
		ResponseDto<String> result = new ResponseDto<String>(mailOwner, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<String>>(result, HttpStatus.OK);
	}

	// search like bukkenName by page
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/bukkenName/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getBukkenLikeNameByPage(@PathVariable int pageIndex,
			@RequestParam String bukkenName) {
		Page<Bukken> bukkenList = bukkenservice.getBukkenLikeNameByPage(pageIndex, bukkenName);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkenList, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}

	// search like bukkenName
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/bukkenName", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenLikeName(@RequestParam String bukkenName) {
		List<Bukken> bukkenList = bukkenservice.getBukkenLikeName(bukkenName);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkenList, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// count search like bukkenName
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/bukkenName/count", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Integer>> countSearchByLikeBukkenName(@RequestParam String bukkenName) {
		int bukkenList = bukkenservice.countBukkenLikeName(bukkenName);
		ResponseDto<Integer> result = new ResponseDto<Integer>(bukkenList, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Integer>>(result, HttpStatus.OK);
	}

	// add interested Bukken for my account
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/interested/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Object>> addInterestedBukken(@PathVariable int bukkenId) {
		bukkenservice.addInterestedBukken(bukkenId);
		bukkenservice.increaseCountLike(bukkenId);
		bukkenservice.changeMapUserBukken(bukkenId);
		ResponseDto<Object> result = new ResponseDto<Object>("Bookmark successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	// get interested Bukken for my account
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/bukkens/interested/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getInterestedBukkenList(@PathVariable int pageIndex,
			@RequestParam int accountId) {
		Page<Bukken> bukkenList = bukkenservice.getListBukkenBookmark(pageIndex, accountId);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkenList, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}

	// count interested Bukken for my account
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/bukkens/interested/count/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Integer>> countInterestedBukkenList(@PathVariable int accountId) {
		int count = bukkenservice.countListBukkenBookmark(accountId);
		ResponseDto<Integer> result = new ResponseDto<Integer>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Integer>>(result, HttpStatus.OK);
	}

	// get bukkens in 10km
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/nearest/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getNearestBukken10km(@PathVariable int bukkenId) {
		List<Bukken> bukkens = bukkenservice.getNearestBukken10km(bukkenId);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// export data bukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/users/export/excel", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<String>> exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Bukken> bukkenList = bukkenservice.getAll();
		bukkenList.sort(Comparator.comparing(Bukken::getId));

		BukkenExcelExporter excelExporter = new BukkenExcelExporter(bukkenList);

		com.example.Warehouse.entities.fileService.File resultFile = excelExporter.export(response);
		fileRepo.save(resultFile);

		ResponseDto<String> result = new ResponseDto<String>(resultFile.getId(), HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<String>>(result, HttpStatus.OK);
	}

	// get bukken recommendation
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/recommendation", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getRecommendation() {
		List<Integer> userIds = accser.getUsersSimilarity();
		List<Bukken> bukkensSimilarity = accser.getBukkensSimilarity(userIds);
		List<Bukken> bukkensRecommendation = accser.getBukkensRecommendation(bukkensSimilarity);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkensRecommendation, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	// just for test
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Object>> test(HttpServletRequest request) {
		System.out.println(request.getRequestURL().toString().replace(request.getRequestURI(), ""));
		ResponseDto<Object> result = new ResponseDto<Object>("ok", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

}
