package io.qcheng.cloud.server.device.dto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.qcheng.cloud.server.device.utils.PatternUtils;

@Entity
@Table(
		name = "tbl_deviceType",
		uniqueConstraints = {
		        @UniqueConstraint(columnNames = { "name", "user_id"})
		    })
public class DeviceType {

	public DeviceType() {
		super();
	}
	
	public DeviceType(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DeviceType(String id, String name, Set<MessageType> messageTypes) {
		super();
		this.id = id;
		this.name = name;
		this.messageTypes = messageTypes;
	}

	@Id
    @Column(name = "id", length = 20, nullable = false)
	private String id;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;

    @Pattern(regexp = PatternUtils.GENERAL_STRING, message = "error.deviceTypeName.pattern")
    @NotEmpty(message = "error.deviceTypeName.empty")
    @Length(max = 64, message = "error.deviceTypeName.length")
    @Column(name = "name")
	private String name;

	@CreationTimestamp
	@Column(name = "created")
	private Date created;
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "deviceType_messageType", 
	           joinColumns = @JoinColumn(name = "devic_type_id", referencedColumnName = "id", nullable = false, updatable = false),
	           inverseJoinColumns = @JoinColumn(name = "message_type_id", referencedColumnName = "id", nullable = false, updatable = false))
	@JsonIgnoreProperties("deviceTypes")
	private Set<MessageType> messageTypes  = new HashSet<>();
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<MessageType> getMessageTypes() {
		return messageTypes;
	}

	public void setMessageTypes(Set<MessageType> messageTypes) {
		this.messageTypes = messageTypes;
	}
	
	public void addMessageType(MessageType messageType) {
		messageType.addDeviceType(this);
		this.messageTypes.add(messageType);
	}
	
	public void removeMessageType(MessageType messageType) {
		messageType.removeDeviceType(this);
		this.messageTypes.remove(messageType);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
