package com.popcorncafe.employeeservice.service;

import com.popcorncafe.employeeservice.repository.PositionRepository;
import com.popcorncafe.employeeservice.repository.model.Position;
import com.popcorncafe.employeeservice.repository.model.Role;
import com.popcorncafe.employeeservice.service.dto.PositionDto;
import com.popcorncafe.employeeservice.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;
    @InjectMocks
    private PositionServiceImpl positionService;

    private final Random rn = new Random();

    private final List<Position> positions = new ArrayList<>();
    private Position position;
    private PositionDto positionDto;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < rn.nextInt(100); i++) {
            positions.add(createFakePosition());
        }

        position = createFakePosition();

        positionDto = new PositionDto(
                position.id(),
                position.name(),
                position.salary(),
                position.roles().stream().map(Role::name).toList()
        );
    }

    @Test
    void PositionService_GetPositions_ReturnsListOfPositionDtos() {

        Mockito.when(positionRepository.getPositions())
                .thenReturn(positions);

        var result = positionService.getPositions();

        assertThat(result.size()).isEqualTo(positions.size());
    }

    @Test
    void PositionService_GetPosition_ReturnsPositionDto() {

        Mockito.when(positionRepository.getPosition(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(position));

        var result = positionService.getPosition(position.id());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(position.id());
        assertThat(result.name()).isEqualTo(position.name());
        assertThat(result.salary()).isEqualTo(position.salary());
        assertThat(result.roles()).isEqualTo(position.roles().stream().map(Role::name).toList());
    }

    @Test
    void PositionService_CreatePosition_ReturnsPositionDto() {

        var positionId = UUID.randomUUID();

        var positionDto = new PositionDto(
            null,
            "Name #" + rn.nextInt(1000),
            rn.nextFloat(),
            Arrays.stream(Role.values())
                    .filter(role -> rn.nextBoolean()).map(Role::name).toList()
        );

        Mockito.when(positionRepository.createPosition(Mockito.any(Position.class))).thenReturn(positionId);

        var result = positionService.createPosition(positionDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(positionId);
        assertThat(result.name()).isEqualTo(positionDto.name());
        assertThat(result.salary()).isEqualTo(positionDto.salary());
        assertThat(result.roles()).isEqualTo(positionDto.roles());
    }

    @Test
    void PositionService_UpdatePosition_ReturnsResult() {
        var position = createFakePosition();

        var positionDto = new PositionDto(
                position.id(),
                position.name(),
                position.salary(),
                position.roles().stream().map(Role::name).toList()
        );

        Mockito.when(positionRepository.updatePosition(position))
                .thenReturn(true);

        var result = positionService.updatePosition(positionDto);

        assertThat(result).isTrue();
    }

    @Test
    void PositionService_DeletePosition_ReturnsResult() {

        Mockito.when(positionRepository.deletePosition(position.id()))
                .thenReturn(true);

        var result = positionService.deletePosition(position.id());

        assertThat(result).isTrue();
    }

    private Position createFakePosition() {
        return new Position(
                UUID.randomUUID(),
                "Name #" + rn.nextInt(1000),
                rn.nextFloat(),
                Arrays.stream(Role.values())
                        .filter(role -> rn.nextBoolean()).toList());
    }
}
