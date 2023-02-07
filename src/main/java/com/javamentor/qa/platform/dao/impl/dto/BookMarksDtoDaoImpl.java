package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.BookMarksDtoDao;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookMarksDtoDaoImpl implements BookMarksDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookMarksDto> getAllBookMarksUsersById(Long Id) {
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.BookMarksDto(bm.question.id, " +
                        "bm.question.title, " +
                        "(select (count (an.id)) from Answer as an where an.question.id = bm.id), " +
                        "(select (count (qu.id)) from VoteQuestion as qu where qu.question.id = bm.id), " +
                        "(select (count (vi.id)) from QuestionViewed as vi where vi.question.id = bm.id), " +
                        "bm.question.persistDateTime)" +
                        "from BookMarks bm " +
                        "where bm.user.id =: userId", BookMarksDto.class
        ).setParameter("userId", Id).getResultList();
    }
}
