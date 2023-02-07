package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

public interface VoteOnAnswerService extends ReadWriteService<VoteAnswer, Long> {
    Boolean getIfNotExists(Long answerId, Long userId);

    Long getCountOfVotes(Long answerId);

    @Override
    void persist(VoteAnswer voteAnswer);
}
