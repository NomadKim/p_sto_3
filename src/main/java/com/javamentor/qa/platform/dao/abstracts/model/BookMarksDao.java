package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.BookMarks;


public interface BookMarksDao extends ReadWriteDao<BookMarks, Long> {
    boolean isQuestionAlreadyExistOnUserBookmarks(long userId, long questionId);
}
