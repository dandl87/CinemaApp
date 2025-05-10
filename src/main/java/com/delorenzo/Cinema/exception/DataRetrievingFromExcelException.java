package com.delorenzo.Cinema.exception;

import java.io.IOException;

public class DataRetrievingFromExcelException extends IOException {
    public DataRetrievingFromExcelException() {
        super("Error retrieving data from file excel");
    }
}
