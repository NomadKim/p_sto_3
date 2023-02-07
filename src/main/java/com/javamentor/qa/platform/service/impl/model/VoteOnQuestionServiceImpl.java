package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteOnQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
public class VoteOnQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteOnQuestionService {

    private VoteOnQuestionDao voteOnQuestionDao;
    private ReputationDao reputationDao;


    @Autowired
    public VoteOnQuestionServiceImpl(VoteOnQuestionDao voteOnQuestionDao, ReputationDao reputationDao) {
        super(voteOnQuestionDao);
        this.voteOnQuestionDao = voteOnQuestionDao;
        this.reputationDao = reputationDao;
    }

    @Override
    @Transactional
    public Boolean getIfNotExists(Long questionId, Long userId) {
        return voteOnQuestionDao.getIfNotExists(questionId, userId);
    }

    @Override
    public Long getCountOfVotes(Long questionId) {
        return voteOnQuestionDao.getCountOfVotes(questionId);
    }

    @Override
    public void persist(VoteQuestion voteQuestion) {
        Reputation reputation = new Reputation(LocalDateTime.now(), voteQuestion.getQuestion().getUser(), voteQuestion.getUser(), voteQuestion.getVote().getValue(), ReputationType.VoteQuestion, voteQuestion.getQuestion());
        reputationDao.persist(reputation);
        super.persist(voteQuestion);
    }
}
