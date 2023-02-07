package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.BookMarksDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookMarksDtoService{
    List<BookMarksDto> getAllBookMarksUsersById(Long Id);
}
