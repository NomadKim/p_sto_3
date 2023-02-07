package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;

import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

public interface QuestionDtoDao {
    Optional<QuestionDto> getQuestionByQuestionIdAndUserId(Long questionId, Long userId);
}
