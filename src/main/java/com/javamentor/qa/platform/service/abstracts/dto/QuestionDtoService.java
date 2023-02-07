package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;

import java.util.Map;
import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

public interface QuestionDtoService extends PageDtoService<QuestionViewDto> {

    Optional<QuestionDto> getQuestionByQuestionIdAndUserId(Long questionId, Long userId);

    PageDto<QuestionViewDto> getPageQuestionsWithTags(String pageDtoDaoName, Map<String, Object> params);
}

