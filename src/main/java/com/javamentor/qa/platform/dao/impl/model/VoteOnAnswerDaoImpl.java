package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteOnAnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class VoteOnAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteOnAnswerDao {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean getIfNotExists(Long answerId, Long userId) {
        TypedQuery<VoteAnswer> typedQuery = entityManager.createQuery(
                        "select va from VoteAnswer va where va.answer.id = :answerId and va.user.id = :userId",
                        VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", userId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    public Long getCountOfVotes(Long answerId) {
        return entityManager.createQuery(
                        "select sum(case va.vote when 'UP_VOTE' then 1 else -1 end) " +
                                "from VoteAnswer as va where va.answer.id = :answerId", Long.class)
                .setParameter("answerId", answerId)
                .getSingleResult();

    }
}
