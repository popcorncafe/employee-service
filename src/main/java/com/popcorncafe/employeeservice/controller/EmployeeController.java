package com.popcorncafe.employeeservice.controller;

import com.popcorncafe.employeeservice.service.EmployeeService;
import com.popcorncafe.employeeservice.service.dto.EmployeeDto;
import com.popcorncafe.employeeservice.service.dto.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getEmployeeCount() {
        return ResponseEntity.ok(employeeService.getEmployeeCount());
    }

    @GetMapping("/{size}/{num}")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(@PathVariable("size") int size, @PathVariable("num") int num) {
        return ResponseEntity.ok(employeeService.getEmployees(new Page(size, num)));
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByStoreId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(employeeService.getEmployeesByStoreId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @GetMapping("/phones/{phoneNumber}")
    ResponseEntity<Boolean> hasEmployeeWithThisNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(employeeService.hasEmployeeWithNumber(phoneNumber));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }
}
