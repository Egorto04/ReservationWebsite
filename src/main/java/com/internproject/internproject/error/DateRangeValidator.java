package com.internproject.internproject.error;

import com.internproject.internproject.user.FlightInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, FlightInfo> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(FlightInfo flightInfo, ConstraintValidatorContext context) {
        if (flightInfo.getDepartureDate() == null || flightInfo.getArrivalDate() == null) {
            return true; // Assuming null dates are valid; adjust this logic if you need to handle nulls differently
        }

        LocalDate departureDate = flightInfo.getDepartureDate().toLocalDate();
        LocalDate arrivalDate = flightInfo.getArrivalDate().toLocalDate();
        LocalDate now = LocalDate.now();

        // Check if departure date is today or in the future
        if (departureDate.isBefore(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Departure date must be today or in the future.")
                    .addPropertyNode("departureDate").addConstraintViolation();
            return false;
        }

        // Check if arrival date is after departure date
        if (arrivalDate.isBefore(departureDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Arrival date must be after the departure date.")
                    .addPropertyNode("arrivalDate").addConstraintViolation();
            return false;
        }

        return true;
    }
}
