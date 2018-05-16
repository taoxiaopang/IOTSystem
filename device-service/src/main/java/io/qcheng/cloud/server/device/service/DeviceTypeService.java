package io.qcheng.cloud.server.device.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.qcheng.cloud.server.device.dto.DeviceType;
import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.repository.DeviceTypeJpaRepository;
import io.qcheng.cloud.server.device.repository.MessageTypeJpaRepository;

@Service
public class DeviceTypeService {

	private DeviceTypeJpaRepository deviceTypeJpaRepository;

	private MessageTypeJpaRepository messageTypeJpaRepository;

	@Autowired
	public void setDeviceTypeJpsRepository(DeviceTypeJpaRepository deviceTypeJpaRepository) {
		this.deviceTypeJpaRepository = deviceTypeJpaRepository;
	}

	@Autowired
	public void setMessageTypeJpaRepository(MessageTypeJpaRepository messageTypeJpaRepository) {
		this.messageTypeJpaRepository = messageTypeJpaRepository;
	}

	public List<DeviceType> getAllDeviceTypes(Long userId) {
		return deviceTypeJpaRepository.findByUserId(userId);
	}

	public DeviceType getDeviceType(String id) {
		return deviceTypeJpaRepository.findById(id);
	}

	public void createDeviceType(DeviceType deviceType) {
		deviceTypeJpaRepository.save(deviceType);

	}

	public void addNewMessageType(DeviceType deviceType, MessageType messageType) {
		deviceType.addMessageType(messageType);
		deviceTypeJpaRepository.save(deviceType);

	}
	
	public void removeMessageType(DeviceType deviceType, MessageType messageType) {
		deviceType.removeMessageType(messageType);
		deviceTypeJpaRepository.save(deviceType);
		
	}

	public List<MessageType> getAllMessageTypes(DeviceType deviceType) {
		return messageTypeJpaRepository.findByDeviceTypes(Arrays.asList(deviceType));

	}

	public MessageType getMessageType(String id) {
		return messageTypeJpaRepository.findById(id);

	}



}
