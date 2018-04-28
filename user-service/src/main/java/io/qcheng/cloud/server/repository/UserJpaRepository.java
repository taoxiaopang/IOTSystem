package io.qcheng.cloud.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.qcheng.cloud.server.dto.UserDTO;

public interface UserJpaRepository extends JpaRepository<UserDTO, Long>{

    UserDTO findByEmail(String email);
    UserDTO findById(Long id);
    void deleteById(Long id);

}
