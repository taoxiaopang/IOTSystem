package io.qcheng.cloud.server.device.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.repository.MessageTypeJpaRepository;

@DefaultProperties(commandProperties = {
		@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000") })
@Service
public class MessageTypeService {

	private MessageTypeJpaRepository messageTypeJpaRepository;

	@Autowired
	public void setMessageTypeJpaRepository(MessageTypeJpaRepository messageTypeJpaRepository) {
		this.messageTypeJpaRepository = messageTypeJpaRepository;
	}

	public void createMessageType(MessageType messageType) {
		messageTypeJpaRepository.save(messageType);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackMessageTypeList")
	public List<MessageType> getAllMessageTypes(Long userId) {
		return messageTypeJpaRepository.findByUserId(userId);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackMessageType")
	public MessageType getMessageTypeById(String id) {
		return messageTypeJpaRepository.findById(id);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackMessageType")
	public MessageType getMessageTypeByName(String name) {
		return messageTypeJpaRepository.findByName(name);
	}

	public void deleteMessageType(String id) {
		messageTypeJpaRepository.deleteById(id);
	}

	private List<MessageType> buildFallbackMessageTypeList(Long userId) {
		List<MessageType> list = new ArrayList<>();
		MessageType messageType = new MessageType("0000-0000-0000-00000", "Sorry no message type currently available");
		list.add(messageType);

		return list;
	}

	private MessageType buildFallbackMessageType(String e) {
		MessageType messageType = new MessageType("0000-0000-0000-00000", "Sorry no message type currently available");

		return messageType;
	}

}
