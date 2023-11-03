package io.github.artemnefedov.repository;

import io.github.artemnefedov.entity.Employee;
import io.github.artemnefedov.entity.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

    List<Employee> getAllEmployees();

    Optional<Employee> findEmployeeById(UUID id);

    Optional<Employee> findEmployeeByEmail(String email);

    boolean addEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(UUID id);

    List<Position> getAllPositions();

    Optional<Position> getPositionById(UUID id);

    boolean addPosition(Position position);

    boolean updatePosition(Position position);

    boolean deletePosition(UUID id);

    boolean hasEmployeeWithThisNumber(String phoneNumber);

    List<Employee> getEmployeesByStoreId(UUID id);
}
