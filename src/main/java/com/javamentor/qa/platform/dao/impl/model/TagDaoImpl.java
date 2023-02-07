package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getListTagsByListOfTagName(List<String> listTagName) {
        String hql = "select tag from Tag tag where tag.name in (:listTagName)";
        Query query = entityManager.createQuery(hql).setParameter("listTagName", listTagName);
        return query.getResultList();
    }

    @Override
    public boolean isTagsMappingToTrackedAndIgnoredCorrect(List<Long> trackedTag, List<Long> ignoredTag) {
        return entityManager.createQuery(
                        "select tt, it from TrackedTag tt, IgnoredTag it " +
                                "where ((tt.trackedTag.id not in :trackedTag and -1 not in :trackedTag) or tt.trackedTag.id in :ignoredTag) " +
                                "or ((it.ignoredTag.id not in :ignoredTag and -1 not in :ignoredTag) or it.ignoredTag.id in :trackedTag)")
                .setParameter("ignoredTag", ignoredTag)
                .setParameter("trackedTag", trackedTag)
                .getResultList()
                .isEmpty();
    }

    @Override
    public boolean checkedAndIgnoredContainTag(Long tagId, Long userId) {
        String hql = "SELECT count(it) FROM IgnoredTag it JOIN TrackedTag tt ON it.user.id=tt.user.id WHERE " +
                "it.ignoredTag.id =:tagId AND tt.trackedTag.id=:tagId AND it.user.id=:userId";
        return entityManager.createQuery(hql).setParameter("tagId", tagId).getFirstResult()==0;
    }

}