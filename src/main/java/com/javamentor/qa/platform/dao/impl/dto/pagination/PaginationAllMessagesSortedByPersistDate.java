package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllMessagesSortedByPersistDate implements PageDtoDao<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");
        Long chatId = (Long) params.get("chatId");
        Boolean sortAscendingFlag = (Boolean) params.get("sortAscendingFlag");
        String sql = "ORDER BY m.persistDate ";

        if (sortAscendingFlag) {
            sql = sql + "asc";
        } else {
            sql = sql + "desc";
        }

        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.MessageDto" +
                                "(m.id," +
                                "m.message, " +
                                "m.userSender.nickname, " +
                                "m.userSender.id, " +
                                "m.userSender.imageLink, " +
                                "m.persistDate)" +
                                "FROM Message m " +
                                "JOIN User u ON (m.userSender.id = u.id) " +
                                "WHERE m.chat.id = :chatId " + sql
                        , MessageDto.class)
                .setParameter("chatId", chatId)
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                        ("Select CAST(count(message.id) as int) AS countMessages from Message message WHERE message.chat.id = :chatId")
                .setParameter("chatId", params.get("chatId"));
        return (int) queryTotal.getSingleResult();
    }
}
