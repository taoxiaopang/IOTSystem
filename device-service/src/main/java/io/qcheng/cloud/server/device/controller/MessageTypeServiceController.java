package io.qcheng.cloud.server.device.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.exception.MessageTypeErrorType;
import io.qcheng.cloud.server.device.service.MessageTypeService;
import io.qcheng.cloud.server.device.utils.DeviceServiceUtils;

@RestController
@RequestMapping("/api/v1.0/messageType")
public class MessageTypeServiceController {

	private static final Logger logger = LogManager.getLogger(MessageTypeServiceController.class);
	private MessageTypeService messageTypeService;
	
	@Autowired
	public void setMessageTypeService(MessageTypeService messageTypeService) {
		this.messageTypeService = messageTypeService;
	}
	
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<MessageType> getAllMessageTypes() {
		//TODO: 
		Long userId = 1L;
		
		return messageTypeService.getAllMessageTypes(userId);
	}
	
	@GetMapping(value = "/{messageType_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageType> getMessageType(@PathVariable("messageType_id") String messageTypeId) {

		MessageType messageType = messageTypeService.getMessageTypeById(messageTypeId);
		
		if(messageType == null) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("Unable to get. MessageType with id " + messageTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(messageType, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageType> createMessageType(@Valid @RequestBody final MessageType messageType) {
		if(messageTypeService.getMessageTypeByName(messageType.getName()) != null) {
			return new ResponseEntity<>(
                    new MessageTypeErrorType(
                            "Unable to create new message type. A message type with name " + messageType.getName() + " already exist."),
                    HttpStatus.CONFLICT);
		}

		messageType.setId(DeviceServiceUtils.generateIdentity());
		//TODO:
		messageType.setUserId(1L);

		messageTypeService.createMessageType(messageType);

		return new ResponseEntity<>(messageType, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{messageType_id}")
	public ResponseEntity<MessageType> deleteMessageType(@PathVariable("messageType_id") final String messageTypeId) {

		MessageType messageType = messageTypeService.getMessageTypeById(messageTypeId);
		if(!messageType.getDeviceTypes().isEmpty()) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("You try to delete a message type that has been assigned. "
							+ "Assignments should be removed before deleting a message type."),
					HttpStatus.BAD_REQUEST);
		}

		messageTypeService.deleteMessageType(messageTypeId);
		
		return new ResponseEntity<>(messageType, HttpStatus.CREATED);
	}
}
