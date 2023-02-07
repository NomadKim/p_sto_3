package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.SingleChatDao;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SingleChatDaoImpl extends ReadWriteDaoImpl<SingleChat, Long> implements SingleChatDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void deleteChatFromUser(Long chatId, User user){

        entityManager.createQuery("update SingleChat sc " +
                "set " +
                "sc.isDeleteTwo = " +
                "case when sc.useTwo.id = :userId then true else sc.isDeleteTwo end, " +
                "sc.isDeleteOne = " +
                "case when sc.userOne.id = :userId then true else sc.isDeleteOne end " +
                "where sc.id = :chatId")
                .setParameter("userId", user.getId())
                .setParameter("chatId", chatId)
                .executeUpdate();
    }

    @Override
    public boolean isUsersChat(Long chatId, Long userId) {
        return (boolean) entityManager.createQuery("select count(sc.id) > 0 from SingleChat sc " +
                        "where sc.id = :chatId and (sc.useTwo.id = :uid or sc.userOne.id = :uid)")
                .setParameter("uid", userId)
                .setParameter("chatId", chatId)
                .getSingleResult();
    }

}
