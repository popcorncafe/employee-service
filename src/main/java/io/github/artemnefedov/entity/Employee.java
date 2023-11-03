package io.github.artemnefedov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

        private UUID id;
        private String firstname;
        private String surname;
        private LocalDate dateOfBirth;
        private String email;
        private String phoneNumber;
        private Instant registerDate;
        private UUID positionId;
        private UUID storeId;
        private byte personalPercentage;
}
