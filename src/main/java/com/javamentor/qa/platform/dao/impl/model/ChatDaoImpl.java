package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ChatDao;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDaoImpl extends ReadOnlyDaoImpl<Chat, Long> implements ChatDao {

}
