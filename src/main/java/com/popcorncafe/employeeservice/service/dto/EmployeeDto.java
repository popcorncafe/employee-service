package com.popcorncafe.employeeservice.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeDto(
        UUID id,
        String firstname,
        String surname,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        Instant registerDate,
        UUID positionId,
        UUID storeId,
        byte personalPercentage
) implements Dto {
}
