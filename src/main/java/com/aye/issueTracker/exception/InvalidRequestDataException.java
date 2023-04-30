package com.aye.issueTracker.exception;

public class InvalidRequestDataException extends RuntimeException{

    public InvalidRequestDataException(String message) {
        super(message);
    }
}
