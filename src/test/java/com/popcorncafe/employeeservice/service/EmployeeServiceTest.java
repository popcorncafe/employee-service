package com.popcorncafe.employeeservice.service;

import com.popcorncafe.employeeservice.repository.EmployeeRepository;
import com.popcorncafe.employeeservice.repository.model.Employee;
import com.popcorncafe.employeeservice.service.dto.EmployeeDto;
import com.popcorncafe.employeeservice.service.dto.Page;
import com.popcorncafe.employeeservice.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Random rn = new Random();

    @Test
    void EmployeeService_GetEmployees_ReturnsListOfEmployeeDtos() {
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            employees.add(createFakeEmployee());
        }

        Mockito.when(employeeRepository.getEmployees(Mockito.any(Page.class)))
                .thenReturn(employees);

        var result = employeeService.getEmployees(new Page(employees.size(), 0));

        assertThat(result.size()).isEqualTo(employees.size());
    }

    @Test
    void EmployeeService_GetEmployeesByStoreId_ReturnsListOfEmployeeDtos() {
        List<Employee> employees = new ArrayList<>();

        var testStoreId = UUID.randomUUID();

        for (int i = 0; i < 100; i++) {

            if (rn.nextBoolean()) {
                employees.add(createFakeEmployee());
            } else {
                employees.add(new Employee(
                        UUID.randomUUID(),
                        "Test Firstname" + rn.nextInt(1000),
                        "Test Surname" + rn.nextInt(1000),
                        LocalDate.now(),
                        rn.nextInt(10000000) + "@test.com",
                        "+342" + rn.nextInt(10000000),
                        Instant.now(),
                        UUID.randomUUID(),
                        testStoreId,
                        (byte) rn.nextInt(100)));
            }
        }

        Mockito.when(employeeRepository.getEmployeesByStoreId(testStoreId))
            .thenReturn(
                employees.stream()
                .filter(e -> e.storeId().equals(testStoreId))
                .toList());

        var result = employeeService.getEmployeesByStoreId(testStoreId);

        assertThat(result.size())
            .isEqualTo(employees.stream()
                .filter(e -> e.storeId().equals(testStoreId))
                .count()
            );
    }

    @Test
    void EmployeeService_GetEmployee_ReturnsEmployeeDto() {
        var employee = createFakeEmployee();

        Optional.of(employee);

        Mockito.when(employeeRepository.getEmployee(employee.id())).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.getEmployee(employee.id());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(employee.id());
        assertThat(result.firstname()).isEqualTo(employee.firstname());
        assertThat(result.surname()).isEqualTo(employee.surname());
        assertThat(result.dateOfBirth()).isEqualTo(employee.dateOfBirth());
        assertThat(result.email()).isEqualTo(employee.email());
        assertThat(result.phoneNumber()).isEqualTo(employee.phoneNumber());
        assertThat(result.registerDate()).isEqualTo(employee.registerDate());
        assertThat(result.positionId()).isEqualTo(employee.positionId());
        assertThat(result.storeId()).isEqualTo(employee.storeId());
        assertThat(result.personalPercentage()).isEqualTo(employee.personalPercentage());
    }

    @Test
    void EmployeeService_GetEmployeeByEmail_ReturnsEmployeeDto() {
        var employee = createFakeEmployee();

        Optional.of(employee);

        Mockito.when(employeeRepository.getEmployeeByEmail(employee.email())).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.getEmployeeByEmail(employee.email());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(employee.id());
        assertThat(result.firstname()).isEqualTo(employee.firstname());
        assertThat(result.surname()).isEqualTo(employee.surname());
        assertThat(result.dateOfBirth()).isEqualTo(employee.dateOfBirth());
        assertThat(result.email()).isEqualTo(employee.email());
        assertThat(result.phoneNumber()).isEqualTo(employee.phoneNumber());
        assertThat(result.registerDate()).isEqualTo(employee.registerDate());
        assertThat(result.positionId()).isEqualTo(employee.positionId());
        assertThat(result.storeId()).isEqualTo(employee.storeId());
        assertThat(result.personalPercentage()).isEqualTo(employee.personalPercentage());
    }

    @Test
    void EmployeeService_HasEmployeeWithNumber_ReturnsBoolean() {

        String phoneNumber = "+342" + rn.nextInt(10000000);

        Mockito.when(employeeRepository.hasEmployeeWithNumber(phoneNumber)).thenReturn(true);

        boolean result = employeeService.hasEmployeeWithNumber(phoneNumber);

        assertThat(result).isTrue();
    }

    @Test
    void EmployeeService_CreateEmployee_ReturnsEmployeeDto() {
        var employee = new Employee(
                UUID.randomUUID(),
                "Test Firstname" + rn.nextInt(1000),
                "Test Surname" + rn.nextInt(1000),
                LocalDate.now(),
                rn.nextInt(10000000) + "@test.com",
                "+342" + rn.nextInt(10000000),
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                (byte) rn.nextInt(100)
        );

        var employeeDto = new EmployeeDto(
                employee.id(),
                employee.firstname(),
                employee.surname(),
                employee.dateOfBirth(),
                employee.email(),
                employee.phoneNumber(),
                employee.registerDate(),
                employee.positionId(),
                employee.storeId(),
                employee.personalPercentage()
        );

        Mockito.when(employeeRepository.createEmployee(employee)).thenReturn(employee.id());

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(employee.id());
        assertThat(result.firstname()).isEqualTo(employee.firstname());
        assertThat(result.surname()).isEqualTo(employee.surname());
        assertThat(result.dateOfBirth()).isEqualTo(employee.dateOfBirth());
        assertThat(result.email()).isEqualTo(employee.email());
        assertThat(result.phoneNumber()).isEqualTo(employee.phoneNumber());
        assertThat(result.positionId()).isEqualTo(employee.positionId());
        assertThat(result.storeId()).isEqualTo(employee.storeId());
        assertThat(result.personalPercentage()).isEqualTo(employee.personalPercentage());
    }

    @Test
    void EmployeeService_UpdateEmplotee_ReturnsResult() {
        var employee = new EmployeeDto(
                UUID.randomUUID(),
                "Test Firstname" + rn.nextInt(1000),
                "Test Surname" + rn.nextInt(1000),
                LocalDate.now(),
                rn.nextInt(10000000) + "@test.com",
                "+342" + rn.nextInt(10000000),
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                (byte) rn.nextInt(100)
        );

        Mockito.when(employeeRepository.updateEmployee(Mockito.any(Employee.class)))
            .thenReturn(true);

        boolean result = employeeService.updateEmployee(employee);

        assertThat(result).isTrue();
    }

    @Test
    void EmployeeService_DeleteEmployee_ReturnsResult() {
        var employee = createFakeEmployee();

        Mockito.when(employeeRepository.deleteEmployee(employee.id()))
            .thenReturn(true);

        boolean result = employeeService.deleteEmployee(employee.id());

        assertThat(result).isTrue();
    }


    @Test
    void EmployeeService_GetEmployeeCount_ReturnsCount() {
        Mockito.when(employeeRepository.getEmployeeCount())
            .thenReturn(100);

        int result = employeeService.getEmployeeCount();

        assertThat(result).isEqualTo(100);
    }

    private Employee createFakeEmployee() {
        return new Employee(
                UUID.randomUUID(),
                "Test Firstname" + rn.nextInt(1000),
                "Test Surname" + rn.nextInt(1000),
                LocalDate.now(),
                rn.nextInt(10000000) + "@test.com",
                "+342" + rn.nextInt(10000000),
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                (byte) rn.nextInt(100));
    }
}
