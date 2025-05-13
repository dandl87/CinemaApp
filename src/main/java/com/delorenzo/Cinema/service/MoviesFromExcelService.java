package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.conf.StorageProperties;
import com.delorenzo.Cinema.dto.NewMovie;
import com.delorenzo.Cinema.exception.StorageException;
import com.delorenzo.Cinema.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
public class MoviesFromExcelService {

    private final Path rootLocation;

    private FileInputStream inputStream;

    private String fileName;

    private Workbook workbook;

    private Sheet firstSheet;

    private Iterator<Row> rowIterator;

    private static final Logger logger = LoggerFactory.getLogger(MoviesFromExcelService.class);

    public MoviesFromExcelService(StorageProperties properties) throws IOException {

        if(properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be empty");
        }
        this.rootLocation = Paths.get(properties.getLocation());


    }

    public List<NewMovie> readFile(String fileName) throws IOException {

        fileName = fileName.trim();
 
        this.inputStream = new FileInputStream("target/classes/files/"+fileName);
        this.workbook = new XSSFWorkbook(inputStream);
        List<NewMovie> newMovies = new ArrayList<>();
        this.firstSheet = workbook.getSheetAt(0);
        this.rowIterator = firstSheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {


            Row nextRow = rowIterator.next();


                NewMovie newMovie = new NewMovie();

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
        workbook.close();
        return newMovies;

    }


}
