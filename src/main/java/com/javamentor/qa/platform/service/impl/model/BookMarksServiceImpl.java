package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookMarksDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.service.abstracts.model.BookMarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookMarksServiceImpl extends ReadWriteServiceImpl<BookMarks, Long> implements BookMarksService {

    private BookMarksDao bookmarkQuestionsDao;

    @Autowired
    public BookMarksServiceImpl(BookMarksDao bookmarkQuestionsDao) {
        super((ReadWriteDao<BookMarks, Long>) bookmarkQuestionsDao);
        this.bookmarkQuestionsDao = bookmarkQuestionsDao;
    }

    @Override
    public boolean isQuestionAlreadyExistOnUserBookmarks(long userId, long questionId) {
        return bookmarkQuestionsDao.isQuestionAlreadyExistOnUserBookmarks(userId, questionId);
    }

}
