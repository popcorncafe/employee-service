package io.github.artemnefedov.repository;

import io.github.artemnefedov.entity.Employee;
import io.github.artemnefedov.entity.Position;
import io.github.artemnefedov.entity.Role;
import io.github.artemnefedov.repository.EmployeeRepository;
import io.github.artemnefedov.rowMapper.EmployeeMapper;
import io.github.artemnefedov.rowMapper.PositionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Types.ARRAY;
import static java.sql.Types.DATE;

@Repository
@RequiredArgsConstructor
public class EmployeeDataAccessService implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {

        var sql = "SELECT * FROM employee";

        return jdbcTemplate.query(sql, new EmployeeMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(UUID id) {

        var sql = """
                SELECT * FROM employee
                WHERE id = ?
                LIMIT 1
                """;

        return jdbcTemplate.query(sql, new EmployeeMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeByEmail(String email) {

        var sql = """
                SELECT * FROM employee
                WHERE email = ?
                LIMIT 1
                """;

        return jdbcTemplate.query(sql, new EmployeeMapper(), email)
                .stream()
                .findFirst();
    }

    @Override
    @Transactional
    public boolean addEmployee(Employee employee) {

        var sql = """
                INSERT INTO employee(firstname, surname, date_of_birth, email, phone_number, position_id, store_id, personal_percentage)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql,
                employee.getFirstname(),
                employee.getSurname(),
                new SqlParameterValue(DATE, employee.getDateOfBirth()),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getPositionId(),
                employee.getStoreId(),
                employee.getPersonalPercentage()) >= 1;
    }

    @Override
    @Transactional
    public boolean updateEmployee(Employee employee) {

        var sql = """
                UPDATE employee
                SET firstname = ?,
                    surname = ?,
                    date_of_birth = ?,
                    email = ?,
                    phone_number = ?,
                    position_id = ?,
                    store_id = ?,
                    personal_percentage = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql,
                employee.getFirstname(),
                employee.getSurname(),
                new SqlParameterValue(DATE, employee.getDateOfBirth()),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getPositionId(),
                employee.getStoreId(),
                employee.getPersonalPercentage(),
                employee.getId()) >= 1;
    }

    @Override
    @Transactional
    public boolean deleteEmployee(UUID id) {

        var sql = "DELETE FROM employee WHERE id = ?";

        return jdbcTemplate.update(sql, id) >= 1;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Position> getAllPositions() {

        var sql = "SELECT * FROM position";

        return jdbcTemplate.query(sql, new PositionMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Position> getPositionById(UUID id) {

        var sql = """
                SELECT * FROM position
                WHERE id = ?
                LIMIT 1""";

        return jdbcTemplate.query(sql, new PositionMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    @Transactional
    public boolean addPosition(Position position) {

        var sql = """
                INSERT INTO position (name, salary, roles)
                VALUES (?, ?, ?);
                """;

        return jdbcTemplate.update(sql,
                position.getName(),
                position.getSalary(),
                new SqlParameterValue(ARRAY, position.getRoles()
                        .stream()
                        .map(Role::ordinal)
                        .mapToInt(Integer::intValue)
                        .toArray())) >= 1;
    }

    @Override
    @Transactional
    public boolean updatePosition(Position position) {

        var sql = """
                UPDATE position
                SET name = ?,
                    salary = ?,
                    roles = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql,
                position.getName(),
                position.getSalary(),
                new SqlParameterValue(ARRAY, position.getRoles()
                        .stream()
                        .map(Role::ordinal)
                        .mapToInt(Integer::intValue)
                        .toArray()),
                position.getId()) >= 1;
    }

    @Override
    @Transactional
    public boolean deletePosition(UUID id) {

        var sql = "DELETE FROM position WHERE id = ?";

        return jdbcTemplate.update(sql, id) >= 1;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEmployeeWithThisNumber(String phoneNumber) {

        var sql = """
                SELECT EXISTS (
                SELECT employee.id FROM employee
                WHERE phone_number = ?)
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, phoneNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByStoreId(UUID id) {

        var sql = """
        SELECT * FROM employee
        WHERE store_id = ?
        """;

        return jdbcTemplate.query(sql, new EmployeeMapper(), id);
    }
//
//    @Override
//    public boolean findEmployeeByPhoneNumber(String phoneNumber) {
//
//        var sql = """
//                SELECT EXISTS (
//                SELECT employee.id FROM employee
//                WHERE phone_number = ?
//                )
//                """;
//
//        return Boolean.TRUE.equals(jdbcTemplate.query(sql,
//                resultSet -> {
//                    return resultSet.getBoolean("exists");
//                }
//                , phoneNumber));
//    }
//
//    @Override
//    public List<Employee> getEmployeesByStoreId(UUID id) {
//
//        var sql = """
//                SELECT * FROM employee AS emp
//                WHERE emp.store_id = ?
//                """;
//
//        return jdbcTemplate.query(sql, new EmployeeMapper(), id);
//    }
//
//    @Override
//    public List<Position> getAllPosition() {
//
//        var sql = "SELECT * FROM position";
//
//        return jdbcTemplate.query(sql, new PositionMapper());
//    }
//
//    @Override
//    public Optional<Position> getPositionById(UUID id) {
//
//        var sql = """
//                SELECT p.id, p.name, p.salary, p.roles FROM position AS p
//                WHERE id = ?
//                LIMIT 1
//                """;
//
//        return jdbcTemplate.query(sql, new PositionMapper(), id)
//                .stream()
//                .findFirst();
//    }
//
//    @Override
//    public boolean addPosition(Position position) {
//
//        var sql = """
//                INSERT INTO position (name, salary, roles)
//                VALUES (?, ?, ?);
//                """;
//
//        return jdbcTemplate.update(sql,
//                position.getName(),
//                position.getSalary(),
//                new SqlParameterValue(ARRAY, position.getRoles()
//                        .stream()
//                        .map(Role::ordinal)
//                        .mapToInt(Integer::intValue)
//                        .toArray())) > 0;
//    }
//
//    @Override
//    public boolean addEmployee(Employee employee) {
//
//        var sql = """
//                INSERT INTO employee (firstname, surname, date_of_birth, email, phone_number, position_id, store_id, personal_percentage)
//                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
//                """;
//
//        return jdbcTemplate.update(sql,
//                employee.getFirstname(),
//                employee.getSurname(),
//                new SqlParameterValue(DATE, employee.getDateOfBirth()),
//                employee.getEmail(),
//                employee.getPhoneNumber(),
//                employee.getPositionId(),
//                employee.getStoreId(),
//                employee.getPersonalPercentage()) > 0;
//    }
//
//    @Override
//    public boolean updateEmployee(Employee employee) {
//
//        var sql = """
//                UPDATE employee
//                SET firstname = ?,
//                    surname = ?,
//                    date_of_birth = ?,
//                    email = ?,
//                    phone_number = ?,
//                    position_id = ?,
//                    store_id = ?,
//                    personal_percentage = ?
//                WHERE id = ?
//                """;
//
//        return jdbcTemplate.update(sql,
//                employee.getFirstname(),
//                employee.getSurname(),
//                new SqlParameterValue(DATE, employee.getDateOfBirth()),
//                employee.getEmail(),
//                employee.getPhoneNumber(),
//                employee.getPositionId(),
//                employee.getStoreId(),
//                employee.getPersonalPercentage(),
//                employee.getId()) > 0;
//    }
//
//    @Override
//    public boolean updatePosition(Position position) {
//
//        var sql = """
//                UPDATE position
//                SET name = ?,
//                    salary = ?,
//                    roles = ?
//                WHERE id = ?
//                """;
//
//        return jdbcTemplate.update(sql,
//                position.getName(),
//                position.getSalary(),
//                new SqlParameterValue(ARRAY, position.getRoles()
//                        .stream()
//                        .map(Role::ordinal)
//                        .mapToInt(Integer::intValue)
//                        .toArray()),
//                position.getId()) > 0;
//    }
//
//    @Override
//    public boolean deleteEmployee(UUID id) {
//
//        var sql = "DELETE FROM employee WHERE id = ?;";
//
//        return jdbcTemplate.update(sql, id) > 0;
//    }
//
//    @Override
//    public boolean deletePosition(UUID id) {
//
//        var sql = "DELETE FROM position WHERE id = ?;";
//
//        return jdbcTemplate.update(sql, id) > 0;
//    }
}