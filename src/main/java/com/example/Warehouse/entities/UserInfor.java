package com.example.Warehouse.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "userinfor", schema = "public",uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class UserInfor {
	@Id 
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "name is mandatory")
	@Length(max = 50, message = "name has max-length = 50")
	private String name;
	@NotBlank(message = "address is mandatory")
	@Length(max = 100, message = "address has max-length = 100")
	private String address;
	@Min(value = 1)
	private int age;
	@NotBlank(message = "email is mandatory")
	@Length(max = 50, message = "email has max-length = 50")
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email not match")
	private String email;
	private Date create_At;
    private Date update_At;
	
	
	
	@PrePersist
    protected void onCreate(){
        this.create_At =new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.update_At =new Date();
    }
	
	
	public Date getCreate_At() {
		return create_At;
	}
	public void setCreate_At(Date create_At) {
		this.create_At = create_At;
	}
	public Date getUpdate_At() {
		return update_At;
	}
	public void setUpdate_At(Date update_At) {
		this.update_At = update_At;
	}


	// owning side has joincolumn
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
	@JsonIgnore
	private Account account;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public UserInfor(int id,
			@NotBlank(message = "name is mandatory") @Length(max = 50, message = "name has max-length = 50") String name,
			@NotBlank(message = "address is mandatory") @Length(max = 100, message = "address has max-length = 100") String address,
			@Min(1) int age,
			@NotBlank(message = "email is mandatory") @Length(max = 50, message = "email has max-length = 50") @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email not match") String email,
			Account account) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
		this.email = email;
		this.account = account;
	}

	public UserInfor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
