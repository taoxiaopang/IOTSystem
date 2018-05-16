package io.qcheng.cloud.server.device.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.repository.MessageTypeJpaRepository;

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

	public List<MessageType> getAllMessageTypes(Long userId) {
		return messageTypeJpaRepository.findByUserId(userId);
	}

	public MessageType getMessageTypeById(String id) {
		return messageTypeJpaRepository.findById(id);
	}
	
	public MessageType getMessageTypeByName(String name) {
		return messageTypeJpaRepository.findByName(name);
	}

	public void deleteMessageType(String id) {
		messageTypeJpaRepository.deleteById(id);
	}

}
