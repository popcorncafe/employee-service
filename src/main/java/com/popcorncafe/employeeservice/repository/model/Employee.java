package com.popcorncafe.employeeservice.repository.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Employee(UUID id, String firstname, String surname, LocalDate dateOfBirth, String email,
                       String phoneNumber, Instant registerDate, UUID positionId, UUID storeId,
                       byte personalPercentage) implements Model {
}
