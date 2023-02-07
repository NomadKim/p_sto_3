package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;


public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long> {

    Boolean isUserViewedQuestion(String email, Long questionId);

    Boolean persistQuestionViewed(QuestionViewed questionViewed);
}
