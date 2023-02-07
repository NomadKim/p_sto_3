package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface GroupChatService extends ReadWriteService<GroupChat, Long>{

    Optional<GroupChat> getGroupChatWithUsersById(Long id);

    void deleteChatFromUser(Long id, User user);

}
