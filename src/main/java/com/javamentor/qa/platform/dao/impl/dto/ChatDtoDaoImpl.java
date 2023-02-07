package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class ChatDtoDaoImpl implements ChatDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ChatDto> getChatByString(Long userId, String searchedString) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.ChatDto " +
                        "(ch.id, "+
                        "case when ch.chatType = :chatTypeGroup then gc.title else " +
                                "case when sc.userOne.id = :userId then (select u.fullName from User as u WHERE u.id = sc.useTwo.id) " +
                                "else (select u.fullName from User as u WHERE u.id = sc.userOne.id) end " +
                        "end, "+
                        "case " +
                            "when ch.chatType = :chatTypeSingle then u.imageLink " +
                            "else " +
                            "(select gc.imageChat from GroupChat as gc where gc.id = ch.id)" +
                        "end , "+
                        "m.message, " +
                        "case when exists(" +
                            "select ucp " +
                            "from UserChatPin ucp " +
                            "where ucp.chat.id = ch.id and ucp.user.id = :userId) " +
                            "then true else false end " +
                        ", m.persistDate) " +
                        "from Chat as ch " +
                        "join Message as m  on ch.id = m.chat.id " +
                        "left join GroupChat as gc on m.chat.id = gc.chat.id " +
                        "left join SingleChat as sc on m.chat.id = sc.chat.id " +
                        "left join User as u on m.userSender.id = u.id "+
                        "where " +
                        "(upper(gc.title) like upper(:searchString) " +
                        "or " +
                        "(ch.id in(select sc.chat.id from SingleChat sc where " +
                        "case " +
                        "when  sc.userOne.id = :userId " +
                        "then sc.isDeleteOne " +
                        "else sc.isDeleteTwo end = false " +
                        "and " +
                        "case " +
                            "when sc.userOne.id = :userId " +
                            "then upper(sc.userOne.fullName) " +
                            "else upper(sc.useTwo.fullName) end like upper(:searchString)))) " +
                        "and " +
                        "((sc.userOne = :userId or sc.useTwo = :userId)" +
                        "or " +
                        "ch.id in (select g.id from GroupChat as g " +
                            "join g.users as ghu on ghu.id = :userId)) " +
                        "and " +
                        "m.persistDate = (select max(mes.persistDate) from Message as mes where mes.chat.id = ch.id) "
                        , ChatDto.class)
                .setParameter("searchString", "%" + searchedString + "%")
                .setParameter("userId", userId)
                .setParameter("chatTypeSingle", ChatType.SINGLE)
                .setParameter("chatTypeGroup", ChatType.GROUP)
                .getResultList();
    }
}
