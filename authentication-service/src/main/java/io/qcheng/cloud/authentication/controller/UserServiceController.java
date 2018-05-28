package io.qcheng.cloud.authentication.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.qcheng.cloud.authentication.dto.User;
import io.qcheng.cloud.authentication.exception.UserErrorType;
import io.qcheng.cloud.authentication.service.DatabaseUserDetailsService;

@RestController
@RequestMapping("/v1.0/user")
public class UserServiceController {
	private static final Logger logger = LogManager.getLogger(UserServiceController.class);
	private DatabaseUserDetailsService userDetailsService;

	@Autowired
	public void setUserDetailsService(DatabaseUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(OAuth2Authentication authUser) {
		String username = authUser.getUserAuthentication().getPrincipal().toString();

		User user = userDetailsService.getUserByUsername(username);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> createNewUser(@Valid @RequestBody final User user) {
		if (userDetailsService.getUserByEmail(user.getEmail()) != null) {
			logger.info("Unable to create. A User with email {} already exist", user.getEmail());

			return new ResponseEntity<>(
					new UserErrorType(
							"Unable to create new user. A User with email " + user.getEmail() + " already exist."),
					HttpStatus.CONFLICT);
		} else if (userDetailsService.getUserByUsername(user.getUsername()) != null) {
			logger.info("Unable to create. A User with username {} already exist", user.getUsername());

			return new ResponseEntity<>(new UserErrorType(
					"Unable to create new user. A User with username " + user.getUsername() + " already exist."),
					HttpStatus.CONFLICT);
		}
		userDetailsService.createUser(user);
		return new ResponseEntity<>(userDetailsService.getUserByEmail(user.getEmail()), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") final Long id) {
		User user = userDetailsService.getUserById(id);
		if (user == null) {
			logger.info("Unable to get. User with id {} not found", id);

			return new ResponseEntity<>(new UserErrorType("Unable to get. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") final Long id) {
		if (userDetailsService.getUserById(id) == null) {
			logger.info("Unable to delete. User with id {} not found", id);

			return new ResponseEntity<>(new UserErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		userDetailsService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
