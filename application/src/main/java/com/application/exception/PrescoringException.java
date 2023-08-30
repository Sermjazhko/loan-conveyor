package com.application.exception;

import java.util.List;

public class PrescoringException extends Exception {

    private final List<String> errorsMessage;

    public PrescoringException(List<String> errorMessage) {
        super(errorMessage.get(0));
        this.errorsMessage = errorMessage;
    }

    public List<String> getAllErrors() {
        return errorsMessage;
    }
}
