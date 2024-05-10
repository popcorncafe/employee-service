package com.popcorncafe.employeeservice.repository;

import com.popcorncafe.employeeservice.repository.model.Employee;
import com.popcorncafe.employeeservice.service.dto.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

    List<Employee> getEmployees(Page page);

    Optional<Employee> getEmployee(UUID id);

    Optional<Employee> getEmployeeByEmail(String email);

    UUID createEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(UUID id);

    boolean hasEmployeeWithNumber(String phoneNumber);

    List<Employee> getEmployeesByStoreId(UUID id);

    int getEmployeeCount();
}
