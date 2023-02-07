package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TrackedTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackedTagDtoServiceImpl implements TrackedTagDtoService {

    private final TrackedTagDtoDao tagDtoDao;

    @Autowired
    public TrackedTagDtoServiceImpl(TrackedTagDtoDao tagDtoDao){
        this.tagDtoDao = tagDtoDao;
    }


    @Override
    public List<TagDto> getTrackedTags(Long Id) {
        return tagDtoDao.getTrackedTags(Id);
    }
}
