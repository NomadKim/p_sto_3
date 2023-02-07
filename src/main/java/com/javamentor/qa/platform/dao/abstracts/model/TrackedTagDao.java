package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.TrackedTag;

public interface TrackedTagDao extends ReadWriteDao<TrackedTag, Long> {

    public Boolean getTagIfNotExist(Long tagId, Long userId);
    void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId);
}
