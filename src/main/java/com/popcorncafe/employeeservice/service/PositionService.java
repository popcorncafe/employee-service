package com.popcorncafe.employeeservice.service;

import com.popcorncafe.employeeservice.service.dto.PositionDto;

import java.util.List;
import java.util.UUID;

public interface PositionService {

    List<PositionDto> getPositions();

    PositionDto getPosition(UUID id);

    PositionDto createPosition(PositionDto position);

    boolean updatePosition(PositionDto position);

    boolean deletePosition(UUID id);
}
