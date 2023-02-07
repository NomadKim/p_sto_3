package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AnswerConverter {

    @Mapping(constant = "0L", target = "countValuable")
    @Mapping(constant = "0L", target = "userReputation")
    @Mapping(source = "answer.user.id", target = "userId")
    @Mapping(source = "answer.question.id", target = "questionId")
    @Mapping(source = "answer.user.imageLink", target = "image")
    @Mapping(source = "answer.user.nickname", target = "nickName")
    @Mapping(source = "answer.htmlBody", target = "body")
    @Mapping(source = "answer.persistDateTime", target = "persistDate")
    @Mapping(source = "answer.dateAcceptTime", target = "dateAccept")

    public abstract AnswerDto answerToAnswerDto(Answer answer);
}
