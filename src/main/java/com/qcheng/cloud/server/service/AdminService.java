package com.qcheng.cloud.server.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcheng.cloud.server.dto.UserDTO;
import com.qcheng.cloud.server.repository.UserJpaRepository;

@Service
public class AdminService {

    private UserJpaRepository userJpaRepository;

    @Autowired
    public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userJpaRepository.findAll();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userJpaRepository.findById(id);
    }

    public UserDTO getUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    public void createUser(@Valid UserDTO user) {
        userJpaRepository.saveAndFlush(user);

    }

    public void deleteUserById(Long id) {
        userJpaRepository.deleteById(id);
    }

}
