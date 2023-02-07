package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TrackedTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TrackedTagDtoDaoImpl
        implements TrackedTagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTrackedTags(Long userId) {
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagDto(tt.id, tt.trackedTag.name, tt.trackedTag.description) " +
                        "from TrackedTag tt " +
                        "where tt.user.id =: userId",TagDto.class
        ).setParameter("userId", userId).getResultList();
    }
}