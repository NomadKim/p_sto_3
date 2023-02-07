package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.MessageStar;

public interface MessageStarService extends ReadWriteService<MessageStar, Long>{
    boolean isUserHasNoMoreThanThreeMessageStar(long userId);

}
