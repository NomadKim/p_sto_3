package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.BookMarksDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.BookMarksDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookMarkersDtoServiceImpl implements BookMarksDtoService {

    private final BookMarksDtoDao bookMarksDtoDao;
    private final TagDtoDao tagDtoDao;

    @Autowired
    public BookMarkersDtoServiceImpl(BookMarksDtoDao bookMarksDtoDao, TagDtoDao tagDtoDao) {
        this.bookMarksDtoDao = bookMarksDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<BookMarksDto> getAllBookMarksUsersById(Long Id) {
        List<BookMarksDto> bookMarksDtoList = bookMarksDtoDao.getAllBookMarksUsersById(Id);

        List<Long> idBookMarks = bookMarksDtoList.stream()
                .map(BookMarksDto::getQuestionId).collect(Collectors.toList());

        Map<Long, List<TagDto>> tags = tagDtoDao.getTagsByQuestionIds(idBookMarks);

        for (BookMarksDto e : bookMarksDtoList) {
            e.setTag(tags.get(e.getQuestionId()));
        }
        return bookMarksDtoList;
    }
}
