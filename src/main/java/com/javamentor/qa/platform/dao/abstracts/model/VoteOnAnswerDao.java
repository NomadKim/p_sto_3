package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

import java.util.Optional;

public interface VoteOnAnswerDao extends ReadWriteDao<VoteAnswer, Long> {
    Boolean getIfNotExists(Long answerId, Long userId);

    Long getCountOfVotes(Long answerId);
}

