package com.example.Warehouse.entities.fileService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.bukkenService.Bukken;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
@Entity
@Table(name = "file", schema = "public")
public class File {
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    @Lob
    private byte[] data;
    @Column
    private Boolean attachFile;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bukken_id")
	private Bukken bukken;
    
	public File() {
		super();
		// TODO Auto-generated constructor stub
	}

	public File(String name, String type, byte[] data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Bukken getBukken() {
		return bukken;
	}

	public void setBukken(Bukken bukken) {
		this.bukken = bukken;
	}

	public Boolean isAttachFile() {
		return attachFile;
	}

	public void setAttachFile(Boolean attachFile) {
		this.attachFile = attachFile;
	}
    
}
