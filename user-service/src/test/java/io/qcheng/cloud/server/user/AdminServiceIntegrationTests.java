package io.qcheng.cloud.server.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.qcheng.cloud.server.user.dto.User;
import io.qcheng.cloud.server.user.repository.UserJpaRepository;

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
	
	private final User user1 = new User("xiaopang1", "Tao1", "123456", "txp1@123.com");
	private final User user2 = new User("xiaopang2", "Tao2", "654321", "txp2@abc.com");
	
	@Before
	public void before() {
		//given
		userJpaRepository.deleteAll();
		userJpaRepository.save(Arrays.asList(user1, user2));
	}
	
	@After
	public void after() {
		
	}

	@Test
	public void getUsersShouldReturnAllUser() {
		//when
		ResponseEntity<User[]> entity = restTemplate.getForEntity("/api/v1.0/admin/user", User[].class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().length).isEqualTo(2);
		assertThat(entity.getBody()[0]).isInstanceOf(User.class);
		assertThat(entity.getBody()).extracting("email").containsExactly(user1.getEmail(), user2.getEmail());
	}
	
	@Test
	public void getUserByEmailShouldReturnTheUser() {
		//given
		//given(adminService.getUserById(1L)).willReturn(Optional.of(new UserDTO(1L, "xiaopang", "Tao", "123456", "txp@123.com")));
		
		//when
		ResponseEntity<User> entity = restTemplate.getForEntity("/api/v1.0/admin/user/1", User.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isInstanceOf(User.class);	
		assertThat(entity.getBody().getEmail()).isEqualTo(user1.getEmail());
	}
	
	@Test
	public void createUserShouldReturnCreated() {
		//when
		User user3 = new User("xiaopang3", "Tao3", "123456", "txp3@123.com");
		ResponseEntity<User> entity = restTemplate.postForEntity("/api/v1.0/admin/user", user3, User.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(userJpaRepository.findAll().size()).isEqualTo(3);
		assertThat(entity.getBody().getEmail()).isEqualTo(user3.getEmail());
		assertThat(entity.getBody().getId()).isEqualTo(3L);
	}
	
	@Test
	public void createUserWithDuplicateEmailShouldReturnConflict() {
		//when
		User user3 = new User("xiaopang3", "Tao3", "123456", user1.getEmail());
		ResponseEntity<User> entity = restTemplate.postForEntity("/api/v1.0/admin/user", user3, User.class);
		
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
		assertThat(userJpaRepository.findById(2L).getEmail()).isEqualTo(user2.getEmail());
	}

}
