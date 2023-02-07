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
public class PaginationAllUsersSortedByReputation implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<UserDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");
        Query query = entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.UserDto (" +
                        "u.id, " +
                        "u.email, " +
                        "u.fullName, " +
                        "u.imageLink, " +
                        "u.city, " +
                        "coalesce(CAST(sum(rep.count) as integer), 0)) " +
                        "from User u left join Reputation rep on u.id = rep.author.id " +
                        " WHERE u.email like concat('%', :filter, '%') " +
                        " or u.fullName like concat('%', :filter, '%') " +
                        "group by u.id,rep.author.id " +
                        "order by sum(rep.count) desc, u.id", UserDto.class)
                        .setParameter("filter", params.get("filter"));
        query.setFirstResult((page -1) * itemsOnPage);
        query.setMaxResults(itemsOnPage);
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