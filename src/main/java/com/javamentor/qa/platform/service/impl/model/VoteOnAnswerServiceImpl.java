package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteOnAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnAnswerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class VoteOnAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteOnAnswerService {

    private final VoteOnAnswerDao voteOnAnswerDao;
    private final ReputationDao reputationDao;

    public VoteOnAnswerServiceImpl(VoteOnAnswerDao voteOnAnswerDao, ReputationDao reputationDao) {
        super(voteOnAnswerDao);
        this.voteOnAnswerDao = voteOnAnswerDao;
        this.reputationDao = reputationDao;
    }

    @Override
    public Boolean getIfNotExists(Long answerId, Long userId) {
        return voteOnAnswerDao.getIfNotExists(answerId, userId);
    }

    @Override
    public Long getCountOfVotes(Long answerId) {
        return voteOnAnswerDao.getCountOfVotes(answerId);
    }

    @Override
    public void persist(VoteAnswer voteAnswer) {
        Reputation reputation = new Reputation(
                LocalDateTime.now(),
                voteAnswer.getAnswer().getUser(),
                voteAnswer.getUser(),
                voteAnswer.getVote().getValue(),
                ReputationType.VoteAnswer,
                voteAnswer.getAnswer());
        reputationDao.persist(reputation);
        super.persist(voteAnswer);
    }
}
