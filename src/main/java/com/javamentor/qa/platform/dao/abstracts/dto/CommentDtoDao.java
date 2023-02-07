package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentDtoDao {
    List<CommentDto> getCommentDtosByQuestionId(Long id);
    Optional<CommentDto> getCommentDtoByCommentId(Long id);
    Map<Long, List<CommentDto>> getCommentDtosByAnswerIds(List<Long> ids);
}
