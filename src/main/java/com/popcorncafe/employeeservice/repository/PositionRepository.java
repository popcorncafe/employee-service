package com.popcorncafe.employeeservice.repository;

import com.popcorncafe.employeeservice.repository.model.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository {

    List<Position> getPositions();

    Optional<Position> getPosition(UUID id);

    UUID createPosition(Position position);

    boolean updatePosition(Position position);

    boolean deletePosition(UUID id);
}
