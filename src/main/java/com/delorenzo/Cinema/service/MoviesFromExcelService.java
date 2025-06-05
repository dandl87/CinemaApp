package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.dto.NewMovieDTO;
import com.delorenzo.Cinema.exception.UploadFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MoviesFromExcelService {

    private final Path rootLocation;

    private static final Logger logger = LoggerFactory.getLogger(MoviesFromExcelService.class);

    public MoviesFromExcelService(StorageProperties properties) throws IOException {

        if (properties.getLocation().trim().isEmpty()) {
            throw new UploadFileException("File upload location can not be empty");
        }
        this.rootLocation = Paths.get(properties.getLocation());

    }

    public List<NewMovieDTO> readFile(String fileName) throws IOException {

        fileName = fileName.trim();

        try (FileInputStream inputStream = new FileInputStream(rootLocation + "/" + fileName)) {

            try (Workbook workbook = new XSSFWorkbook(inputStream)) {

                List<NewMovieDTO> newMovies = new ArrayList<>();
                Sheet firstSheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = firstSheet.iterator();
                rowIterator.next();

                while (rowIterator.hasNext()) {


                    Row nextRow = rowIterator.next();


                    NewMovieDTO newMovie = new NewMovieDTO();

                    Iterator<Cell> cellIterator = nextRow.cellIterator();

                    while (cellIterator.hasNext()) {

                        Cell nextCell = cellIterator.next();

                        int columnIndex = nextCell.getColumnIndex();

                        switch (columnIndex) {
                            case 0:
                                String title = nextCell.getStringCellValue();
                                newMovie.setTitle(title);
                                break;
                            case 1:
                                String director = nextCell.getStringCellValue();
                                newMovie.setDirector(director);
                                break;
                            case 2:
                                double year = nextCell.getNumericCellValue();
                                newMovie.setYear((int) year);
                                break;
                            case 3:
                                double duration = nextCell.getNumericCellValue();
                                newMovie.setDuration((int) duration);
                                break;

                            case 4:
                                boolean imax = nextCell.getBooleanCellValue();
                                newMovie.setImax(imax);
                                break;

                            case 5:
                                double value = nextCell.getNumericCellValue();
                                newMovie.setValue(value);
                                break;

                            default:
                                logger.info("not a valid cell");
                        }

                    }
                    newMovies.add(newMovie);
                }
                return newMovies;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}