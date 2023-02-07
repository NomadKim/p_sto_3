package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;


public interface QuestionViewedDao extends ReadWriteDao<QuestionViewed, Long> {

    Boolean isUserViewedQuestion(String email, Long questionId);

    Boolean persistQuestionViewed(QuestionViewed questionViewed);
}
