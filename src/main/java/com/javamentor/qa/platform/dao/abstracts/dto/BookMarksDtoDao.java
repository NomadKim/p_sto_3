package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.BookMarksDto;

import java.util.List;

public interface BookMarksDtoDao {
    List<BookMarksDto> getAllBookMarksUsersById(Long Id);
}

