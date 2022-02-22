package com.example.Warehouse.entities.accountService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.MapKeyColumn;
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
import com.example.Warehouse.entities.bukkenService.InterestedBukken;
import com.example.Warehouse.entities.bukkenService.RefBukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "account", schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class Account {
	@Id
	@Column(name = "id")
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

	// owning side has joincolumn
	// cascade ALL appear in refering side
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@Valid
	private Role role;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "account_permission", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	@NotEmpty(message = "permissions cannot be empty.")
	private Set<Permission> permissions;

//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//	@JoinTable(name = "account_bukken",
//	joinColumns = @JoinColumn(name = "account_id"),
//	inverseJoinColumns = @JoinColumn(name = "bukken_id"))
//	private Set<Bukken> bukkens;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	private Set<Bukken> bukkens;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	private Set<RefBukken> refbukkens;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	private Set<InterestedBukken> interestedBukkens;

	private String codeId;

	private Boolean codeIdExpired;

	@Column(nullable = true)
	private boolean enabled;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<ScheduleBukkenUser> scheduleBukkenUserList;

	@ElementCollection
	@MapKeyColumn(name = "bukken_id")
	@Column(name = "value")
	@CollectionTable(name = "map_user_bukken", joinColumns = @JoinColumn(name = "id"))
	Map<Integer, Integer> matrix = new HashMap<Integer, Integer>(); // maps from attribute name to value

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

	public Boolean getCodeIdExpired() {
		return codeIdExpired;
	}

	public void setCodeIdExpired(Boolean codeIdExpired) {
		this.codeIdExpired = codeIdExpired;
	}

	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	public Account() {
		this.enabled = false;
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

	public Set<ScheduleBukkenUser> getScheduleBukkenUserList() {
		return scheduleBukkenUserList;
	}

	public void setScheduleBukkenUserList(Set<ScheduleBukkenUser> scheduleBukkenUserList) {
		this.scheduleBukkenUserList = scheduleBukkenUserList;
	}

	public Map<Integer, Integer> getMatrix() {
		return matrix;
	}

	public void setMatrix(Map<Integer, Integer> matrix) {
		this.matrix = matrix;
	}

	public Set<RefBukken> getRefbukkens() {
		return refbukkens;
	}

	public void setRefbukkens(Set<RefBukken> refbukkens) {
		this.refbukkens = refbukkens;
	}

	public Set<InterestedBukken> getInterestedBukkens() {
		return interestedBukkens;
	}

	public void setInterestedBukkens(Set<InterestedBukken> interestedBukkens) {
		this.interestedBukkens = interestedBukkens;
	}

}
