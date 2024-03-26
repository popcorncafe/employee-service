package com.popcorncafe.employeeservice.service.dto;

import java.util.List;
import java.util.UUID;

public record PositionDto(
        UUID id,
        String name,
        float salary,
        List<String> roles
) implements Dto {
}
