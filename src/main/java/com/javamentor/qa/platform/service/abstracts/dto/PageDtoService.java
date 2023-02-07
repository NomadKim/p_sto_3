package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;

public interface PageDtoService<T> {
    PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params);
}
