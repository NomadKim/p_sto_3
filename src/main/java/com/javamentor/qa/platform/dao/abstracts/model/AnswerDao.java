package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {
    Boolean getIfNotExists(Long questionId, Long userId);
    Boolean isAnswerExistInQuestion(Long answerId, Long questionId);
}