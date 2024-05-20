package com.popcorncafe.employeeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorncafe.employeeservice.repository.model.Role;
import com.popcorncafe.employeeservice.service.dto.PositionDto;
import com.popcorncafe.employeeservice.service.impl.PositionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = PositionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PositionServiceImpl positionService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Random rn = new Random();

    private final List<PositionDto> positions = new ArrayList<>();
    private PositionDto position;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < rn.nextInt(100); i++) {
            positions.add(createFakePosition());
        }

        position = createFakePosition();
    }

    @Test
    void PositionController_GetPositions_ReturnsListOfPositionDtos() throws Exception {

        when(positionService.getPositions()).thenReturn(positions);

        ResultActions response = mockMvc.perform(get("/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positions)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(positions)));
    }

    @Test
    void PositionController_GetPosition_ReturnsPositionDto() throws Exception {
        given(positionService.getPosition(Mockito.any(UUID.class)))
                .willReturn(position);

        ResultActions response = mockMvc.perform(get("/positions/{id}", position.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(position)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(position)));
    }

    @Test
    void PositionController_CreatePosition_ReturnsPositionDto() throws Exception {
        given(positionService.createPosition(Mockito.any(PositionDto.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(position)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(position)));
    }

    @Test
    void PositionController_UpdatePosition_ReturnsResult() throws Exception {
        given(positionService.updatePosition(Mockito.any(PositionDto.class)))
                .willReturn(true);

        ResultActions response = mockMvc.perform(put("/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(position)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void PositionController_DeletePosition_ReturnsResult() throws Exception {

        given(positionService.deletePosition(Mockito.any(UUID.class)))
                .willReturn(true);

        ResultActions response = mockMvc.perform(delete("/positions/{id}", position.id())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    private PositionDto createFakePosition() {
        return new PositionDto(
                UUID.randomUUID(),
                "Test Position" + rn.nextInt(1000),
                rn.nextInt(1000),
                Arrays.stream(Role.values())
                        .map(Role::name)
                        .filter(role -> rn.nextBoolean()).toList()
        );
    }
}