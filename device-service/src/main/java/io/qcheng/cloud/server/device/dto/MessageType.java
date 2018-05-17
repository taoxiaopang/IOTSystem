package io.qcheng.cloud.server.device.dto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.qcheng.cloud.server.device.utils.PatternUtils;

@Entity
@Table(name = "tbl_messageType", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "user_id" }) })
public class MessageType {

	public MessageType() {
		super();
	}

	public MessageType(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public MessageType(String id, String name, MessageDirection direction, Set<Field> fields) {
		super();
		this.id = id;
		this.name = name;
		this.direction = direction;
		this.fields = fields;
	}

	public MessageType(String name, MessageDirection direction, Set<Field> fields) {
		super();
		this.name = name;
		this.direction = direction;
		this.fields = fields;
	}

	@Id
	@Column(name = "id", length = 20, nullable = false)
	private String id;

	@Pattern(regexp = PatternUtils.GENERAL_STRING, message = "error.messageTypName.pattern")
	@NotEmpty(message = "error.messageTypName.empty")
	@Length(max = 64, message = "error.messageTypName.length")
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull(message = "error.messageDirection.empty")
	@Enumerated(EnumType.STRING)
	@Column(name = "direction")
	private MessageDirection direction;

	@CreationTimestamp
	@Column(name = "created")
	private Date created;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "messageTypes")
	@JsonIgnoreProperties("messageTypes")
	@JsonIgnore
	private Set<DeviceType> deviceTypes = new HashSet<>();

	@Size(min = 1, max = 100)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "messageType")
	private Set<Field> fields = new HashSet<>();

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<DeviceType> getDeviceTypes() {
		return deviceTypes;
	}

	public void setDeviceTypes(Set<DeviceType> deviceTypes) {
		this.deviceTypes = deviceTypes;
	}

	public void addDeviceType(DeviceType deviceType) {
		this.deviceTypes.add(deviceType);
	}

	public void removeDeviceType(DeviceType deviceType) {
		this.deviceTypes.remove(deviceType);
	}

	public Set<Field> getFields() {
		return fields;
	}

	public void setFields(Set<Field> fields) {
		// https://stackoverflow.com/questions/40977239/jpa-onetomany-foreign-key-is-null
		if (fields != null && !fields.isEmpty()) {
			for (Field field : fields) {
				field.setMessageType(this);
			}
		}
		this.fields = fields;
	}

	public MessageDirection getDirection() {
		return direction;
	}

	public void setDirection(MessageDirection direction) {
		this.direction = direction;
	}

}
