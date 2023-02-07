package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupChatServiceImpl extends ReadWriteServiceImpl<GroupChat, Long> implements GroupChatService {

    private final GroupChatDao groupChatDao;

    @Autowired
    public GroupChatServiceImpl(GroupChatDao groupChatDao) {
        super(groupChatDao);
        this.groupChatDao = groupChatDao;
    }

    @Override
    public Optional<GroupChat> getGroupChatWithUsersById(Long id) {
        return groupChatDao.getGroupChatWithUsersById(id);
    }

    @Override
    @Transactional
    public void deleteChatFromUser(Long id, User user) {

        Optional<GroupChat> groupChat = getById(id);

        if (!groupChatDao.isUsersChat(id, user.getId())){
            throw new BadCredentialsException("Чат не принадлежит текущему пользователю");
        }

        if(groupChat.isPresent()) {
            groupChat.get().getUsers().remove(user);
            update(groupChat.get());
        }
    }

}
