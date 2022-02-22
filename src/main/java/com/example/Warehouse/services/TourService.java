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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Warehouse.dtos.SearchDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.BukkenStatus;
import com.example.Warehouse.entities.bukkenService.InterestedBukken;
import com.example.Warehouse.entities.bukkenService.Station;
import com.example.Warehouse.entities.bukkenService.Tour;
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
import com.example.Warehouse.repositories.bukkenService.TourRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfJoins;
import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Service
public class TourService {
	@Autowired
	TourRepository tourRepo;
	@Autowired
	private BukkenRepository bukkenRepo;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	// create tour
	public Tour createTour(int bukkenId, Tour tour) {
		Optional<Bukken> optionBukken = bukkenRepo.findById(bukkenId);
		if (optionBukken.isPresent()) {
			Bukken thisBukken = optionBukken.get();
			Tour result = new Tour();
			result.setBukken(thisBukken);
			result.setName(tour.getName());
			thisBukken.setVr(true);

			HttpHeaders headers = new HttpHeaders();
			String authHeader = "Bearer " + tourRepo.save(result).getId();
			headers.set("Authorization", authHeader);
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			// Yêu cầu trả về định dạng JSON
			headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject object = new JSONObject();
			object.put("name", tour.getName());
			HttpEntity<String> entity = new HttpEntity<String>(object.toString(), headers);
			// RestTemplate
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
					"https://stage-vision-api.euclidxr.com/tours/create", HttpMethod.POST, entity, String.class);
			try {
				JsonNode root = objectMapper.readTree(response.getBody());
				result.setTourUUID(root.get("data").get("id").asText());
				result.setIdIframe("https://vision-viewer.euclidxr.com/" + root.get("data").get("id").asText());
				tourRepo.save(result);
				bukkenRepo.save(thisBukken);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tourRepo.save(result);
		} else {
			throw new BukkenNotFoundException(bukkenId);
		}
	}

	// delete all tours
	public void deleteAllTour() {
		tourRepo.deleteAll();
	}

	// delete tour by Id
	public void deleteTourById(int tourId) {
		tourRepo.deleteById(tourId);
	}

	// get tours by bukkenId
	public List<Tour> getToursByBukkenId(int bukkenId) {
		return tourRepo.findByBukkenId(bukkenId);
	}
}
