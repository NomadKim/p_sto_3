package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationAllUsersSortingByPersistDate implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<String, Object> params) {
        Query query = entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.UserDto" +
                " (user.id,user.email,user.fullName," +
                " user.imageLink,user.city," +
                " (select CAST(COALESCE(sum(reputation.count), 0) as int) from Reputation reputation where reputation.author = user)) " +
                " from User user" +
                " WHERE user.email like concat('%', :filter, '%') " +
                " or user.fullName like concat('%', :filter, '%') " +
                " order by user.persistDateTime desc", UserDto.class)
                .setParameter("filter", params.get("filter"));
        query.setFirstResult(((int) params.get("currentPageNumber") - 1) * (int) params.get("itemsOnPage"));
        query.setMaxResults((int) params.get("itemsOnPage"));
        return query.getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        Query queryTotal = entityManager.createQuery
                ("Select CAST(count(user.id) as int) AS countUsers from User user" +
                    " WHERE user.email like concat('%', :filter, '%') " +
                    " or user.fullName like concat('%', :filter, '%') ")
                .setParameter("filter", params.get("filter"));
        return (int) queryTotal.getSingleResult();
    }
}
