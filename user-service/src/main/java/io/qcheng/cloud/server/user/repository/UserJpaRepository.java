package io.qcheng.cloud.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.qcheng.cloud.server.user.dto.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
    User findById(Long id);
    void deleteById(Long id);

}
