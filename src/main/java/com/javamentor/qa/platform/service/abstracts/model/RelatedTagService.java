package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.RelatedTag;
import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface RelatedTagService extends ReadWriteService<RelatedTag, Long>{
    List<Tag> getAllChildTagsById(long id);
}
