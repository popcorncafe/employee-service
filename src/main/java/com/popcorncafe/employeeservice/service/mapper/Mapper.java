package com.popcorncafe.employeeservice.service.mapper;

import com.popcorncafe.employeeservice.repository.model.Model;
import com.popcorncafe.employeeservice.service.dto.Dto;

public interface Mapper <T extends Model, R extends Dto> {

    T toModel(R dto);
    R toDto(T model);
}

