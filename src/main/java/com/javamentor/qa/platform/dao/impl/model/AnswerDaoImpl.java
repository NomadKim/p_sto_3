package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer,Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteById(Long id) {
        String query = "UPDATE Answer SET isDeleted = true where id = :id";
        entityManager.createQuery(query)
                .setParameter("id", id)
                .executeUpdate();
    }


    @Override
    public Boolean getIfNotExists(Long questionId, Long userId) {
        TypedQuery<Boolean> typedQuery = entityManager.createQuery(
                        "select case when count(a) > 0 then FALSE else TRUE end " +
                                "from Answer a where a.question.id =: questionId and a.user.id = :userId",
                        Boolean.class
                ).setParameter("questionId", questionId)
                .setParameter("userId", userId);
        return typedQuery.getSingleResult();
    }

    @Override
    public Boolean isAnswerExistInQuestion(Long answerId, Long questionId) {
        TypedQuery<Answer> typedQuery = entityManager.createQuery(
                "select a from Answer a where a.id = :answerId and a.question.id = :questionId",
                Answer.class)
                .setParameter("answerId", answerId)
                .setParameter("questionId", questionId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isEmpty();
    }
}