package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean getTagIfNotExist(Long tagId, Long userId) {
        TypedQuery<TrackedTag> typedQuery = entityManager.createQuery(
                        "select tt from TrackedTag tt where tt.trackedTag.id = :tagId and tt.user.id = :userId",
                        TrackedTag.class
                ).setParameter("tagId",tagId)
                .setParameter("userId",userId);
        return SingleResultUtil.getSingleResultOrNull(typedQuery).isPresent();
    }

    @Override
    public void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId) {
        entityManager.createQuery(
                        "delete from TrackedTag tt where tt.trackedTag.id = :tagId and tt.user.id = :userId")
                .setParameter("tagId",tagId)
                .setParameter("userId",userId)
                .executeUpdate();
    }
}
