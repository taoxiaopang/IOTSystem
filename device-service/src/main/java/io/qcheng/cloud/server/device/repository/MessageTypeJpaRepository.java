package io.qcheng.cloud.server.device.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.qcheng.cloud.server.device.dto.DeviceType;
import io.qcheng.cloud.server.device.dto.MessageType;

@Repository
public interface MessageTypeJpaRepository extends JpaRepository<MessageType, Long> {

	List<MessageType> findByDeviceTypes(List<DeviceType> deviceTypes);

	MessageType findById(String identity);

	void deleteById(String id);

	List<MessageType> findByUserId(Long userId);

	MessageType findByName(String name);
	
}
