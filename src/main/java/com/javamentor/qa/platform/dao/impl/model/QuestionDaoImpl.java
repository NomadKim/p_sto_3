package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question,Long>implements QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getQuestionCount() {
        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM question WHERE is_deleted = false");
        return ((Number) query.getResultList().get(0)).longValue();
    }

}