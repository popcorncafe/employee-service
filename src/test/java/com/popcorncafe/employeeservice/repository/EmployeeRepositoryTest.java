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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
class EmployeeRepositoryTest {

    private static final List<Employee> testEmployee = new ArrayList<>();
    private static final int TEST_EMPLOYEE_COUNT = 100;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16.0-alpine3.18");
    private static UUID testPositionId;

    @Autowired
    private EmployeeRepository employeeRepository;

//    @Autowired
//    private NamedParameterJdbcTemplate parameterJdbcTemplate;
//
//    @BeforeEach
//    void setUp() {
//
//        testPositionId = parameterJdbcTemplate.queryForObject("INSERT INTO position (name, salary, roles) VALUES (:name, :salary, :roles) RETURNING id", Map.of("name", "Test position", "salary", 123.4, "roles", new SqlParameterValue(ARRAY, new int[]{0})), UUID.class);
//
//        for (int i = 0; i <= TEST_EMPLOYEE_COUNT; i++) {
//
//            var employee = createFakeEmployee();
//
//            testEmployee.add(
//                    new Employee(
//                            parameterJdbcTemplate.queryForObject("""
//                                INSERT INTO employee (firstname, surname, date_of_birth, email, phone_number, position_id, store_id, personal_percentage)
//                                VALUES (:firstname, :surname, :date_of_birth, :email, :phone_number, :position_id, :store_id, :personal_percentage)
//                                RETURNING id;
//                            """,
//                                    Map.of(
//                                            "firstname", employee.firstname(),
//                                            "surname", employee.surname(),
//                                            "date_of_birth", employee.dateOfBirth(),
//                                            "email", employee.email(),
//                                            "phone_number", employee.phoneNumber(),
//                                            "position_id", testPositionId,
//                                            "store_id", employee.storeId(),
//                                            "personal_percentage", employee.personalPercentage()
//                                    ),
//                                    UUID.class
//                            ),
//                    employee.firstname(),
//                    employee.surname(),
//                    employee.dateOfBirth(),
//                    employee.email(),
//                    employee.phoneNumber(),
//                    Instant.now(),
//                    testPositionId,
//                    employee.storeId(),
//                    employee.personalPercentage())
//            );
//        }
//    }

//    @AfterEach
//    void clearData() {
//        parameterJdbcTemplate.update("DELETE FROM employee; DELETE FROM position;", Map.of());
//    }

    @Test
    void EmployeeRepository_GetEmployees_ReturnsAllEmployees() {
        List<Employee> employees = employeeRepository.getEmployees(new Page(testEmployee.size(), 0));

        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(testEmployee.size());
    }

    @Test
    void getEmployee() {

    }

    @Test
    void getEmployeeByEmail() {
    }

    @Test
    void createEmployee() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }

    @Test
    void hasEmployeeWithThisNumber() {
    }

    @Test
    void getEmployeesByStoreId() {
    }

    @Test
    void getEmployeeCount() {
    }


    private Employee createFakeEmployee() {
        var rn = new Random();
        return new Employee(null, "Test firstname #" + rn.nextInt(1000), "Test surname #" + rn.nextInt(1000), LocalDate.now().minusYears(rn.nextInt(50)), "test" + rn.nextInt(1000) + "@test.com", "+380" + rn.nextInt(1000000000), null, testPositionId, UUID.randomUUID(), (byte) rn.nextInt(100));
    }
}