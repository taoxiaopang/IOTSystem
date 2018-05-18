package io.qcheng.cloud.server.device.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

import io.qcheng.cloud.server.device.dto.DeviceType;
import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.exception.DeviceTypeErrorType;
import io.qcheng.cloud.server.device.exception.MessageTypeErrorType;
import io.qcheng.cloud.server.device.service.DeviceTypeService;
import io.qcheng.cloud.server.device.utils.DeviceServiceUtils;

@RestController
@RequestMapping("/v1.0/deviceType")
public class DeviceTypeServiceController {
	private static final Logger logger = LogManager.getLogger(DeviceTypeServiceController.class);
	private DeviceTypeService deviceTypeService;

	@Autowired
	public void setDeviceTypeService(DeviceTypeService deviceTypeService) {
		this.deviceTypeService = deviceTypeService;
	}

	@GetMapping("")
	List<DeviceType> getAllDeviceTypes() {
		
		Long userId = 1L; //TODO
		return deviceTypeService.getAllDeviceTypes(userId);
	}

	@GetMapping("/{deviceType_id}")
	@ResponseStatus(HttpStatus.OK)
	DeviceType getDeviceType(@PathVariable("deviceType_id") final String identity) {
		return deviceTypeService.getDeviceType(identity);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DeviceType> createDeviceType(@Valid @RequestBody final DeviceType deviceType) {
		
		Long userId = 1L; //TODO
		deviceType.setUserId(userId);
		deviceType.setId(DeviceServiceUtils.generateIdentity());
		for(MessageType messageType : deviceType.getMessageTypes()) {
			messageType.setUserId(userId);
			messageType.setId(DeviceServiceUtils.generateIdentity());
			
		}
		deviceTypeService.createDeviceType(deviceType);

		return new ResponseEntity<>(deviceType, HttpStatus.CREATED);
	}

	@PostMapping(value = "/{deviceType_id}/messageTypes", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeviceType> addNewMessageType(@PathVariable("deviceType_id") final String deviceTypeId,
			                                            @Valid @RequestBody final MessageType messageType) {
		DeviceType deviceType = deviceTypeService.getDeviceType(deviceTypeId);
		if (deviceType == null) {
			return new ResponseEntity<>(
					new DeviceTypeErrorType("Unable to create. DeviceType with id " + deviceTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		Long userId = 1L; //TODO
		messageType.setUserId(userId);
		messageType.setId(DeviceServiceUtils.generateIdentity());
		deviceTypeService.addNewMessageType(deviceType, messageType);

		return new ResponseEntity<>(deviceType, HttpStatus.CREATED);
	}

	@GetMapping(value = "/{deviceType_id}/messageTypes")
	public ResponseEntity<List<MessageType>> getAllMessageTypes(@PathVariable("deviceType_id") final String deviceTypeId) {
		DeviceType deviceType = deviceTypeService.getDeviceType(deviceTypeId);
		if (deviceType == null) {
			return new ResponseEntity<>(
					Arrays.asList(new MessageTypeErrorType("Unable to get. DeviceType with id " + deviceTypeId + " not found.")),
					HttpStatus.NOT_FOUND);
		}
		
		List<MessageType> messageTypes = deviceTypeService.getAllMessageTypes(deviceType);
		return new ResponseEntity<>(messageTypes, HttpStatus.OK);
	}

	@GetMapping(value = "/{deviceType_id}/messageTypes/{messageType_id}")
	public ResponseEntity<MessageType> getMessageType(@PathVariable("deviceType_id") final String deviceTypeId,
			                                          @PathVariable("messageType_id") final String messageTypeId) {
		DeviceType deviceType = deviceTypeService.getDeviceType(deviceTypeId);
		if (deviceType == null) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("Unable to get. DeviceType with id " + deviceTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		List<MessageType> messageTypes = deviceTypeService.getAllMessageTypes(deviceType);
		List<MessageType> result = 
				messageTypes.stream().filter(messageType -> messageType.getId().equals(messageTypeId)).collect(Collectors.toList());
		if(result.isEmpty()) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("Unable to get. MessageType with id " + messageTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(result.get(0), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{deviceType_id}/messageTypes/{messageType_id}")
	public ResponseEntity<MessageType> removeMessageType(@PathVariable("deviceType_id") final String deviceTypeId,
			                                             @PathVariable("messageType_id") final String messageTypeId) {
		DeviceType deviceType = deviceTypeService.getDeviceType(deviceTypeId);
		if (deviceType == null) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("Unable to delete. DeviceType with id " + deviceTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		MessageType messageType = deviceTypeService.getMessageType(messageTypeId);
		if (messageType == null) {
			return new ResponseEntity<>(
					new MessageTypeErrorType("Unable to delete. MessageType with id " + messageTypeId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		deviceTypeService.removeMessageType(deviceType, messageType);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
