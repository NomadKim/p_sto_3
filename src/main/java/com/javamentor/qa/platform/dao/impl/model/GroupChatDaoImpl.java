package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.GroupChatDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class GroupChatDaoImpl extends ReadWriteDaoImpl<GroupChat, Long> implements GroupChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GroupChat> getGroupChatWithUsersById(Long id) {
        TypedQuery<GroupChat> query = entityManager.createQuery("select gch from GroupChat gch left join fetch gch.users" +
                " where gch.id = :id", GroupChat.class).setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public boolean isUsersChat(Long chatId, Long userId) {
        return (boolean) entityManager.createQuery("select count(gc.id) > 0 from GroupChat gc " +
                        "join gc.users u on u.id = :uid where gc.id = :chatId")
                .setParameter("chatId", chatId)
                .setParameter("uid", userId).getSingleResult();
    }
}
