package com.example.Warehouse.dtos.scheduleService;

import java.util.List;

import com.example.Warehouse.dtos.FileResponse;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;

public class ScheduleBukkenDto {
	private ScheduleBukken scheduleBukken;
	private List<FileResponse> files;
	
	public ScheduleBukkenDto() {
		super();
	}
	public ScheduleBukkenDto(ScheduleBukken scheduleBukken, List<FileResponse> files) {
		super();
		this.scheduleBukken = scheduleBukken;
		this.files = files;
	}
	public ScheduleBukken getScheduleBukken() {
		return scheduleBukken;
	}
	public void setScheduleBukken(ScheduleBukken scheduleBukken) {
		this.scheduleBukken = scheduleBukken;
	}
	public List<FileResponse> getFiles() {
		return files;
	}
	public void setFiles(List<FileResponse> files) {
		this.files = files;
	}
	
	
}
