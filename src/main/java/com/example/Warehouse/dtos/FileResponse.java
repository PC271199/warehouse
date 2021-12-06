package com.example.Warehouse.dtos;

public class FileResponse {

	private String IdFile;
	private String name;
	private String url;
	private String type;
	private long size;
	private Boolean attachFile =false;
	
	public FileResponse(String IdFile,String name, String url, String type, long size,Boolean attachFile) {
		super();
		this.IdFile=IdFile;
		this.name = name;
		this.url = url;
		this.type = type;
		this.size = size;
		this.attachFile=attachFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getIdFile() {
		return IdFile;
	}

	public void setIdFile(String idFile) {
		IdFile = idFile;
	}

	public Boolean isAttachFile() {
		return attachFile;
	}

	public void setAttachFile(Boolean attachFile) {
		this.attachFile = attachFile;
	}
}
