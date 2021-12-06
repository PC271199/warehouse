package com.example.Warehouse.entities.accountService;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.domain.Pageable;

//import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;

import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "account", schema = "public",uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class Account {
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "email is mandatory")
    @Email
    private String email;
	
	private String password;
	
	private Date create_At;
	
    private Date update_At;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
	
    // refering side has mappedBy att
 	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
 	private UserInfor userinfor;
 	
 	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
 	private VerificationToken verificationToken;
 	
 	//owning side has joincolumn
	// cascade ALL appear in refering side
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@Valid
	private Role role;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "account_permission",
	joinColumns = @JoinColumn(name = "account_id"),
	inverseJoinColumns = @JoinColumn(name = "permission_id"))
	@NotEmpty(message = "permissions cannot be empty.")
	private Set<Permission> permissions;
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//	@JoinTable(name = "account_bukken",
//	joinColumns = @JoinColumn(name = "account_id"),
//	inverseJoinColumns = @JoinColumn(name = "bukken_id"))
//	private Set<Bukken> bukkens;
	
	@ManyToMany(fetch = FetchType.EAGER,mappedBy = "accounts")
	private Set<Bukken> bukkens;
	
	
	private String codeId;
	@Column(nullable = true)
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "account",cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<ScheduleBukkenUser> scheduleWarehouseUserList;
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	@PrePersist
    protected void onCreate(){
        this.create_At =new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.update_At =new Date();
    }

	public Account() {
		this.enabled=false;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(UserInfor userinfor) {
		this.userinfor = userinfor;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
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
	public UserInfor getUserinfor() {
		return userinfor;
	}
	public void setUserinfor(UserInfor userinfor) {
		this.userinfor = userinfor;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AuthProvider getProvider() {
		return provider;
	}
	public void setProvider(AuthProvider provider) {
		this.provider = provider;
	}
	public VerificationToken getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}
	public Set<Bukken> getBukkens() {
		return bukkens;
	}
	public Page<Bukken> getBukkensByPage(Pageable page) {
		return (Page<Bukken>) bukkens;
	}
	public void setBukkens(Set<Bukken> bukkens) {
		this.bukkens = bukkens;
	}
	
}