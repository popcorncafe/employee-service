package com.popcorncafe.employeeservice.controller;

import com.popcorncafe.employeeservice.service.dto.MessageResponse;
import com.popcorncafe.employeeservice.exsception.ResourceNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<MessageResponse> resourceNotFoundException(ResourceNotFound e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Internal error"));
    }
}
