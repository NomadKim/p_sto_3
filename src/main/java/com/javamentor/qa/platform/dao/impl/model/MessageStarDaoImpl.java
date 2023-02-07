package com.javamentor.qa.platform.dao.impl.model;


import com.javamentor.qa.platform.dao.abstracts.model.MessageStarDao;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Repository
public class MessageStarDaoImpl extends ReadWriteDaoImpl<MessageStar, Long> implements MessageStarDao {
    @PersistenceContext
    private EntityManager entityManager;

    //    проверяет не больше ли трех избранных сообщений у юзера
    @Override
    public boolean isUserHasNoMoreThanThreeMessageStar(long userId){


        Query query = entityManager.createQuery("select count(messageStar.user.id) from MessageStar messageStar where messageStar.user.id = :userId")
                .setParameter("userId", userId);


        return Integer.parseInt(String.valueOf(query.getSingleResult())) < 3;
    }
}
