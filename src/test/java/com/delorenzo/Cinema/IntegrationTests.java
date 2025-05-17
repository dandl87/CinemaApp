package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.DateHolder;
import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.repository.RoomRepository;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.service.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertTrue;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
class IntegrationTests implements PostgreSQLContainerInitializer{

	private static final Logger logger = LoggerFactory.getLogger(IntegrationTests.class);

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	ScreeningRepository screeningRepository;
	@Autowired
	StorageProperties storageProperties;
	@Autowired
	ScreeningService screeningService;
	@Autowired
	DateHolder currentDay;


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
		assertTrue(postgresContainer.isRunning());
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
//
//
//	@DisplayName("Test di integrazione: inizializzazione")
//	@Test
//	void initBatchTest() throws Exception {
//
//		List<Scheduler> schedulers = schedulerInit();
//		Optional<Scheduler> imaxScheduler = Utils.getSchedulerByName(schedulers,"imax");
//		Optional<Scheduler> regularScheduler = Utils.getSchedulerByName(schedulers,"regular");
//
//		MovieService movieService = new MovieService(movieRepository);
//		StorageService storageService = new FileSystemStorageService(storageProperties);
//		storageService.deleteAll();
//		storageService.init();
//		MoviesFromExcelService moviesFromExcelService = new MoviesFromExcelService(storageProperties);
//
//		MainService mainService = new MainService(
//				movieRepository,
//				imaxScheduler.get(),
//				regularScheduler.get(),
//				movieService,
//				screeningService,
//				currentDay);
//
//		List<Screening> screenings = screeningRepository.findAll();
//		Assertions.assertEquals(10, screenings.size());
//	}
//
//
//	private List<Scheduler> schedulerInit() throws Exception {
//		List<Scheduler> schedulers = new ArrayList<>();
//		Scheduler imaxScheduler = new Scheduler("imax",2);
//		Scheduler regularScheduler = new Scheduler("regular",10);
//		SchedulerDataLoader schedulerDataLoader = new SchedulerDataLoader(imaxScheduler,regularScheduler,roomRepository);
//		schedulerDataLoader.run(new DefaultApplicationArguments());
//		schedulers.add(imaxScheduler);
//		schedulers.add(regularScheduler);
//		return schedulers;
//
//	}





}
