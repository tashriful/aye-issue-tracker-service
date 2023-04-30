package com.aye.issueTracker.exception;

public class InvalidDepartmentDataException extends RuntimeException{

    public InvalidDepartmentDataException(String message) {
        super(message);
    }
}
