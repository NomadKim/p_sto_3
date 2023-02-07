package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ChatDao;
import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ReadOnlyServiceImpl<Chat, Long> implements ChatService {

    private GroupChatDao groupChatDao;
    private ChatDao chatDao;
    private SingleChatDao singleChatDao;


    @Autowired
    public ChatServiceImpl(ChatDao chatDao, GroupChatDao groupChatDao, SingleChatDao singleChatDao) {
        super(chatDao);
        this.chatDao = chatDao;
        this.singleChatDao = singleChatDao;
        this.groupChatDao = groupChatDao;
    }

    @Override
    public boolean isChatHasUser(Long chatId, Long userId) {
        return (singleChatDao.isUsersChat(chatId, userId) || groupChatDao.isUsersChat(chatId, userId));
    }

}
