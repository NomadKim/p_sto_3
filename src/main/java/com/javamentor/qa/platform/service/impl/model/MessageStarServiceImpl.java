package com.javamentor.qa.platform.service.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MessageStarServiceImpl extends ReadWriteServiceImpl<MessageStar, Long> implements MessageStarService{

    private MessageStarDao messageStarDao;


    @Autowired
    public MessageStarServiceImpl(MessageStarDao messageStarDao) {
        super(messageStarDao);
        this.messageStarDao = messageStarDao;
    }
    @Override
    public boolean isUserHasNoMoreThanThreeMessageStar(long userId) {
        return messageStarDao.isUserHasNoMoreThanThreeMessageStar(userId);
    }
}
