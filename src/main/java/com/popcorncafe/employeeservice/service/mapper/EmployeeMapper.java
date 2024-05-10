package com.popcorncafe.employeeservice.service.mapper;

import com.popcorncafe.employeeservice.repository.model.Employee;
import com.popcorncafe.employeeservice.service.dto.EmployeeDto;

public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {
    @Override
    public Employee toModel(EmployeeDto dto) {
        return new Employee(
                dto.id(),
                dto.firstname(),
                dto.surname(),
                dto.dateOfBirth(),
                dto.email(),
                dto.phoneNumber(),
                dto.registerDate(),
                dto.positionId(),
                dto.storeId(),
                dto.personalPercentage()
        );
    }

    @Override
    public EmployeeDto toDto(Employee model) {
        return new EmployeeDto(
                model.id(),
                model.firstname(),
                model.surname(),
                model.dateOfBirth(),
                model.email(),
                model.phoneNumber(),
                model.registerDate(),
                model.positionId(),
                model.storeId(),
                model.personalPercentage()
        );
    }
}
