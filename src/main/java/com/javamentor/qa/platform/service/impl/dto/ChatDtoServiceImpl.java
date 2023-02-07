package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatDtoServiceImpl implements ChatDtoService {

    private final ChatDtoDao chatDtoDao;

    public ChatDtoServiceImpl(ChatDtoDao chatDtoDao) {
        this.chatDtoDao = chatDtoDao;
    }

    @Override
    public List<ChatDto> getChatsByString(Long userId, String searchedString) {
        return chatDtoDao.getChatByString(userId, searchedString)
                .stream()
                .sorted(Comparator.comparing(ChatDto::getIsChatPin, Comparator.reverseOrder())
                        .thenComparing(ChatDto::getPersistDateTimeLastMessage, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
