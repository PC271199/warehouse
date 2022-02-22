package com.example.Warehouse.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.entities.bukkenService.Tour;
import com.example.Warehouse.services.TourService;

// add comment here
@RestController
@RequestMapping(value = "/rest-tour")
public class TourController {

	@Autowired
	TourService tourService;

	// create new tour
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/tours/{bukkenId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Tour>> createTour(@PathVariable int bukkenId, @RequestBody Tour tour) {
		Tour thisTour = tourService.createTour(bukkenId, tour);
		ResponseDto<Tour> result = new ResponseDto<Tour>(thisTour, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Tour>>(result, HttpStatus.OK);
	}

	// get tour by bukkenId
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/tours/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<Tour>>> getToursByBukkenId(@PathVariable int bukkenId) {
		List<Tour> tours = tourService.getToursByBukkenId(bukkenId);
		ResponseDto<List<Tour>> result = new ResponseDto<List<Tour>>(tours, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<Tour>>>(result, HttpStatus.OK);
	}

	// delete tour by Id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/tours/{tourId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteTourById(@PathVariable int tourId) {
		tourService.deleteTourById(tourId);
		ResponseDto<Object> result = new ResponseDto<Object>("Delete Tour successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	// delete all tour
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER')")
	@RequestMapping(value = "/tours", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAllTour() {
		tourService.deleteAllTour();
		ResponseDto<Object> result = new ResponseDto<Object>("Delete Tours successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}
}
