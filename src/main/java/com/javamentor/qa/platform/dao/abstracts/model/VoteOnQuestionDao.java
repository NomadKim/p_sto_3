package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;


public interface VoteOnQuestionDao extends ReadWriteDao<VoteQuestion, Long> {

    Boolean getIfNotExists(Long questionId, Long userId);

    Long getCountOfVotes(Long questionId);
}
