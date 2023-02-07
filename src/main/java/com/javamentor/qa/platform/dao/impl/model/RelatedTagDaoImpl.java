package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RelatedTagDao;
import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class RelatedTagDaoImpl extends ReadWriteDaoImpl<RelatedTag, Long> implements RelatedTagDao {

    private final EntityManager entityManager;

    @Autowired
    public RelatedTagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Tag> getAllChildTagsById(long id) {
        Query query = entityManager.createNativeQuery("SELECT child_tag FROM related_tag WHERE main_tag = :id");
        query.setParameter("id", id);
        return query.getResultList();

    }
}
