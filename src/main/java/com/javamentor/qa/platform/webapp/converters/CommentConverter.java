package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CommentConverter {

    @Mapping(source = "comment.text", target = "comment")
    @Mapping(source = "comment.user.id", target = "userId")
    @Mapping(source = "comment.user.fullName", target = "fullName")
    @Mapping(constant = "0L", target = "reputation")
    @Mapping(source = "comment.persistDateTime", target = "dateAdded")

    public abstract CommentDto commentToCommentDto(Comment comment);
}
