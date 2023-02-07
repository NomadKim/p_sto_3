package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;

public interface SingleChatService extends ReadWriteService<SingleChat, Long>{
    void addSingleChatAndMessage(SingleChat singleChat, String message);

    boolean isStatusDeleted(Long id);

    void deleteChatFromUser(Long id, User user);

}
