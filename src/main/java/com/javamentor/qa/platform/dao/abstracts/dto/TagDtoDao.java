package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface TagDtoDao {

    List<TagDto> getIgnoredTags(Long userId);

    Map<Long, List<TagDto>> getTagsByQuestionIds(List<?> ids);

    List<TagDto> getTop10FoundTags(String searchString);

    List<TagDto> getTop3UserTagsByReputation(Long userId);

    List<TagDto> getTagsByQuestionId(Long id);

}
