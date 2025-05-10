package com.delorenzo.Cinema;

import com.delorenzo.Cinema.conf.SchedulerDataLoader;
import com.delorenzo.Cinema.entity.Movie;
import com.delorenzo.Cinema.entity.Room;
import com.delorenzo.Cinema.entity.Screening;
import com.delorenzo.Cinema.entity.Week;
import com.delorenzo.Cinema.logic.Scheduler;
import com.delorenzo.Cinema.repository.MovieRepository;
import com.delorenzo.Cinema.repository.RoomRepository;
import com.delorenzo.Cinema.repository.ScreeningRepository;
import com.delorenzo.Cinema.repository.WeekRepository;
import com.delorenzo.Cinema.service.MainService;
import com.delorenzo.Cinema.service.MovieService;
import com.delorenzo.Cinema.service.MoviesFromExcelService;
import com.delorenzo.Cinema.service.WeekService;
import com.delorenzo.Cinema.utils.Utils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
class CinemaApplicationTests implements PostgreSQLContainerInitializer{

	private static final Logger logger = LoggerFactory.getLogger(CinemaApplicationTests.class);

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	WeekRepository weekRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	ScreeningRepository screeningRepository;
	@Autowired
	Environment environment;



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


	@DisplayName("Test di integrazione: inizializzazione")
	@Test
	void initBatchTest() throws Exception {

		List<Scheduler> schedulers = schedulerInit();
		Optional<Scheduler> imaxScheduler = Utils.getSchedulerByName(schedulers,"imax");
		Optional<Scheduler> regularScheduler = Utils.getSchedulerByName(schedulers,"regular");
		WeekService weekService = new WeekService();
		MovieService movieService = new MovieService(movieRepository);
		MoviesFromExcelService moviesFromExcelService = new MoviesFromExcelService(environment);

		MainService mainService = new MainService(moviesFromExcelService,
				weekRepository,
				movieRepository,
				imaxScheduler.get(),
				regularScheduler.get(),
				movieService,
				weekService);

		mainService.initializationBatch();
		List<Screening> screenings = screeningRepository.findAll();
		Assertions.assertEquals(10, screenings.size());
		List<Week> weeks = weekRepository.findAll();
		Assertions.assertEquals(1, weeks.size());
	}

	@DisplayName("Test con tutte le dipendenze per verificare che il metodo" +
			"di inizializzazione Batch effettivamente " +
			"crei le proiezioni e la settimana" +
			"A db inizialmente ci sono 8 regular movies e 4 imax movies")
	@Test
	void weeklyBatchTest() throws Exception {

		List<Scheduler> schedulers = schedulerInit();
		Optional<Scheduler> imaxScheduler = Utils.getSchedulerByName(schedulers,"imax");
		Optional<Scheduler> regularScheduler = Utils.getSchedulerByName(schedulers,"regular");
		WeekService weekService = new WeekService();
		MovieService movieService = new MovieService(movieRepository);
		MoviesFromExcelService moviesFromExcelService = new MoviesFromExcelService(environment);

		MainService mainService = new MainService(moviesFromExcelService,
				weekRepository,
				movieRepository,
				imaxScheduler.get(),
				regularScheduler.get(),
				movieService,
				weekService);

		mainService.initializationBatch();
		mainService.weeklyBatch();
		List<Screening> screenings = screeningRepository.findAll();
		Assertions.assertEquals(20, screenings.size());
		List<Week> weeks = weekRepository.findAll();
		Assertions.assertEquals(2, weeks.size());

	}

	private List<Scheduler> schedulerInit() throws Exception {
		List<Scheduler> schedulers = new ArrayList<>();
		Scheduler imaxScheduler = new Scheduler("imax",2);
		Scheduler regularScheduler = new Scheduler("regular",10);
		SchedulerDataLoader schedulerDataLoader = new SchedulerDataLoader(imaxScheduler,regularScheduler,roomRepository);
		schedulerDataLoader.run(new DefaultApplicationArguments());
		schedulers.add(imaxScheduler);
		schedulers.add(regularScheduler);
		return schedulers;

	}



}
