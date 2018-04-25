package com.qcheng.cloud.server.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qcheng.cloud.server.dto.UserDTO;
import com.qcheng.cloud.server.exception.UserErrorType;
import com.qcheng.cloud.server.service.AdminService;

@RestController
@RequestMapping("/api/v2.0/admin")
public class AdminServiceController {
    private static final Logger logger = LogManager.getLogger(AdminServiceController.class);
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @GetMapping("/user")
    public List<UserDTO> getUsers() {
        return adminService.getAllUsers();
        
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") final Long id) {
    	Optional<UserDTO> user = adminService.getUserById(id);
    	if(!user.isPresent()) {
            logger.info("Unable to get. User with id {} not found", id);
            
            return new ResponseEntity<>(
                    new UserErrorType(
                            "Unable to get. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
    
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody final UserDTO user) {
        if(adminService.getUserByEmail(user.getEmail()) != null) {
            logger.info("Unable to create. A User with name {} already exist", user.getEmail());
            
            return new ResponseEntity<>(
                    new UserErrorType(
                            "Unable to create new user. A User with email " + user.getEmail() + " already exist."),
                    HttpStatus.CONFLICT);
        }
        adminService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") final Long id) {
        if(!adminService.getUserById(id).isPresent()) {
            logger.info("Unable to delete. User with id {} not found", id);
            
            return new ResponseEntity<>(
                    new UserErrorType(
                            "Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        adminService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
