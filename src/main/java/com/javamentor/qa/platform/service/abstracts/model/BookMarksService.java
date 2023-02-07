package com.javamentor.qa.platform.service.abstracts.model;


import com.javamentor.qa.platform.models.entity.BookMarks;

public interface BookMarksService extends ReadWriteService<BookMarks,Long> {
    boolean isQuestionAlreadyExistOnUserBookmarks(long userId, long questionId);

}
