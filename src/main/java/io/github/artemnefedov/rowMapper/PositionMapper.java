package io.github.artemnefedov.rowMapper;

import io.github.artemnefedov.entity.Position;
import io.github.artemnefedov.entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class PositionMapper implements RowMapper<Position> {

    @Override
    public Position mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return Position.builder()
                .id(resultSet.getObject("id", UUID.class))
                .name(resultSet.getString("name"))
                .salary(resultSet.getFloat("salary"))
                .roles(stream(((Short[]) resultSet.getArray("roles").getArray()))
                        .map(roleNum -> Role.values()[roleNum.intValue()])
                        .collect(Collectors.toList()))
                .build();
    }
}
