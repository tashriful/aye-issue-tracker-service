package com.aye.issueTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotDeletableException extends RuntimeException {
    public NotDeletableException(String message) {
        super(message);
    }
}
