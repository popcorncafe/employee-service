package io.github.artemnefedov.service;

import io.github.artemnefedov.entity.Employee;
import io.github.artemnefedov.entity.Position;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    ResponseEntity<List<Employee>> getAllEmployees();

    ResponseEntity<Employee> getEmployeeById(UUID id);

    ResponseEntity<Employee> getEmployeeByEmail(String email);

    ResponseEntity<Boolean> addEmployee(Employee employee);

    ResponseEntity<Boolean> updateEmployee(Employee employee);

    ResponseEntity<Boolean> deleteEmployee(UUID id);

    ResponseEntity<List<Position>> getAllPositions();

    ResponseEntity<Position> getPositionById(UUID id);

    ResponseEntity<Boolean> addPosition(Position position);

    ResponseEntity<Boolean> updatePosition(Position position);

    ResponseEntity<Boolean> deletePosition(UUID id);

    ResponseEntity<Boolean> hasEmployeeWithThisNumber(String phoneNumber);

    ResponseEntity<List<Employee>> getEmployeesByStoreId(UUID id);
}
