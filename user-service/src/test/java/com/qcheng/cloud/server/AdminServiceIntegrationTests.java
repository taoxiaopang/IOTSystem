package com.qcheng.cloud.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.qcheng.cloud.server.dto.UserDTO;
import com.qcheng.cloud.server.repository.UserJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminServiceIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	//@MockBean
	//private AdminService adminService;
	
	@Autowired
	private UserJpaRepository userJpaRepository;
	
	private final UserDTO user1 = new UserDTO("xiaopang1", "Tao1", "123456", "txp1@123.com");
	private final UserDTO user2 = new UserDTO("xiaopang2", "Tao2", "654321", "txp2@abc.com");
	
	@Before
	public void before() {
		//given
		userJpaRepository.deleteAll();
		userJpaRepository.saveAll(Arrays.asList(user1, user2));
	}
	
	@After
	public void after() {
		
	}

	@Test
	public void getUsersShouldReturnAllUser() {
		//when
		ResponseEntity<List> entity = restTemplate.getForEntity("/api/v2.0/admin/user", List.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().size()).isEqualTo(2);
		assertThat(entity.getBody()).extracting("email").containsExactly(user1.getEmail(), user2.getEmail());
	}
	
	@Test
	public void getUserByEmailShouldReturnTheUser() {
		//given
		//given(adminService.getUserById(1L)).willReturn(Optional.of(new UserDTO(1L, "xiaopang", "Tao", "123456", "txp@123.com")));
		
		//when
		ResponseEntity<UserDTO> entity = restTemplate.getForEntity("/api/v2.0/admin/user/1", UserDTO.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isInstanceOf(UserDTO.class);	
		assertThat(entity.getBody().getEmail()).isEqualTo(user1.getEmail());
	}
	
	@Test
	public void createUserShouldReturnCreated() {
		//when
		UserDTO user3 = new UserDTO("xiaopang3", "Tao3", "123456", "txp3@123.com");
		ResponseEntity<UserDTO> entity = restTemplate.postForEntity("/api/v2.0/admin/user", user3, UserDTO.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(userJpaRepository.findAll().size()).isEqualTo(3);
		assertThat(entity.getBody().getEmail()).isEqualTo(user3.getEmail());
		assertThat(entity.getBody().getId()).isEqualTo(3L);
	}
	
	@Test
	public void createUserWithDuplicateEmailShouldReturnConflict() {
		//when
		UserDTO user3 = new UserDTO("xiaopang3", "Tao3", "123456", user1.getEmail());
		ResponseEntity<UserDTO> entity = restTemplate.postForEntity("/api/v2.0/admin/user", user3, UserDTO.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(userJpaRepository.findAll().size()).isEqualTo(2);
	}
	
	@Test
	public void deleteUser() {
		//when
		restTemplate.delete("/api/v2.0/admin/user/1");
		
		//then
		assertThat(userJpaRepository.findAll().size()).isEqualTo(1);
		assertThat(userJpaRepository.findById(2L).get().getEmail()).isEqualTo(user2.getEmail());
	}

}
