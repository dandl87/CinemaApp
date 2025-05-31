package com.delorenzo.Cinema.unit;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.service.MoviesFromExcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExcelFetchingDataUnitTest {
    List<NewMovieDTO> newMovies;


    @BeforeEach
    void init(){
        StorageProperties storageProperties = new StorageProperties();
        try {
            MoviesFromExcelService moviesFromExcelService = new MoviesFromExcelService(storageProperties);
            newMovies = moviesFromExcelService.readFile("test.xlsx");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void checkNewMoviesFromFile(){
        for (NewMovieDTO newMovie : newMovies) {
            assertNotNull(newMovie);
            System.out.println(newMovie);
        }
    }
}
