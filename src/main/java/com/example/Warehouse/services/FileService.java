package com.example.Warehouse.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Warehouse.dtos.FileResponse;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.repositories.bukkenService.BukkenRepository;
import com.example.Warehouse.repositories.systemService.FileRepository;
import org.springframework.util.StringUtils;
@Service
public class FileService {
	
	@Autowired
    private FileRepository fileRepo;
	
	@Autowired
    private BukkenService bukkenService;
	
    public File store(MultipartFile file, int bukkenId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File result = new File(fileName, file.getContentType(), file.getBytes());
        Bukken thisBukken=bukkenService.getById(bukkenId);
        result.setBukken(thisBukken);
        return fileRepo.save(result);
    }
    
    public File findById(String id) {
    	Optional<File> optionalFile= fileRepo.findById(id);
    	if (optionalFile.isPresent()) {
    		return optionalFile.get();
    	}
    	else {
    		return null;
    	}
    }
    
    public List<File> fileResponse2File(List<FileResponse> fileResponses){
    	List<File> result=new ArrayList<File>();
    	for (FileResponse file : fileResponses) {
			File thisFile=findById(file.getIdFile());
			if (thisFile != null) {
				thisFile.setAttachFile(file.isAttachFile());
				result.add(thisFile);
			}
		}
    	if (result.size() > 0) {
    		fileRepo.saveAll(result);
    		return result;
    	}
    	else {
    		return null;
    	}
    }
    
    public Stream<File> getAllFiles() {
        return fileRepo.findAll().stream();
    }
    
    public Stream<File> getByBukkenId(int bukkenId){
    	return fileRepo.findByBukkenId(bukkenId).stream();
    }
}
