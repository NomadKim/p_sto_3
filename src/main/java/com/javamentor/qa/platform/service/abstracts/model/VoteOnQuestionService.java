package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;


public interface VoteOnQuestionService extends ReadWriteService<VoteQuestion, Long> {

    Boolean getIfNotExists(Long questionId, Long userId);

    Long getCountOfVotes(Long questionId);

    @Override
    void persist(VoteQuestion voteQuestion);
    //
}
