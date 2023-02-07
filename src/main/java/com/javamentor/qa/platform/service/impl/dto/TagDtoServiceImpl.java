package com.javamentor.qa.platform.service.impl.dto;


import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagDtoServiceImpl extends PageDtoServiceImpl<TagViewDto> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    @Autowired
    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getIgnoredTags(Long userId) {
        return tagDtoDao.getIgnoredTags(userId);
    }

    @Override
    public List<TagDto> getTop10FoundTags(String searchString) {
       return tagDtoDao.getTop10FoundTags(searchString);
    }


}
