package org.example;

public class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
