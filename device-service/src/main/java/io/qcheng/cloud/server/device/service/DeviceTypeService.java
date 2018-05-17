package io.qcheng.cloud.server.device.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.qcheng.cloud.server.device.dto.DeviceType;
import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.repository.DeviceTypeJpaRepository;
import io.qcheng.cloud.server.device.repository.MessageTypeJpaRepository;

@DefaultProperties(commandProperties = {
		@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000") })
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

	@HystrixCommand(fallbackMethod = "buildFallbackDeviceTypeList")
	public List<DeviceType> getAllDeviceTypes(Long userId) {
		return deviceTypeJpaRepository.findByUserId(userId);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackDeviceType")
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

	@HystrixCommand(fallbackMethod = "buildFallbackMessageTypeList")
	public List<MessageType> getAllMessageTypes(DeviceType deviceType) {
		return messageTypeJpaRepository.findByDeviceTypes(Arrays.asList(deviceType));

	}

	public MessageType getMessageType(String id) {
		return messageTypeJpaRepository.findById(id);

	}

	private List<DeviceType> buildFallbackDeviceTypeList(Long userId) {
		List<DeviceType> list = new ArrayList<>();
		DeviceType deviceType = new DeviceType("0000-0000-0000-00000", "Sorry no device type currently available");
		list.add(deviceType);

		return list;
	}

	private DeviceType buildFallbackDeviceType(String id) {
		DeviceType deviceType = new DeviceType("0000-0000-0000-00000", "Sorry no device type currently available");

		return deviceType;
	}

	private List<MessageType> buildFallbackMessageTypeList(DeviceType deviceType) {
		List<MessageType> list = new ArrayList<>();
		MessageType messageType = new MessageType("0000-0000-0000-00000", "Sorry no message type currently available");
		list.add(messageType);

		return list;
	}
}
