package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.BookMarksDao;
import com.javamentor.qa.platform.models.entity.BookMarks;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Repository
public class BookMarksDaoImpl extends ReadWriteDaoImpl<BookMarks, Long> implements BookMarksDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isQuestionAlreadyExistOnUserBookmarks(long userId, long questionId) {
        String hql = "select count(bookmark) from BookMarks bookmark where bookmark.question.id = :questionId and bookmark.user.id = :userId";
        Query query = entityManager.createQuery(hql).setParameter("userId", userId).setParameter("questionId", questionId);
        long count = (long) query.getSingleResult();
        return count > 0;
    }
}
