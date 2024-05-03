package com.popcorncafe.employeeservice.repository;

import com.popcorncafe.employeeservice.repository.model.Position;
import com.popcorncafe.employeeservice.repository.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static java.sql.Types.ARRAY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class PositionRepositoryTest {

    private static final List<Position> testPositions = new ArrayList<>();
    private static final int TEST_POSITIONS_COUNT = 100;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");

    Random rn = new Random();

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {

        for (int i = 0; i < TEST_POSITIONS_COUNT; i++) {

            var position = createFakePosition();

            parameterJdbcTemplate.update(
                    "INSERT INTO position (id, name, salary, roles) VALUES (:id, :name, :salary, :roles)",
                    Map.of(
                            "id", position.id(),
                            "name", position.name(),
                            "salary", position.salary(),
                            "roles", new SqlParameterValue(
                                    ARRAY,
                                    position.roles().stream()
                                            .map(Role::ordinal)
                                            .mapToInt(Integer::intValue)
                                            .toArray())));
            testPositions.add(position);
        }
    }

    @AfterEach
    void cleanUp() {
        parameterJdbcTemplate.update("DELETE FROM position", Map.of());
        testPositions.clear();
    }

    @Test
    void PositionRepository_GetPositions_ReturnsAllPositions() {
        var positions = positionRepository.getPositions();
        assertThat(positions.size()).isEqualTo(TEST_POSITIONS_COUNT);
    }

    @Test
    void PositionRepository_GetPosition_ReturnsOptionalPosition() {
        var position = testPositions.get(rn.nextInt(TEST_POSITIONS_COUNT) - 1);
        var result = positionRepository.getPosition(position.id());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(position);
    }

    @Test
    void PositionRepository_CreatePosition_ReturnsNewPositionId() {
        var position = createFakePosition();
        var newPositionId = positionRepository.createPosition(position);

        var result = positionRepository.getPosition(newPositionId);

        assertThat(result).isPresent();

        var createdPosition = result.get();

        assertThat(createdPosition.name()).isEqualTo(position.name());
        assertThat(createdPosition.salary()).isEqualTo(position.salary());
        assertThat(createdPosition.roles()).isEqualTo(position.roles());
    }

    @Test
    void PositionRepository_UpdatePosition_ReturnsResult() {
        var position = testPositions.get(rn.nextInt(TEST_POSITIONS_COUNT) - 1);
        var updatedPosition = new Position(
                position.id(),
                "Updated Name",
                1000.0f,
                Arrays.stream(Role.values())
                        .filter(role -> rn.nextBoolean()).toList());

        var result = positionRepository.updatePosition(updatedPosition);

        assertThat(result).isTrue();

        var resultPosition = positionRepository.getPosition(position.id()).get();

        assertThat(resultPosition).isEqualTo(updatedPosition);
    }

    @Test
    void PositionRepository_DeletePosition_ReturnsResult() {
        var position = testPositions.get(rn.nextInt(TEST_POSITIONS_COUNT - 1));
        var result = positionRepository.deletePosition(position.id());
        assertThat(result).isTrue();
        assertThat(positionRepository.getPosition(position.id())).isEmpty();
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
