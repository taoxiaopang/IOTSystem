package io.qcheng.cloud.server.device.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.qcheng.cloud.server.device.dto.DeviceType;

public interface DeviceTypeJpaRepository extends JpaRepository<DeviceType, Long> {
	
	public DeviceType findById(String id);
	
	public List<DeviceType> findByUserId(Long userId);

}
