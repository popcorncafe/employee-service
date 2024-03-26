package com.popcorncafe.employeeservice.service;

import com.popcorncafe.employeeservice.service.dto.EmployeeDto;
import com.popcorncafe.employeeservice.service.dto.Page;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeDto> getEmployees(Page page);

    List<EmployeeDto> getEmployeesByStoreId(UUID id);

    EmployeeDto getEmployee(UUID id);

    EmployeeDto getEmployeeByEmail(String email);

    boolean hasEmployeeWithNumber(String phoneNumber);

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    boolean updateEmployee(EmployeeDto employeeDto);

    boolean deleteEmployee(UUID id);

    int getEmployeeCount();
}
