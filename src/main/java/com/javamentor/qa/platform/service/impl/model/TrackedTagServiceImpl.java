package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {
    private final TrackedTagDao trackedTagDao;

    @Autowired
    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
    }

    @Override
    public Boolean getTagIfNotExist(Long tagId, Long userId) {
        return trackedTagDao.getTagIfNotExist(tagId, userId);
    }

    @Transactional
    @Override
    public void deleteTrackedTagByTagIdAndUserId(Long tagId, Long userId) {
        trackedTagDao.deleteTrackedTagByTagIdAndUserId(tagId, userId);
    }


}
