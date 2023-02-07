package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserChatPinDao;
import com.javamentor.qa.platform.models.entity.user.UserChatPin;
import org.springframework.stereotype.Repository;

@Repository
public class UserChatPinDaoImpl extends ReadWriteDaoImpl<UserChatPin, Long> implements UserChatPinDao {


}
