package io.qcheng.cloud.server.device;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.qcheng.cloud.server.device.dto.DataType;
import io.qcheng.cloud.server.device.dto.DeviceType;
import io.qcheng.cloud.server.device.dto.Field;
import io.qcheng.cloud.server.device.dto.MessageDirection;
import io.qcheng.cloud.server.device.dto.MessageType;
import io.qcheng.cloud.server.device.repository.DeviceTypeJpaRepository;
import io.qcheng.cloud.server.device.repository.MessageTypeJpaRepository;
import io.qcheng.cloud.server.device.utils.DeviceServiceUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DeviceTypeServiceIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private DeviceTypeJpaRepository deviceTypeJpaRepository;

	@Autowired
	private MessageTypeJpaRepository messageTypeJpaRepository;

	private Long userId = 1L;

	private Field field1 = new Field("timestamp", DataType.DATE);
	private Field field2 = new Field("voltage", 1, DataType.DOUBLE, 64);
	private Field field3 = new Field("status", DataType.BOOLEAN);
	private Field field4 = new Field("ampere", 2, DataType.DOUBLE, 64);

	private static final String MSGTYPE1_NAME = "msgType1";
	private static final String MSGTYPE2_NAME = "msgType2";
	private static final String MSGTYPE3_NAME = "msgType3";
	private static final String MSGTYPE4_NAME = "msgType4";
	private static final String DEVTYPE1_NAME = "devType1";
	private static final String DEVTYPE2_NAME = "devType2";
	private String msgType1Id = DeviceServiceUtils.generateIdentity();
	private String msgType2Id = DeviceServiceUtils.generateIdentity();
	private String msgType3Id = DeviceServiceUtils.generateIdentity();
	private String devType1Id = DeviceServiceUtils.generateIdentity();
	private String devType2Id = DeviceServiceUtils.generateIdentity();

	private MessageType msgType1 = new MessageType(msgType1Id, MSGTYPE1_NAME, MessageDirection.fromDevice,
			new HashSet<>(Arrays.asList(field1, field2, field3)));

	private MessageType msgType2 = new MessageType(msgType2Id, MSGTYPE2_NAME, MessageDirection.bidirection,
			new HashSet<>(Arrays.asList(field1, field4)));

	private MessageType msgType3 = new MessageType(msgType3Id, MSGTYPE3_NAME, MessageDirection.bidirection,
			new HashSet<>(Arrays.asList(field2, field3)));
	
	private MessageType msgType4 = new MessageType(MSGTYPE4_NAME, MessageDirection.toDevice,
			new HashSet<>(Arrays.asList(field2, field3)));

	private DeviceType devType1;
	private DeviceType devType2;

	@Before
	public void before() {
		// given
		deviceTypeJpaRepository.deleteAll();
		messageTypeJpaRepository.deleteAll();

		msgType1.setUserId(userId);
		msgType2.setUserId(userId);
		msgType3.setUserId(userId);
		devType1 = new DeviceType(devType1Id, DEVTYPE1_NAME, new HashSet<>(Arrays.asList(msgType1, msgType2)));
		devType1.setUserId(userId);

		devType2 = new DeviceType(devType2Id, DEVTYPE2_NAME);
		devType2.setUserId(userId);

		// messageTypeJpaRepository.save(msgType1);
		messageTypeJpaRepository.save(msgType3);

		deviceTypeJpaRepository.save(devType1);
		deviceTypeJpaRepository.save(devType2);
	}

	@Test
	public void getAllDeviceTypesShouldReturnAll() {
		// when
		ResponseEntity<DeviceType[]> entity = restTemplate.getForEntity("/api/v1.0/deviceType", DeviceType[].class);

		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		DeviceType[] body = entity.getBody();
		assertThat(body.length).isEqualTo(2);
		assertThat(body[0]).isInstanceOf(DeviceType.class);
		assertThat(body[1]).isInstanceOf(DeviceType.class);
		assertThat(body).extracting("id").containsExactlyInAnyOrder(devType1Id, devType2Id);
		assertThat(body).extracting("name").containsExactlyInAnyOrder(DEVTYPE1_NAME, DEVTYPE2_NAME);
	}

	@Test
	public void getDeviceTypeByIdShouldReturnTheDeviceType() {
		// when
		ResponseEntity<DeviceType> entity = restTemplate.getForEntity("/api/v1.0/deviceType/" + devType1Id,
				DeviceType.class);

		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		DeviceType body = entity.getBody();
		assertThat(body.getId()).isEqualTo(devType1Id);
		assertThat(body.getName()).isEqualTo(DEVTYPE1_NAME);
		assertThat(body.getMessageTypes().size()).isEqualTo(2);
	}

	@Test
	public void geAllMessageTypeOfDeviceTypeShouldReturnAllItsMessageTypes() {
		// when
		ResponseEntity<MessageType[]> entity = restTemplate
				.getForEntity("/api/v1.0/deviceType/" + devType1Id + "/messageTypes", MessageType[].class);

		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		MessageType[] body = entity.getBody();
		assertThat(body.length).isEqualTo(2);
		assertThat(body[0]).isInstanceOf(MessageType.class);
		assertThat(body[1]).isInstanceOf(MessageType.class);
		assertThat(body).extracting("id").containsExactlyInAnyOrder(msgType1Id, msgType2Id);
		assertThat(body).extracting("name").containsExactlyInAnyOrder(MSGTYPE1_NAME, MSGTYPE2_NAME);
		assertThat(body).extracting("direction").containsExactlyInAnyOrder(MessageDirection.fromDevice,
				MessageDirection.bidirection);
	}

	@Test
	public void getMessageTypeOfDeviceTypeShouldReturnTheMessageType() {
		// when
		ResponseEntity<MessageType> entity = restTemplate
				.getForEntity("/api/v1.0/deviceType/" + devType1Id + "/messageTypes/" + msgType1Id, MessageType.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		MessageType body = entity.getBody();
		assertThat(body.getId()).isEqualTo(msgType1Id);
		assertThat(body.getName()).isEqualTo(MSGTYPE1_NAME);
	}

	@Test
	public void getMessageTypeNotBelongToDeviceTypeShouldReturnNotFound() {
		// when
		ResponseEntity<MessageType> entity = restTemplate
				.getForEntity("/api/v1.0/deviceType/" + devType1Id + "/messageTypes/" + msgType3Id, MessageType.class);

		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteMessageTypeShouldDetachFromItsDeviceType() {
		//when
		ResponseEntity<MessageType> entity = restTemplate
				.exchange("/api/v1.0/deviceType/" + devType1Id + "/messageTypes/" + msgType1Id, HttpMethod.DELETE, null, MessageType.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(messageTypeJpaRepository.findById(msgType1Id)).isNotNull();
		
		//This will cause org.hibernate.LazyInitializationException
		//Set<DeviceType> deviceTypes = new HashSet<>(messageTypeJpaRepository.findById(msgType1Id).getDeviceTypes());
		//assertThat(devType1Id).isNotIn(deviceTypes.stream().map(deviceType -> deviceType.getId()).collect(Collectors.toList()));
		
		entity = restTemplate
				.getForEntity("/api/v1.0/deviceType/" + devType1Id + "/messageTypes/" + msgType1Id, MessageType.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	

	@Test
	public void getAllMessageTypesShouldReturnAll() {
		// when
		ResponseEntity<MessageType[]> entity = restTemplate.getForEntity("/api/v1.0/messageType", MessageType[].class);

		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		MessageType[] body = entity.getBody();
		assertThat(body.length).isEqualTo(3);
		assertThat(body[0]).isInstanceOf(MessageType.class);
		assertThat(body[1]).isInstanceOf(MessageType.class);
		assertThat(body).extracting("id").containsExactlyInAnyOrder(msgType1Id, msgType2Id, msgType3Id);
		assertThat(body).extracting("name").containsExactlyInAnyOrder(MSGTYPE1_NAME, MSGTYPE2_NAME, MSGTYPE3_NAME);
		assertThat(body).extracting("direction").containsExactlyInAnyOrder(MessageDirection.fromDevice,
				MessageDirection.bidirection, MessageDirection.bidirection);
	}
	
	@Test
	public void getMessageTypeShouldReturnTheMessageType() {
		//when
		ResponseEntity<MessageType> entity = restTemplate.getForEntity("/api/v1.0/messageType/" + msgType1Id, MessageType.class);
		
		//then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		MessageType messageType = entity.getBody();
		assertThat(messageType).isNotNull();
		assertThat(messageType.getId()).isEqualTo(msgType1Id);
	}
	
	@Test
	public void createMessageTypeShouldReturnCreated() {
		// when
		ResponseEntity<MessageType> entity = restTemplate.postForEntity("/api/v1.0/messageType", msgType4, MessageType.class);
		
		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		MessageType messageType = messageTypeJpaRepository.findByName(MSGTYPE4_NAME);
		assertThat(messageType).isNotNull();
		assertThat(messageType.getDirection()).isEqualTo(MessageDirection.toDevice);
		String msgType4Id = messageType.getId();
		
		entity = restTemplate.getForEntity("/api/v1.0/messageType/" + msgType4Id, MessageType.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isNotNull();
		assertThat(entity.getBody().getFields()).extracting("name").containsExactlyInAnyOrder("voltage", "status");
	}
	
	@Test
	public void createMessageTypeAlreadyExistShouldReturnConflict() {
		// when
		ResponseEntity<MessageType> entity = restTemplate.postForEntity("/api/v1.0/messageType", msgType3, MessageType.class);
		
		// then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);	
	}
}
