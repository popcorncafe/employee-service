package com.popcorncafe.employeeservice.repository.model;

import java.util.List;
import java.util.UUID;

public record Position(UUID id, String name, float salary, List<Role> roles) implements Model {
}
