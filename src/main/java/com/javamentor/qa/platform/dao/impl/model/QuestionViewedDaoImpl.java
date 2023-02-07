package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "isUserViewedQuestion", key = "{#email, #questionId}")
    public Boolean isUserViewedQuestion(String email, Long questionId) {
        TypedQuery<QuestionViewed> typedQuery = entityManager.createQuery(
                        "select q from QuestionViewed q where q.user.email = :email and q.question.id = :questionId",
                        QuestionViewed.class
                ).setParameter("email", email)
                .setParameter("questionId", questionId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    @CachePut(value = "isUserViewedQuestion", key = "{#questionViewed.user.email, #questionViewed.question.id}")
    public Boolean persistQuestionViewed(QuestionViewed questionViewed) {
        entityManager.persist(questionViewed);
        return true;
    }
}
