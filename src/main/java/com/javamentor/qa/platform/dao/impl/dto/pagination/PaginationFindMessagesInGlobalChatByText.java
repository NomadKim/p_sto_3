package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.MessageDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationFindMessagesInGlobalChatByText implements PageDtoDao<MessageDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MessageDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.MessageDto" +
                                "(m.id, m.message, m.userSender.nickname, m.userSender.id, m.userSender.imageLink, m.persistDate) " +
                                "FROM Chat c " +
                                "JOIN GroupChat gc ON c.id = gc.chat.id " +
                                "JOIN Message m ON c.id = m.chat.id " +
                                "WHERE gc.isGlobal = true AND LOWER(m.message) LIKE LOWER(CONCAT('%', :text, '%'))"
                        , MessageDto.class)
                .setParameter("text", params.get("text"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return (int) entityManager.createQuery(
                        "SELECT CAST(count(m) as int) " +
                                "FROM Chat c " +
                                "JOIN GroupChat gc ON c.id = gc.chat.id " +
                                "JOIN Message m ON c.id = m.chat.id " +
                                "WHERE gc.isGlobal = true AND LOWER(m.message) LIKE LOWER(CONCAT('%', :text, '%'))")
                .setParameter("text", params.get("text"))
                .getSingleResult();
    }
}
