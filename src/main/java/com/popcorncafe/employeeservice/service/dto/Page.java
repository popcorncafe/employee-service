package com.popcorncafe.employeeservice.service.dto;

public record Page(
        int size,
        int number
) {
    public int offset() {
        return number() * size();
    }
}
