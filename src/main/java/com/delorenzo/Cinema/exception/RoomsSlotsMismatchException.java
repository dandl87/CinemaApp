package com.delorenzo.Cinema.exception;

public class RoomsSlotsMismatchException extends RuntimeException {
    public RoomsSlotsMismatchException() {
        super("Scheduler slot number and room number mismatch");
    }
}
