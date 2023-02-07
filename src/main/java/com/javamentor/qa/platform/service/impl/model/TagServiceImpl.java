package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        super(tagDao);
        this.tagDao = tagDao;
    }

    public List<Tag> getListTagsByListOfTagName(List<String> listTagName){
        return tagDao.getListTagsByListOfTagName(listTagName);
    }

    @Override
    public boolean isTagsMappingToTrackedAndIgnoredCorrect(List<Long> trackedTag, List<Long> ignoredTag) {
        return tagDao.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag);
    }

    @Transactional
    @Override
    public boolean checkedAndIgnoredContainTag(Long tagId, Long userId) {
        return tagDao.checkedAndIgnoredContainTag(tagId, userId);
    }
}
