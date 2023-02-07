package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.Chat;

public interface ChatService extends ReadOnlyService<Chat, Long> {
    boolean isChatHasUser(Long chatId, Long userId);
}
