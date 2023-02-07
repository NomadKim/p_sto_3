package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationSearchByChatMessage implements PageDtoDao<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int items = (int) params.get("itemsOnPage");

        return entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.MessageDto(" +
                        "m.id, m.message, u.nickname, u.id, u.imageLink, m.persistDate) " +
                        "FROM Message m " +
                        "INNER JOIN User u ON m.userSender.id = u.id " +
                        "WHERE m.message LIKE CONCAT('%', :word, '%') " +
                        "AND m.chat.id = :chatId", MessageDto.class)
                .setParameter("word", params.get("word"))
                .setParameter("chatId", params.get("chatId"))
                .setFirstResult((page - 1) * items)
                .setMaxResults(items)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return (int) entityManager.createQuery(
                "SELECT CAST(count(m) as int) " +
                        "FROM Message m " +
                        "WHERE m.chat.id = :chatId " +
                        "AND m.message LIKE CONCAT('%', :word, '%')")
                .setParameter("word", params.get("word"))
                .setParameter("chatId", params.get("chatId"))
                .getSingleResult();
    }
}
