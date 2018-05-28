package io.qcheng.cloud.authentication.dto;

import static io.qcheng.cloud.authentication.utils.PatternUtils.GENERAL_STRING;
import static io.qcheng.cloud.authentication.utils.PatternUtils.PASSWORD;
import static io.qcheng.cloud.authentication.utils.PatternUtils.PHONE_NUMBER;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }),
		@UniqueConstraint(columnNames = { "username" }) })
public class User {

	// @JsonIgnore
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = GENERAL_STRING, message = "error.username.pattern")
	@NotEmpty(message = "error.username.empty")
	@Length(max = 128, message = "error.username.length")
	@Column(name = "username")
	private String username;

	@Pattern(regexp = GENERAL_STRING, message = "error.firstName.pattern")
	@NotEmpty(message = "error.firstName.empty")
	@Length(max = 64, message = "error.firstName.length")
	@Column(name = "firstName")
	private String firstName;

	@Pattern(regexp = GENERAL_STRING, message = "error.lastName.pattern")
	@NotEmpty(message = "error.lastName.empty")
	@Length(max = 64, message = "error.lastName.length")
	@Column(name = "lastName")
	private String lastName;

	@Pattern(regexp = PHONE_NUMBER, message = "error.phone.pattern")
	@Length(max = 64, message = "error.phone.length")
	@Column(name = "phone")
	private String phone;

	@Email(message = "error.email.email")
	@NotEmpty(message = "error.email.empty")
	@Length(max = 64, message = "error.email.length")
	@Column(name = "email")
	private String email;

	@Pattern(regexp = PASSWORD, message = "error.password.pattern")
	@NotEmpty(message = "error.password.empty")
	@Length(min = 6, max = 32, message = "error.password.length")
	@Column(name = "password")
	private String password;

	@JsonIgnore
	private boolean enabled;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles;

	public User() {
		super();
	}

	public User(String firstName, String lastName, String phone, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}

	public User(Long id, String firstName, String lastName, String phone, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
