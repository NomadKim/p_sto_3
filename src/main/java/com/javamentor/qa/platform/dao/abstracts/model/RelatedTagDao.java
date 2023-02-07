package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface RelatedTagDao extends ReadWriteDao <RelatedTag, Long> {
    List<Tag> getAllChildTagsById(long id);
}
