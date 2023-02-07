package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean getTagIfNotExist(Long tagId, Long userId) {
        TypedQuery<IgnoredTag> typedQuery = entityManager.createQuery(
                        "select it from IgnoredTag it where it.ignoredTag.id = :tagId and it.user.id = :userId",
                        IgnoredTag.class
                ).setParameter("tagId",tagId)
                .setParameter("userId",userId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    public void deleteIgnoredTagByTagIdAndUserId(Long tagId, Long userId) {
        entityManager.createQuery(
                        "delete from IgnoredTag it where it.ignoredTag.id = :tagId and it.user.id = :userId")
                .setParameter("tagId",tagId)
                .setParameter("userId",userId)
                .executeUpdate();
    }
}
