package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteOnQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class VoteOnQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteOnQuestionDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Boolean getIfNotExists(Long questionId, Long userId) {
        TypedQuery<VoteQuestion> typedQuery = entityManager.createQuery(
                        "select vq from VoteQuestion vq where vq.question.id = :questionId and vq.user.id = :userId",
                        VoteQuestion.class
                ).setParameter("questionId", questionId)
                .setParameter("userId", userId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    public Long getCountOfVotes(Long questionId) {
        Query query = entityManager.createQuery("select sum(case v.vote  when 'UP_VOTE' then 1 else -1 end) " +
                        "from VoteQuestion v where v.question.id=:questionId", Long.class)
                .setParameter("questionId", questionId);
        return ((Number) query.getSingleResult()).longValue();
    }
}
