package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;

import java.util.List;
import java.util.Map;


public interface ChatDtoDao {
    List<ChatDto> getChatByString (Long userId, String searchedString);
}
