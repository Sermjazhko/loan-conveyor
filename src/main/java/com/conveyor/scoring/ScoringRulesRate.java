package com.conveyor.scoring;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.conveyor.scoring.EmploymentStatus.*;
import static com.conveyor.scoring.Gender.*;
import static com.conveyor.scoring.MaritalStatus.MARRIED;
import static com.conveyor.scoring.MaritalStatus.NOT_MARRIED;
import static com.conveyor.scoring.Position.*;


public class ScoringRulesRate {
    public static Double getEmploymentStatus(EmploymentStatus employmentStatus) {
        Double rate;
        switch (employmentStatus){
            case SELF_EMPLOYED:
                rate = 1.0;
                break;
            case BUSINESS:
                rate = 3.0;
                break;
            default:
                throw new IllegalArgumentException("Not employment status");
        }
   /*     if (employmentStatus.equals(SELF_EMPLOYED)) {
            rate = 1.0;
        } else if (employmentStatus.equals(BUSINESS)) {
            rate = 3.0;
        } else {
            throw new IllegalArgumentException("Not employment status");
        }*/
        return rate;
    }

    public static Double getPosition(Position position) {
        Double rate;
        switch (position){
            case MANAGER:
                rate = 0.0;
                break;
            case MIDDLE_MANAGER:
                rate = -2.0;
                break;
            case TOP_MANAGER:
                rate = -4.0;
                break;
            default:
                throw new IllegalArgumentException("Not position");
        }
      /*  if (position.equals(MANAGER)) {
            rate = 0.0;
        } else if (position.equals(MIDDLE_MANAGER)) {
            rate = -2.0;
        } else if (position.equals(TOP_MANAGER)) {
            rate = -4.0;
        } else {
            throw new IllegalArgumentException("Not position");
        }*/
        return rate;
    }

    public static Double getMaritalStatus(MaritalStatus maritalStatus) {
        Double rate;
        switch (maritalStatus){
            case MARRIED:
                rate = -3.0;
                break;
            case NOT_MARRIED:
                rate = -1.0;
                break;
            default:
                throw new IllegalArgumentException("Not marital status");
        }
     /*   if (maritalStatus.equals(MARRIED)) {
            rate = -3.0;
        } else if (maritalStatus.equals(NOT_MARRIED)) {
            rate = 1.0;
        } else {
            throw new IllegalArgumentException("Not marital status");
        }*/
        return rate;
    }

    public static Double getDependentAmount(Integer dependentAmount) {
        Double rate = 0.0;
        if(dependentAmount > 1) {
            rate += 1.0;
        }
        return rate;
    }

    public static Double getGender(Gender gender, LocalDate localDate) {
        LocalDate localDateNow = LocalDate.now();
        long years = localDate.until(localDateNow, ChronoUnit.YEARS);
        Double rate = 0.0;
        switch (gender){
            case WOMAN:
                if (34 < years && years < 61) {
                    rate = -3.0;
                }
                break;
            case MAN:
                if (29 < years && years < 56) {
                    rate = -3.0;
                }
                break;
            case OTHER:
                rate = 3.0;
                break;
            default:
                throw new IllegalArgumentException("Not employment status");
        }
        /*if (gender.equals(WOMAN)) {
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
        }*/
        return rate;
    }
}
