package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDtoResultTransformer;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationSearchQuestionsSortedById implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> params) {

        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createNativeQuery(
                        "SELECT " +
                                "distinct q.id AS q_id, " +
                                "q.title, " +
                                "q.description, " +
                                "q.last_redaction_date, " +
                                "q.persist_date, " +
                                "u.id , " +
                                "u.full_name, " +
                                "u.image_link, " +

                                "(SELECT coalesce(sum(r.count),0) FROM reputation r " +
                                "   WHERE r.author_id = u.id) AS reputation, " +

                                "(SELECT coalesce(count(up.vote), 0) FROM votes_on_questions up " +
                                "   WHERE up.vote = 'UP_VOTE' AND up.question_id = q.id) " +
                                "- " +
                                "(SELECT coalesce(count(down.vote), 0) FROM votes_on_questions down " +
                                "   WHERE down.vote = 'DOWN_VOTE' AND down.question_id = q.id) AS votes, " +

                                "(SELECT coalesce(count(a.id),0) FROM answer a " +
                                "   WHERE a.question_id = q.id) AS answers, " +

                                "(SELECT count(qv.id) from question_viewed qv where qv.question_id = q.id), " +

                                "(SELECT coalesce(count(b.id), 0) FROM bookmarks b WHERE b.question_id = q.id AND b.user_id = :userId) " +

                                "FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "LEFT JOIN question_has_tag qht ON q.id = qht.question_id " +
                                " where q.title like concat ('%', :title, '%')" +
                                " and q.description like concat ('%', :body, '%')" +
                                " and u.full_name like concat ('%', :userName, '%')" +
                                " and (q.title like concat ('%', :request, '%')"+
                                " or q.description like concat ('%', :request, '%'))"+
                                "ORDER BY q.id ")
                        .setParameter("title", params.get("title"))
                        .setParameter("body", params.get("body"))
                        .setParameter("request", params.get("request"))
                        .setParameter("userName",params.get("user"))
                        .setParameter("userId", params.get("userId"))
                        .setFirstResult((page - 1) * itemsOnPage)
                        .setMaxResults(itemsOnPage)
                        .unwrap(org.hibernate.query.Query.class)
                        .setResultTransformer(new QuestionViewDtoResultTransformer())
                        .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        return ((BigInteger) entityManager.createNativeQuery(
                        "SELECT " +
                                "COUNT(DISTINCT q.id) FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "LEFT JOIN question_has_tag qht ON q.id = qht.question_id " +
                                " where q.title like concat ('%', :title, '%')" +
                                " and q.description like concat ('%', :body, '%')" +
                                " and u.full_name like concat ('%', :userName, '%')" +
                                " and (q.title like concat ('%', :request, '%')"+
                                " or q.description like concat ('%', :request, '%'))")
                .setParameter("title", params.get("title"))
                .setParameter("body", params.get("body"))
                .setParameter("request", params.get("request"))
                .setParameter("userName",params.get("user"))
                .getSingleResult()).intValue();
    }
}
