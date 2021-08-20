package com.example.Warehouse.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "permission", schema = "public")
public class Permission {
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@NotBlank(message = "actionname is mandatory")
	@Length(max = 50, message = "rolename has max-length = 50")
	private String actname;

	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "permissions")
	@JsonIgnore
	private Set<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "permissions")
	@JsonIgnore
	private Set<Account> accounts;

	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	
	
}
