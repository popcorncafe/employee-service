package com.popcorncafe.employeeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorncafe.employeeservice.service.dto.EmployeeDto;
import com.popcorncafe.employeeservice.service.dto.Page;
import com.popcorncafe.employeeservice.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private final Random rn = new Random();
    private final List<EmployeeDto> employees = new ArrayList<>();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeServiceImpl employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private EmployeeDto employee;

    @BeforeEach
    void setUp() {

        for (int i = 0; i < rn.nextInt(100); i++) {
            employees.add(createFakeEmployee());
        }
    }

    private EmployeeDto createFakeEmployee() {
        return new EmployeeDto(
                UUID.randomUUID(),
                "Test firstname #" + rn.nextInt(1000),
                "Test surname #" + rn.nextInt(1000),
                LocalDate.ofEpochDay(rn.nextInt(1000)),
                "test" + rn.nextInt(1000) + "@test.com",
                "0" + rn.nextInt(1000000000),
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Byte.parseByte(rn.nextInt(100) + "")
        );
    }

    @Test
    void EmployeeController_GetEmployeeCount_ReturnsEmployeeCount() throws Exception {

        when(employeeService.getEmployeeCount()).thenReturn(employees.size());

        ResultActions result = mockMvc.perform(get("/count"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(employees.size())));
    }

    @Test
    void EmployeeController_GetAllEmployees_ReturnsListOfEmployeeDtos() throws Exception {
        int size = employees.size();
        int num = 0;

        when(employeeService.getEmployees(new Page(size, num))).thenReturn(employees);

        ResultActions result = mockMvc.perform(get("/{size}/{num}", size, num)
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(employees)));
    }

    @Test
    void EmployeeController_GetEmployeesByStoreId_ReturnsListOfEmployeeDtos() throws Exception {
        UUID storeId = UUID.randomUUID();

        List<EmployeeDto> storeEmployees = new ArrayList<>();

        for (int i = 0; i < rn.nextInt(100); i++) {
            var emp = new EmployeeDto(
                    UUID.randomUUID(),
                    "Test firstname #" + rn.nextInt(1000),
                    "Test surname #" + rn.nextInt(1000),
                    LocalDate.ofEpochDay(rn.nextInt(1000)),
                    "test" + rn.nextInt(1000) + "@test.com",
                    "0" + rn.nextInt(1000000000),
                    Instant.now(),
                    storeId,
                    UUID.randomUUID(),
                    Byte.parseByte(rn.nextInt(100) + "")
            );

            storeEmployees.add(emp);
        }

        when(employeeService.getEmployeesByStoreId(storeId)).thenReturn(storeEmployees);

        ResultActions result = mockMvc.perform(get("/stores/{id}", storeId)
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(storeEmployees)));

    }

    @Test
    void EmployeeController_GetEmployeeById_ReturnsEmployeeDto() throws Exception {

        when(employeeService.getEmployee(employee.id())).thenReturn(employee);

        ResultActions result = mockMvc.perform(get("/{id}", employee.id())
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    void EmployeeController_GetEmployeeByEmail_ReturnsEmployeeDto() throws Exception {

        when(employeeService.getEmployeeByEmail(employee.email())).thenReturn(employee);

        ResultActions result = mockMvc.perform(get("/email/{email}", employee.email())
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    void EmployeeController_HasEmployeeWithThisNumber_ReturnsBoolean() throws Exception {

        when(employeeService.hasEmployeeWithNumber(employee.phoneNumber())).thenReturn(true);

        ResultActions result = mockMvc.perform(get("/phones/{phoneNumber}", employee.phoneNumber())
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void EmployeeController_CreateEmployee_ReturnsEmployeeDto() throws Exception {

        when(employeeService.createEmployee(employee)).thenReturn(employee);

        ResultActions result = mockMvc.perform(post("/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(employee)));
    }

    @Test
    void EmployeeController_UpdateEmployee_ReturnsBoolean() throws Exception {

        when(employeeService.updateEmployee(employee)).thenReturn(true);

        ResultActions result = mockMvc.perform(put("/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void EmployeeController_DeleteEmployee_ReturnsBoolean() throws Exception {
        UUID id = UUID.randomUUID();

        when(employeeService.deleteEmployee(id)).thenReturn(true);

        ResultActions result = mockMvc.perform(delete("/{id}", id)
                .contentType("application/json"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
}
