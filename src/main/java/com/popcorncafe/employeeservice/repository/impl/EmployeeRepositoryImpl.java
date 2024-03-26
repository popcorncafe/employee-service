package com.popcorncafe.employeeservice.repository.impl;

import com.popcorncafe.employeeservice.repository.EmployeeRepository;
import com.popcorncafe.employeeservice.repository.model.Employee;
import com.popcorncafe.employeeservice.repository.rowMapper.EmployeeMapper;
import com.popcorncafe.employeeservice.service.dto.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public EmployeeRepositoryImpl(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    @Override
    public List<Employee> getEmployees(Page page) {
        return parameterJdbcTemplate.query("""
                SELECT * FROM employee
                ORDER BY                           
                LIMIT :page_size
                OFFSET :page_offset
                """,
                Map.of(
                        "page_size", page.size(),
                        "page_offset", page.offset()
                ), new EmployeeMapper());
    }

    @Override
    public Optional<Employee> getEmployee(UUID id) {
        return parameterJdbcTemplate.query("""
                SELECT * FROM employee
                WHERE id = :id
                LIMIT 1
                """,
                Map.of("id", id), new EmployeeMapper())
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return parameterJdbcTemplate.query("""
                SELECT * FROM employee
                WHERE email = :email
                LIMIT 1
                """,
                Map.of("email", email), new EmployeeMapper())
                .stream()
                .findFirst();
    }

    @Override
    public UUID createEmployee(Employee employee) {
        return parameterJdbcTemplate.queryForObject("""
                INSERT INTO employee (firstname, surname, date_of_birth, email, phone_number, position_id, store_id, personal_percentage)
                VALUES (:firstname, :surname, :date_of_birth, :email, :phone_number, :position_id, :store_id, :personal_percentage)
                RETURNING id;
                """,
                Map.of(
                        "firstname", employee.firstname(),
                        "surname", employee.surname(),
                        "date_of_birth", employee.dateOfBirth(),
                        "email", employee.email(),
                        "phone_number", employee.phoneNumber(),
                        "position_id", employee.positionId(),
                        "store_id", employee.storeId(),
                        "personal_percentage", employee.personalPercentage()
                ), UUID.class);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return parameterJdbcTemplate.update("""
                UPDATE employee
                SET firstname = :firstname,
                    surname = :surname,
                    date_of_birth = :date_of_birth,
                    email = :email,
                    phone_number = :phone_number,
                    position_id = :position_id,
                    store_id = :store_id,
                    personal_percentage = :personal_percentage
                WHERE id = :id;
                """,
                Map.of(
                        "firstname", employee.firstname(),
                        "surname", employee.surname(),
                        "date_of_birth", employee.dateOfBirth(),
                        "email", employee.email(),
                        "phone_number", employee.phoneNumber(),
                        "position_id", employee.positionId(),
                        "store_id", employee.storeId(),
                        "personal_percentage", employee.personalPercentage(),
                        "id", employee.id()
                )) >= 1;
    }

    @Override
    public boolean deleteEmployee(UUID id) {
        return parameterJdbcTemplate.update("""
                DELETE FROM employee
                WHERE id = :id;
                """,
                Map.of("id", id)) >= 1;
    }

    @Override
    public boolean hasEmployeeWithThisNumber(String phoneNumber) {
        return parameterJdbcTemplate.queryForObject("""
                SELECT EXISTS (
                SELECT employee.id FROM employee
                WHERE phone_number = :phone_number
                )
                """,
                Map.of("phone_number", phoneNumber), Boolean.class);
    }

    @Override
    public List<Employee> getEmployeesByStoreId(UUID id) {
        return parameterJdbcTemplate.query("""
                SELECT * FROM employee
                WHERE store_id = :store_id
                """,
                Map.of("store_id", id), new EmployeeMapper());
    }

    @Override
    public int getEmployeeCount() {
        return parameterJdbcTemplate.query(
                "SELECT COUNT(*) FROM employee",
                (rs, rowNum) -> rs.getInt(1)
        ).stream()
                .findFirst().orElse(0);
    }
}