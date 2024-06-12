package com.popcorncafe.employeeservice.repository;

import com.popcorncafe.employeeservice.repository.model.Employee;
import com.popcorncafe.employeeservice.service.dto.Page;
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

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static java.sql.Types.ARRAY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class EmployeeRepositoryTest {

    private static final List<Employee> testEmployee = new ArrayList<>();
    private static final int TEST_EMPLOYEE_COUNT = 100;
    private static UUID testPositionId;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");

    Random rn = new Random();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    @BeforeEach
    void setUp() {

        if (testPositionId == null) {
            testPositionId = parameterJdbcTemplate.queryForObject(
                    "INSERT INTO position (name, salary, roles) VALUES (:name, :salary, :roles) RETURNING id",
                    Map.of("name", "Test position", "salary", 123.4, "roles",
                            new SqlParameterValue(ARRAY, new int[] { 0 })),
                    UUID.class);
        }

        for (int i = 0; i < TEST_EMPLOYEE_COUNT; i++) {

            var employee = createFakeEmployee();

            var newEmpId = parameterJdbcTemplate.queryForObject(
                    """
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
                            "position_id", testPositionId,
                            "store_id", employee.storeId(),
                            "personal_percentage", employee.personalPercentage()),
                    UUID.class);

            testEmployee.add(new Employee(newEmpId, employee.firstname(), employee.surname(), employee.dateOfBirth(),
                    employee.email(), employee.phoneNumber(), Instant.now(), testPositionId, employee.storeId(),
                    employee.personalPercentage()));
        }
    }

    @AfterEach
    void cleanUp() {
        parameterJdbcTemplate.update("DELETE FROM employee", Map.of());
        testEmployee.clear();
    }

    @Test
    void EmployeeRepository_GetEmployees_ReturnsAllEmployees() {
        List<Employee> employees = employeeRepository.getEmployees(new Page(testEmployee.size(), 0));

        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(testEmployee.size());
    }

    @Test
    void EmployeeRepository_GetEmployee_ReturnsOptionalEmployee() {
        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        var optionalEmployee = employeeRepository.getEmployee(expectedEmployee.id());

        assertThat(optionalEmployee).isPresent();

        var actualEmployee = optionalEmployee.get();

        assertThat(actualEmployee.id()).isEqualTo(expectedEmployee.id());
        assertThat(actualEmployee.firstname()).isEqualTo(expectedEmployee.firstname());
        assertThat(actualEmployee.surname()).isEqualTo(expectedEmployee.surname());
        assertThat(actualEmployee.dateOfBirth()).isEqualTo(expectedEmployee.dateOfBirth());
        assertThat(actualEmployee.email()).isEqualTo(expectedEmployee.email());
        assertThat(actualEmployee.phoneNumber()).isEqualTo(expectedEmployee.phoneNumber());
        assertThat(actualEmployee.positionId()).isEqualTo(expectedEmployee.positionId());
        assertThat(actualEmployee.storeId()).isEqualTo(expectedEmployee.storeId());
        assertThat(actualEmployee.personalPercentage()).isEqualTo(expectedEmployee.personalPercentage());
    }

    @Test
    void EmployeeRepository_GetEmployeeByEmail_ReturnsOptionalEmployee() {

        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        var optionalEmployee = employeeRepository.getEmployeeByEmail(expectedEmployee.email());

        assertThat(optionalEmployee).isPresent();

        var actualEmployee = optionalEmployee.get();

        assertThat(actualEmployee.id()).isEqualTo(expectedEmployee.id());
        assertThat(actualEmployee.firstname()).isEqualTo(expectedEmployee.firstname());
        assertThat(actualEmployee.surname()).isEqualTo(expectedEmployee.surname());
        assertThat(actualEmployee.dateOfBirth()).isEqualTo(expectedEmployee.dateOfBirth());
        assertThat(actualEmployee.email()).isEqualTo(expectedEmployee.email());
        assertThat(actualEmployee.phoneNumber()).isEqualTo(expectedEmployee.phoneNumber());
        assertThat(actualEmployee.positionId()).isEqualTo(expectedEmployee.positionId());
        assertThat(actualEmployee.storeId()).isEqualTo(expectedEmployee.storeId());
        assertThat(actualEmployee.personalPercentage()).isEqualTo(expectedEmployee.personalPercentage());
    }

    @Test
    void EmployeeRepository_CreateEmployee_ReturnsNewEmployeeId() {

        var newEmployee = createFakeEmployee();

        var newEmpId = employeeRepository.createEmployee(newEmployee);

        var optionalEmployee = employeeRepository.getEmployee(newEmpId);

        assertThat(optionalEmployee).isPresent();

        var actualEmployee = optionalEmployee.get();

        assertThat(actualEmployee.id()).isEqualTo(newEmpId);
        assertThat(actualEmployee.firstname()).isEqualTo(newEmployee.firstname());
        assertThat(actualEmployee.surname()).isEqualTo(newEmployee.surname());
        assertThat(actualEmployee.dateOfBirth()).isEqualTo(newEmployee.dateOfBirth());
        assertThat(actualEmployee.email()).isEqualTo(newEmployee.email());
        assertThat(actualEmployee.phoneNumber()).isEqualTo(newEmployee.phoneNumber());
        assertThat(actualEmployee.positionId()).isEqualTo(newEmployee.positionId());
        assertThat(actualEmployee.storeId()).isEqualTo(newEmployee.storeId());
        assertThat(actualEmployee.personalPercentage()).isEqualTo(newEmployee.personalPercentage());
    }

    @Test
    void EmployeeRepository_UpdateEmployee_ReturnsResult() {

        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        var newEmployee = new Employee(
                expectedEmployee.id(),
                "Updated firstname",
                "Updated surname",
                LocalDate.now().minusYears(rn.nextInt(50)),
                rn.nextInt(100000) + "@test.com",
                "+380" + rn.nextInt(1000000000),
                null,
                testPositionId,
                UUID.randomUUID(),
                (byte) rn.nextInt(100));

        employeeRepository.updateEmployee(newEmployee);

        var optionalEmployee = employeeRepository.getEmployee(expectedEmployee.id());

        assertThat(optionalEmployee).isPresent();

        var actualEmployee = optionalEmployee.get();

        assertThat(actualEmployee.id()).isEqualTo(expectedEmployee.id());
        assertThat(actualEmployee.firstname()).isEqualTo(newEmployee.firstname());
        assertThat(actualEmployee.surname()).isEqualTo(newEmployee.surname());
        assertThat(actualEmployee.dateOfBirth()).isEqualTo(newEmployee.dateOfBirth());
        assertThat(actualEmployee.email()).isEqualTo(newEmployee.email());
        assertThat(actualEmployee.phoneNumber()).isEqualTo(newEmployee.phoneNumber());
        assertThat(actualEmployee.positionId()).isEqualTo(newEmployee.positionId());
        assertThat(actualEmployee.storeId()).isEqualTo(newEmployee.storeId());
        assertThat(actualEmployee.personalPercentage()).isEqualTo(newEmployee.personalPercentage());
    }

    @Test
    void EmployeeRepository_DeleteEmployee_ReturnsResult() {

        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        employeeRepository.deleteEmployee(expectedEmployee.id());

        var optionalEmployee = employeeRepository.getEmployee(expectedEmployee.id());

        assertThat(optionalEmployee).isEmpty();
    }

    @Test
    void EmployeeRepository_HasEmployeeWithNumber_ReturnsTrue() {
        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        var result = employeeRepository.hasEmployeeWithNumber(expectedEmployee.phoneNumber());

        assertThat(result).isTrue();
    }

    @Test
    void EmployeeRepository_GetEmployeesByStoreId_ReturnsEmployees() {
        var expectedEmployee = testEmployee.get(rn.nextInt(testEmployee.size()));

        var employees = employeeRepository.getEmployeesByStoreId(expectedEmployee.storeId());

        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(1);
    }

    @Test
    void EmployeeRepository_GetEmployeeCount_ReturnsCount() {
        var count = employeeRepository.getEmployeeCount();

        assertThat(count).isEqualTo(testEmployee.size());
    }

    private Employee createFakeEmployee() {

        return new Employee(
                null,
                "Test firstname #" + rn.nextInt(1000),
                "Test surname #" + rn.nextInt(1000),
                LocalDate.now().minusYears(rn.nextInt(50)),
                rn.nextInt(1000000000) + "@test.com",
                "+380" + rn.nextInt(1000000000),
                null,
                testPositionId, UUID.randomUUID(),
                (byte) rn.nextInt(100));
    }
}
