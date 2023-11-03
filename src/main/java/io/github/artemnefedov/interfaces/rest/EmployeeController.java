package io.github.artemnefedov.interfaces.rest;

import io.github.artemnefedov.entity.Employee;
import io.github.artemnefedov.entity.Position;
import io.github.artemnefedov.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") UUID id) {

        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable("email") String email) {

        return employeeService.getEmployeeByEmail(email);
    }

    @PutMapping
    public ResponseEntity<Boolean> addEmployee(@RequestBody Employee employee) {

        return employeeService.addEmployee(employee);
    }

    @PatchMapping
    public ResponseEntity<Boolean> updateEmployee(@RequestBody Employee employee) {

        return employeeService.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable("id") UUID id) {

        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/positions")
    public ResponseEntity<List<Position>> getAllPositions() {

        return employeeService.getAllPositions();
    }

    @GetMapping("/positions/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable("id") UUID id) {

        return employeeService.getPositionById(id);
    }

    @PutMapping("/positions")
    public ResponseEntity<Boolean> addPosition(@RequestBody Position position) {

        return employeeService.addPosition(position);
    }

    @PatchMapping("/positions")
    public ResponseEntity<Boolean> updatePosition(@RequestBody Position position) {

        return employeeService.updatePosition(position);
    }

    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Boolean> deletePosition(@PathVariable("id") UUID id) {

        return employeeService.deletePosition(id);
    }


    @GetMapping("/phones/{phoneNumber}")
    ResponseEntity<Boolean> hasEmployeeWithThisNumber(@PathVariable("phoneNumber") String phoneNumber) {

        return employeeService.hasEmployeeWithThisNumber(phoneNumber);
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<List<Employee>> getEmployeesByStoreId(@PathVariable("id") UUID id) {

        return employeeService.getEmployeesByStoreId(id);
    }
}
