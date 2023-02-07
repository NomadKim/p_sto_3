package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;

public interface IgnoredTagDao extends ReadWriteDao<IgnoredTag, Long> {

    public Boolean getTagIfNotExist(Long tagId, Long userId);
    void deleteIgnoredTagByTagIdAndUserId(Long tagId, Long userId);
}
