package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationCommentsQuestion implements PageDtoDao<CommentDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentDto> getItems(Map<String, Object> params) {
        int itemsOnPage = (int) params.get("itemsOnPage");
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                                "(c.id, c.text, c.user.id, c.user.fullName, SUM(r.count), c.persistDateTime)" +
                                "FROM CommentQuestion cq " +
                                "JOIN Comment c ON cq.comment.id = c.id " +
                                "JOIN Reputation r ON c.user.id = r.author.id " +
                                "WHERE cq.question.id = :questionId " +
                                "GROUP BY c.id, c.user.fullName", CommentDto.class)
                .setParameter("questionId", params.get("questionId"))
                .setFirstResult(((int) params.get("currentPageNumber") - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery("SELECT COUNT(cq) FROM CommentQuestion cq WHERE cq.question.id = :questionId ")
                .setParameter("questionId", params.get("questionId"));
        return ((Long) queryTotal.getSingleResult()).intValue();
    }
}
