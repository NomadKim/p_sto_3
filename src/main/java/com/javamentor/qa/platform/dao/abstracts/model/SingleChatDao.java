package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;

public interface SingleChatDao extends ReadWriteDao <SingleChat, Long> {

    void deleteChatFromUser(Long chatId, User user);

    boolean isUsersChat(Long chatId, Long userId);
}
