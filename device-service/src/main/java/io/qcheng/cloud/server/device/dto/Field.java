package io.qcheng.cloud.server.device.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.qcheng.cloud.server.device.utils.PatternUtils;

@Entity
@Table(name = "tbl_field", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "message_type_id" }) })
public class Field {

	public Field() {
		super();
	}

	public Field(String name, DataType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public Field(String name, Integer position, DataType type, Integer length) {
		super();
		this.name = name;
		this.position = position;
		this.type = type;
		this.length = length;
	}

	@Id
	@Pattern(regexp = PatternUtils.GENERAL_STRING, message = "error.field.pattern")
	@NotEmpty(message = "error.field.empty")
	@Length(max = 64, message = "error.field.length")
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "position", nullable = true)
	private Integer position = 0;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private DataType type;

	@Column(name = "length", nullable = true)
	private Integer length;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore //https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
	private MessageType messageType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

}
