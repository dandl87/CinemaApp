package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.SchedulerConfig;
import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.repository.RoomRepository;
import com.delorenzo.Cinema.service.FileSystemStorageService;
import com.delorenzo.Cinema.service.StorageService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;



@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest()
@Import({IntegrationTestContextConfig.class})
class IntegrationTests implements PostgreSQLContainerInitializer{

	private static final Logger logger = LoggerFactory.getLogger(IntegrationTests.class);

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	RoomRepository roomRepository;





	@BeforeAll
    static void setup() {
		postgresContainer.start();
	}


	@AfterAll
	static void afterAll() {
		postgresContainer.stop();
	}


	@Test
	void contextLoads() {
		Assertions.assertTrue(postgresContainer.isRunning());
	}

	@Test
	void applicationStateAtStartUp(){

		List<Room> regularRooms = roomRepository.findAllByImax(false);
		List<Room> imaxRooms = roomRepository.findAllByImax(true);
		List<Movie> movies = movieRepository.findAll();
		Assertions.assertEquals(10, regularRooms.size());
		Assertions.assertEquals(2, imaxRooms.size());
		Assertions.assertEquals(12, movies.size());
	 }


}
