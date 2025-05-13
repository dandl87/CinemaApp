package com.delorenzo.Cinema.service;

import com.delorenzo.Cinema.dto.NewMovie;
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
import java.time.LocalDate;
import java.util.*;

@Service
public class MoviesFromExcelService {

    private final Environment env;

    private FileInputStream inputStream;

    private String fileName;

    private Workbook workbook;

    private Sheet firstSheet;

    private Iterator<Row> rowIterator;

    private static final Logger logger = LoggerFactory.getLogger(MoviesFromExcelService.class);

    public MoviesFromExcelService(Environment env) throws IOException {
        this.env = env;
        fileName = env.getProperty("data.source[0]");
        Optional<String> optEnvProp = Optional.ofNullable(fileName);
        if(optEnvProp.isEmpty())
            throw new FileNotFoundException("file path not valid");

        this.inputStream = new FileInputStream("src/main/resources/"+optEnvProp.get());
        this.workbook = new XSSFWorkbook(inputStream);

    }

    public List<NewMovie> readFile() throws IOException {
        List<NewMovie> newMovies = new ArrayList<>();
        this.firstSheet = workbook.getSheetAt(0);
        this.rowIterator = firstSheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {


            Row nextRow = rowIterator.next();

            //if(checkDate(nextRow)) {

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
        //}
        workbook.close();
        return newMovies;

    }


}
