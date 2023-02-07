package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {

    private QuestionViewedDao questionViewedDao;

    public QuestionViewedServiceImpl(ReadWriteDao<QuestionViewed, Long> readWriteDao, QuestionViewedDao questionViewedDao) {
        super(readWriteDao);
        this.questionViewedDao = questionViewedDao;
    }

    @Override
    public Boolean isUserViewedQuestion(String email, Long questionId) {
        return questionViewedDao.isUserViewedQuestion(email, questionId);
    }


    @Override
    public Boolean persistQuestionViewed(QuestionViewed questionViewed) {
        return questionViewedDao.persistQuestionViewed(questionViewed);
    }
}
