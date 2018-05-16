package io.qcheng.cloud.server.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.qcheng.cloud.server.user.dto.User;
import io.qcheng.cloud.server.user.repository.UserJpaRepository;

@Service
public class AdminService {

	private UserJpaRepository userJpaRepository;

	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	public List<User> getAllUsers() {
		return userJpaRepository.findAll();
	}

	public User getUserById(Long id) {
		return userJpaRepository.findById(id);
	}

	public User getUserByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}

	public void createUser(User user) {
		userJpaRepository.saveAndFlush(user);

	}

	public void deleteUserById(Long id) {
		userJpaRepository.deleteById(id);
	}

}
