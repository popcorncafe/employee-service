package com.popcorncafe.employeeservice.repository.rowMapper;

import com.popcorncafe.employeeservice.repository.model.Position;
import com.popcorncafe.employeeservice.repository.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class PositionMapper implements RowMapper<Position> {

    @Override
    public Position mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new Position(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("name"),
                resultSet.getFloat("salary"),
                stream(((Short[]) resultSet.getArray("roles").getArray()))
                        .map(roleNum -> Role.values()[roleNum.intValue()])
                        .collect(Collectors.toList())
        );
    }
}
