package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserDtoResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllUsersSortedByVote implements PageDtoDao<UserDto> {

    @PersistenceContext
   private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> params) {
        Query query = entityManager.createQuery("select" +
                " user.id AS id, user.email AS email, user.fullName AS fullName," +
                " user.imageLink AS linkImage, user.city AS city," +
                " (select CAST(COALESCE(sum(reputation.count), 0) as int) from Reputation reputation where reputation.author.id = user.id) AS reputation," +
                " ((SELECT COALESCE(sum(case vQ.vote  when 'UP_VOTE' then 1 else -1 end), 0) FROM VoteQuestion vQ JOIN Question q ON vQ.question.id = q.id WHERE q.user.id = user.id) +" +
                " (SELECT COALESCE(sum(case vA.vote  when 'UP_VOTE' then 1 else -1 end), 0) FROM VoteAnswer vA JOIN Answer ans ON vA.answer.id = ans.id WHERE ans.user.id = user.id)) AS sum1" +
                " FROM User user " +
                " WHERE user.email like concat('%', :filter, '%') " +
                " or user.fullName like concat('%', :filter, '%') " +
                " ORDER BY sum1 DESC, user.id")
                .setParameter("filter", params.get("filter"))
                .unwrap(org.hibernate.query.Query.class).setResultTransformer(new UserDtoResultTransformer());
        query.setFirstResult(((int) params.get("currentPageNumber") - 1) * (int) params.get("itemsOnPage"));
        query.setMaxResults((int) params.get("itemsOnPage"));
        return query.getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                ("Select count(user.id) from User user" +
                " WHERE user.email like concat('%', :filter, '%') " +
                " or user.fullName like concat('%', :filter, '%') ")
                .setParameter("filter", params.get("filter"));
        return ((Long) queryTotal.getSingleResult()).intValue();
    }
}
