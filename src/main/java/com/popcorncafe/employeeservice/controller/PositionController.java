package com.popcorncafe.employeeservice.controller;

import com.popcorncafe.employeeservice.service.PositionService;
import com.popcorncafe.employeeservice.service.dto.PositionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ResponseEntity<List<PositionDto>> getPositions() {
        return ResponseEntity.ok(positionService.getPositions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDto> getPosition(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(positionService.getPosition(id));
    }

    @PostMapping
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionDto positionDto) {
        System.out.println(positionDto.toString());
        return ResponseEntity.ok(positionService.createPosition(positionDto));
    }

    @PutMapping
    public ResponseEntity<Boolean> updatePosition(@RequestBody PositionDto positionDto) {
        return ResponseEntity.ok(positionService.updatePosition(positionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePosition(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(positionService.deletePosition(id));
    }
}
