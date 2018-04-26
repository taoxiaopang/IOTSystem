package com.qcheng.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qcheng.cloud.server.dto.UserDTO;

public interface UserJpaRepository extends JpaRepository<UserDTO, Long>{

    UserDTO findByEmail(String email);

}
