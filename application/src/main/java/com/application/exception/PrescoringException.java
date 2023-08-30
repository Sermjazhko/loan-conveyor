package com.application.exception;

import java.util.List;

public class PrescoringException extends Exception {

    private final List<String> reasons;

    public PrescoringException(List<String> reasons) {
        super("Prescoring error");
        this.reasons = reasons;
    }

    public List<String> getAllErrors() {
        return reasons;
    }
}
