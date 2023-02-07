package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private AnswerDtoDao answerDtoDao;
    private CommentDtoDao commentDtoDao;

    @Autowired
    public AnswerDtoServiceImpl(AnswerDtoDao answerDtoDao, CommentDtoDao commentDtoDao){
        this.answerDtoDao = answerDtoDao;
        this.commentDtoDao = commentDtoDao;
    }

    public AnswerDtoServiceImpl() {

    }

    @Override
    public List<AnswerDto> getAllAnswersByQuestionId(Long id) {

        List<AnswerDto> answerDtos = answerDtoDao.getAllByQuestionId(id);
        List<Long> ids = answerDtos.stream().map(
                answerDtoElement -> answerDtoElement.getId()).collect(Collectors.toList());
        Map<Long, List<CommentDto>> comments = commentDtoDao.getCommentDtosByAnswerIds(ids);
        for (AnswerDto answer : answerDtos) {
            answer.setListOfComeentsDto(comments.get(answer.getId()));
        }
        return answerDtos;
    }

    @Override
    public List<AnswerDto> getDeletedAnswersByUserId(Long id) {
        return answerDtoDao.getDeletedAnswersByUserId(id);
    }

    @Override
    public Long getAmountAllAnswersByUserId(Long id) {
        return answerDtoDao.getAmountAllAnswersByUserId(id);
    }

    @Override
    public List<AnswerUserDto> getAnswerUserDtoForWeek(Long id) {
        return answerDtoDao.getAnswerUserDtoForWeek(id);
    }

}
