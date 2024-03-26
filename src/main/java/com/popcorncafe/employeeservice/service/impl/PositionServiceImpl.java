package com.popcorncafe.employeeservice.service.impl;

import com.popcorncafe.employeeservice.repository.PositionRepository;
import com.popcorncafe.employeeservice.service.PositionService;
import com.popcorncafe.employeeservice.service.dto.PositionDto;
import com.popcorncafe.employeeservice.service.mapper.PositionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper mapper;

    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper mapper) {
        this.positionRepository = positionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PositionDto> getPositions() {
        return positionRepository.getPositions().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PositionDto getPosition(UUID id) {
        return positionRepository.getPosition(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public PositionDto createPosition(PositionDto positionDto) {
        var positionId = positionRepository.createPosition(mapper.toModel(positionDto));
        return new PositionDto(
                positionId,
                positionDto.name(),
                positionDto.salary(),
                positionDto.roles()
        );
    }

    @Override
    public boolean updatePosition(PositionDto position) {
        return positionRepository.updatePosition(mapper.toModel(position));
    }

    @Override
    public boolean deletePosition(UUID id) {
        return positionRepository.deletePosition(id);
    }
}
