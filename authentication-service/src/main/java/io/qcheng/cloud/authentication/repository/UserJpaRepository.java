package io.qcheng.cloud.authentication.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.qcheng.cloud.authentication.dto.User;


public interface UserJpaRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
    User findByUsername(String username);
    User findById(Long id);
    @Transactional
    void deleteById(Long id);

}
