package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationGroupChat implements PageDtoDao<GroupChatDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GroupChatDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.GroupChatDto" +
                                "(gc.chat.id, gc.title, " +
                                "m.id, m.message, m.userSender.nickname, m.userSender.id, m.userSender.imageLink, m.persistDate) " +
                                "FROM GroupChat gc " +
                                "JOIN gc.users gcu ON gcu.id = :userId " +
                                "LEFT JOIN Message m on m.id IN(SELECT mm.id FROM Message mm WHERE mm.persistDate = " +
                                "     (SELECT MAX(mmm.persistDate) FROM Message mmm WHERE mmm.chat.id = gc.id))"
                        , GroupChatDto.class)
                .setParameter("userId" , params.get("userId"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return (int) entityManager.createQuery(
                "SELECT CAST(count(gc) as int) " +
                        "FROM GroupChat gc " +
                        "JOIN gc.users gcu ON gcu.id = :userId")
                .setParameter("userId", params.get("userId"))
                .getSingleResult();
    }
}
