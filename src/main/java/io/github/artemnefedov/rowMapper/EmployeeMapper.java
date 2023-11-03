package io.github.artemnefedov.rowMapper;

import io.github.artemnefedov.entity.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return Employee.builder()
                .id(resultSet.getObject("id", UUID.class))
                .firstname(resultSet.getString("firstname"))
                .surname(resultSet.getString("surname"))
                .dateOfBirth(resultSet.getDate("date_of_birth").toLocalDate())
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getString("phone_number"))
                .registerDate(resultSet.getTimestamp("register_date").toInstant())
                .positionId(resultSet.getObject("position_id", UUID.class))
                .storeId(resultSet.getObject("store_id", UUID.class))
                .personalPercentage(resultSet.getByte("personal_percentage"))
                .build();
    }
}
