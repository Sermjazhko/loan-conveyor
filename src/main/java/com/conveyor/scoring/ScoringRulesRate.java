package com.conveyor.scoring;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.conveyor.scoring.EmploymentStatus.*;
import static com.conveyor.scoring.Gender.*;
import static com.conveyor.scoring.MaritalStatus.MARRIED;
import static com.conveyor.scoring.MaritalStatus.NOT_MARRIED;
import static com.conveyor.scoring.Position.*;

//TODO <T extends Enum<T>> попробовать потом
public class ScoringRulesRate {
    public static Double getEmploymentStatus(Enum employmentStatus) {
        Double rate;
        if (employmentStatus.equals(SELF_EMPLOYED)) {
            rate = 1.0;
        } else if (employmentStatus.equals(BUSINESS)) {
            rate = 3.0;
        } else {
            throw new IllegalArgumentException("Not employment status");
        }
        return rate;
    }

    public static Double getPosition(Enum position) {
        Double rate;
        if (position.equals(MANAGER)) {
            rate = 0.0;
        } else if (position.equals(MIDDLE_MANAGER)) {
            rate = -2.0;
        } else if (position.equals(TOP_MANAGER)) {
            rate = -4.0;
        } else {
            throw new IllegalArgumentException("Not position");
        }
        return rate;
    }

    public static Double getMaritalStatus(Enum maritalStatus) {
        Double rate;
        if (maritalStatus.equals(MARRIED)) {
            rate = -3.0;
        } else if (maritalStatus.equals(NOT_MARRIED)) {
            rate = 1.0;
        } else {
            throw new IllegalArgumentException("Not marital status");
        }
        return rate;
    }

    public static Double getDependentAmount(Integer dependentAmount) {
        Double rate = 0.0;
        if(dependentAmount > 1) {
            rate += 1.0;
        }
        return rate;
    }

    public static Double getGender(Enum gender, LocalDate localDate) {
        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);
        Double rate = 0.0;
        if (gender.equals(WOMAN)) {
            if (34 < years && years < 61) {
                rate = -3.0;
            }
        } else if (gender.equals(MAN)) {
            if (29 < years && years < 56) {
                rate = -3.0;
            }
        } else if (gender.equals(OTHER)) {
            rate = 3.0;
        } else {
            throw new IllegalArgumentException("Not employment status");
        }
        return rate;
    }
}
