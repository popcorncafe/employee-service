package io.github.artemnefedov.service.implementation;

import io.github.artemnefedov.entity.Employee;
import io.github.artemnefedov.entity.Position;
import io.github.artemnefedov.exsception.ResourceNotFound;
import io.github.artemnefedov.repository.EmployeeRepository;
import io.github.artemnefedov.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.getAllEmployees());
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(UUID id) {
        return ResponseEntity.ok(employeeRepository.findEmployeeById(id)
                .orElseThrow(() -> new ResourceNotFound("Unable to find an employee with this id.")));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeByEmail(String email) {
        return ResponseEntity.ok(employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Unable to find an employee with this email.")));
    }

    @Override
    public ResponseEntity<Boolean> addEmployee(Employee employee) {
        return ResponseEntity.ok(employeeRepository.addEmployee(employee));
    }

    @Override
    public ResponseEntity<Boolean> updateEmployee(Employee employee) {
        return ResponseEntity.ok(employeeRepository.updateEmployee(employee));
    }

    @Override
    public ResponseEntity<Boolean> deleteEmployee(UUID id) {
        return ResponseEntity.ok(employeeRepository.deleteEmployee(id));
    }

    @Override
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(employeeRepository.getAllPositions());
    }

    @Override
    public ResponseEntity<Position> getPositionById(UUID id) {
        return ResponseEntity.ok(employeeRepository.getPositionById(id)
                .orElseThrow(() -> new ResourceNotFound("Unable to find position with this id.")));
    }

    @Override
    public ResponseEntity<Boolean> addPosition(Position position) {
        return ResponseEntity.ok(employeeRepository.addPosition(position));
    }

    @Override
    public ResponseEntity<Boolean> updatePosition(Position position) {
        return ResponseEntity.ok(employeeRepository.updatePosition(position));
    }

    @Override
    public ResponseEntity<Boolean> deletePosition(UUID id) {
        return ResponseEntity.ok(employeeRepository.deletePosition(id));
    }

    @Override
    public ResponseEntity<Boolean> hasEmployeeWithThisNumber(String phoneNumber) {
        return ResponseEntity.ok(employeeRepository.hasEmployeeWithThisNumber(phoneNumber));
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByStoreId(UUID id) {
        return ResponseEntity.ok(employeeRepository.getEmployeesByStoreId(id));
    }
}
