package com.javamentor.qa.platform.dao.abstracts.dto;

import java.util.List;
import java.util.Map;

public interface PageDtoDao<T> {

    List<T> getItems(Map<String, Object> params);

    int getTotalResultCount(Map<String, Object> params);
}
