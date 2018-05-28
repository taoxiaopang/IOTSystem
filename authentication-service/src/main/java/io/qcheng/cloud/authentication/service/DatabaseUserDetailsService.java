package io.qcheng.cloud.authentication.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.qcheng.cloud.authentication.dto.User;
import io.qcheng.cloud.authentication.repository.UserJpaRepository;


/*
 * In Spring Security, UserDetailsService is used to return user information 
 * from the back end such as a database that gets compared with a submitted 
 * user’s credentials during the authentication process.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {
	private static final Logger logger = LogManager.getLogger(DatabaseUserDetailsService.class);
	private UserJpaRepository userJpaRepository;

	@Autowired
	public DatabaseUserDetailsService(UserJpaRepository userJpaRepository) {
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
	
	public User getUserByUsername(String username) {
		return userJpaRepository.findByUsername(username);
	}

	public void createUser(User user) {
		userJpaRepository.save(user);
	}

	public void deleteUserById(Long id) {
		userJpaRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userJpaRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(
					"User not found with username: " + username);
		}

		boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), 
				user.getPassword(), enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked,
				getAuthorities(user));

	}

	private Set<GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString())));

		return authorities;
	}

}
