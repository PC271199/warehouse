package com.example.Warehouse.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import com.example.Warehouse.dtos.FileResponse;
import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.repositories.systemService.FileRepository;
import com.example.Warehouse.services.AccountService;
import com.example.Warehouse.services.FileService;

@RestController
@RequestMapping(value = "/rest-file")
public class FileController {
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private AccountService accService;

	@PostMapping("/files/upload/{bukkenId}")
	public ResponseEntity<ResponseDto<Object>> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable int bukkenId) {
		String message = "";
		try {
			fileService.store(file, bukkenId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			ResponseDto<Object> result = new ResponseDto<Object>(message, HttpStatus.OK.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			ResponseDto<Object> result = new ResponseDto<Object>(message, HttpStatus.EXPECTATION_FAILED.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping("/files/image/upload")
	public ResponseEntity<ResponseDto<Object>> uploadImage(@RequestParam("image") MultipartFile file,HttpServletRequest request) {
		String imgURL = "";
		try {
			File thisFile=fileService.storeImage(file);
			imgURL = request.getRequestURL().toString().replace(request.getRequestURI(), "")+"/rest-file/files/"+thisFile.getId();
			ResponseDto<Object> result = new ResponseDto<Object>(imgURL, HttpStatus.OK.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			imgURL = "";
			ResponseDto<Object> result = new ResponseDto<Object>(imgURL, HttpStatus.EXPECTATION_FAILED.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping("/files/image/upload/profile")
	public ResponseEntity<ResponseDto<Object>> uploadImageProfile(@RequestParam("image") MultipartFile file,HttpServletRequest request) {
		String imgURL = "";
		try {
			File thisFile=fileService.storeImage(file);
			imgURL = request.getRequestURL().toString().replace(request.getRequestURI(), "")+"/rest-file/files/"+thisFile.getId();
			accService.updateImgAva(imgURL);
			ResponseDto<Object> result = new ResponseDto<Object>(imgURL, HttpStatus.OK.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			imgURL = "";
			ResponseDto<Object> result = new ResponseDto<Object>(imgURL, HttpStatus.EXPECTATION_FAILED.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/files")
	public ResponseEntity<ResponseDto<List<FileResponse>>> getListFiles() {
		List<FileResponse> files = fileService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/rest-file/files/")
					.path(dbFile.getId().toString()).toUriString();

			return new FileResponse(dbFile.getId(),dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length,dbFile.isAttachFile());
		}).collect(Collectors.toList());

		ResponseDto<List<FileResponse>> result = new ResponseDto<List<FileResponse>>(files, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<FileResponse>>>(result, HttpStatus.OK);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		Optional<File> optionalFileDB = fileRepo.findById(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + optionalFileDB.get().getName() + "\"")
				.body(optionalFileDB.get().getData());
	}
	
	@GetMapping("/files/bukken/{bukkenId}")
	public ResponseEntity<ResponseDto<List<FileResponse>>> getListByBukkenId(@PathVariable int bukkenId) {
		List<FileResponse> files = fileService.getByBukkenId(bukkenId).map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/rest-file/files/")
					.path(dbFile.getId().toString()).toUriString();

			return new FileResponse(dbFile.getId(),dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length,dbFile.isAttachFile());
		}).collect(Collectors.toList());

		ResponseDto<List<FileResponse>> result = new ResponseDto<List<FileResponse>>(files, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<FileResponse>>>(result, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/files/{fileId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAll(@PathVariable String fileId) {
		fileService.deleteById(fileId);
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}
}
