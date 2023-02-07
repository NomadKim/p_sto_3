package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagViewDto;

import java.util.List;

public interface TagDtoService extends PageDtoService<TagViewDto>{
    List<TagDto> getIgnoredTags(Long userId);
    List<TagDto> getTop10FoundTags(String searchString);
}
