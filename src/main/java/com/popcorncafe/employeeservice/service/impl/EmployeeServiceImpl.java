package com.popcorncafe.employeeservice.service.impl;

import com.popcorncafe.employeeservice.repository.EmployeeRepository;
import com.popcorncafe.employeeservice.service.EmployeeService;
import com.popcorncafe.employeeservice.service.dto.EmployeeDto;
import com.popcorncafe.employeeservice.service.dto.Page;
import com.popcorncafe.employeeservice.service.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = employeeMapper;
    }


    @Override
    public List<EmployeeDto> getEmployees(Page page) {
        return employeeRepository.getEmployees(page).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<EmployeeDto> getEmployeesByStoreId(UUID id) {
        return employeeRepository.getEmployeesByStoreId(id).stream().map(mapper::toDto).toList();
    }

    @Override
    public EmployeeDto getEmployee(UUID id) {
        return employeeRepository.getEmployee(id).map(mapper::toDto).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public EmployeeDto getEmployeeByEmail(String email) {
        return employeeRepository.getEmployeeByEmail(email).map(mapper::toDto).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public boolean hasEmployeeWithNumber(String phoneNumber) {
        return employeeRepository.hasEmployeeWithNumber(phoneNumber);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        var employeeId = employeeRepository.createEmployee(mapper.toModel(employeeDto));
        return new EmployeeDto(
                employeeId,
                employeeDto.firstname(),
                employeeDto.surname(),
                employeeDto.dateOfBirth(),
                employeeDto.email(),
                employeeDto.phoneNumber(),
                Instant.now(),
                employeeDto.positionId(),
                employeeDto.storeId(),
                employeeDto.personalPercentage()
        );
    }

    @Override
    public boolean updateEmployee(EmployeeDto employeeDto) {
        return employeeRepository.updateEmployee(mapper.toModel(employeeDto));
    }

    @Override
    public boolean deleteEmployee(UUID id) {
        return employeeRepository.deleteEmployee(id);
    }

    @Override
    public int getEmployeeCount() {
        return employeeRepository.getEmployeeCount();
    }
}
