package com.popcorncafe.employeeservice.repository.impl;

import com.popcorncafe.employeeservice.repository.PositionRepository;
import com.popcorncafe.employeeservice.repository.model.Position;
import com.popcorncafe.employeeservice.repository.model.Role;
import com.popcorncafe.employeeservice.repository.rowMapper.PositionMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Types.ARRAY;

@Repository
public class PositionRepositoryImpl implements PositionRepository {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public PositionRepositoryImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public List<Position> getPositions() {
        return parameterJdbcTemplate.query("SELECT * FROM position;", new PositionMapper());
    }

    @Override
    public Optional<Position> getPosition(UUID id) {
        return parameterJdbcTemplate.query(
                "SELECT * FROM position WHERE id = :id LIMIT 1;",
                Map.of("id", id),
                new PositionMapper()).stream()
                .findFirst();
    }

    @Override
    public UUID createPosition(Position position) {
        return parameterJdbcTemplate.queryForObject(
                "INSERT INTO position (name, salary, roles) VALUES (:name, :salary, :roles) RETURNING id;",
                Map.of(
                        "name", position.name(),
                        "salary", position.salary(),
                        "roles", new SqlParameterValue(
                                ARRAY,
                                position.roles().stream()
                                        .map(Role::ordinal)
                                        .mapToInt(Integer::intValue)
                                        .toArray())
                ),
                UUID.class);
    }

    @Override
    public boolean updatePosition(Position position) {
        return parameterJdbcTemplate.update(
                "UPDATE position SET name = :name, salary = :salary, roles = :roles WHERE id = :id;",
                Map.of(
                        "id", position.id(),
                        "name", position.name(),
                        "salary", position.salary(),
                        "roles", new SqlParameterValue(
                                ARRAY,
                                position.roles().stream()
                                        .map(Role::ordinal)
                                        .mapToInt(Integer::intValue)
                                        .toArray()
                        ))) >= 1;
    }

    @Override
    public boolean deletePosition(UUID id) {
        return parameterJdbcTemplate.update("DELETE FROM position WHERE id = :id", Map.of("id", id)) >= 1;
    }
}
