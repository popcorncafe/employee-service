package com.popcorncafe.employeeservice.repository.rowMapper;

import com.popcorncafe.employeeservice.repository.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Employee(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getTimestamp("register_date").toInstant(),
                resultSet.getObject("position_id", UUID.class),
                resultSet.getObject("store_id", UUID.class),
                resultSet.getByte("personal_percentage")
        );
    }
}
