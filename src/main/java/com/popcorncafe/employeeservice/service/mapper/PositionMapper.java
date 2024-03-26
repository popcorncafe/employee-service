package com.popcorncafe.employeeservice.service.mapper;

import com.popcorncafe.employeeservice.repository.model.Position;
import com.popcorncafe.employeeservice.repository.model.Role;
import com.popcorncafe.employeeservice.service.dto.PositionDto;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper implements Mapper<Position, PositionDto> {
    @Override
    public Position toModel(PositionDto dto) {
        return new Position(
                dto.id(),
                dto.name(),
                dto.salary(),
                dto.roles().stream()
                        .map(Role::valueOf)
                        .toList()
        );
    }

    @Override
    public PositionDto toDto(Position model) {
        return new PositionDto(
                model.id(),
                model.name(),
                model.salary(),
                model.roles().stream()
                        .map(Role::name)
                        .toList()
        );
    }
}
