package com.example.Warehouse.controllers;

import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.SearchDto;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.Station;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.services.BukkenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// add comment here
@RestController
@RequestMapping(value = "/rest-bukken")
public class BukkenController {
	@Autowired
	private BukkenService bukkenservice;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenList() {
		List<Bukken> bukkens = bukkenservice.getAll();
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/address/{lat}/{lng}/{radius}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Bukken>>> getBukkenByAddress(@PathVariable java.math.BigDecimal lat,
			@PathVariable java.math.BigDecimal lng, @PathVariable java.math.BigDecimal radius) {
		List<Bukken> bukkens = bukkenservice.filterByAddress(lat, lng, radius);
		ResponseDto<List<Bukken>> result = new ResponseDto<List<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Bukken>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Bukken>> createBukken(@Valid @RequestBody Bukken bukken) {
		Bukken thisBukken = bukkenservice.createBukken(bukken);
		ResponseDto<Bukken> result = new ResponseDto<Bukken>(thisBukken, HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseDto<Bukken>>(result, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/bukkens/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Bukken>> getBukkenById(@PathVariable int bukkenId) {
		Bukken thisBukken = bukkenservice.getById(bukkenId);
		ResponseDto<Bukken> result = new ResponseDto<Bukken>(thisBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Bukken>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/bukkens/{bukkenId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteBukkenById(@PathVariable int bukkenId) {
		bukkenservice.deleteBukken(bukkenId);
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
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/{accountId}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<Bukken>>> getMyBukkens(@PathVariable int accountId,@PathVariable int pageIndex) {
		Page<Bukken> bukkens = bukkenservice.getMyBukkens(accountId,pageIndex);
		ResponseDto<Page<Bukken>> result = new ResponseDto<Page<Bukken>>(bukkens, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<Bukken>>>(result, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/bukkens/myBukkens/count/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Object>> countMyBukkens(@PathVariable int accountId) {
		int count = bukkenservice.countMyBukkens(accountId);
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
		String mailOwner=bukkenservice.getMailOwner(bukkenId);
		ResponseDto<String> result = new ResponseDto<String>(mailOwner, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<String>>(result, HttpStatus.OK);
	}
}
