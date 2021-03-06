package io.qcheng.cloud.server.user.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import io.qcheng.cloud.server.user.utils.PatternUtils;

@Entity
@Table(name = "tbl_user",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email"})
    })
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = PatternUtils.GENERAL_STRING, message = "error.firstName.pattern")
    @NotEmpty(message = "error.firstName.empty")
    @Length(max = 64, message = "error.firstName.length")
    @Column(name = "firstName")
    private String firstName;

    @Pattern(regexp = PatternUtils.GENERAL_STRING, message = "error.lastName.pattern")
    @NotEmpty(message = "error.lastName.empty")
    @Length(max = 64, message = "error.lastName.length")
    @Column(name = "lastName")
    private String lastName;

    @Pattern(regexp = PatternUtils.PHONE_NUMBER, message = "error.phone.pattern")
    @Length(max = 64, message = "error.phone.length")
    @Column(name = "phone")
    private String phone;

    @Email(message = "error.email.email")
    @NotEmpty(message = "error.email.empty")
    @Length(max = 64, message = "error.email.length")
    @Column(name = "email")
    private String email;
    
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

}
