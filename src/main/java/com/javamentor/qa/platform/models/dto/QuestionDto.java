package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ali Veliev 10.12.2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private Long id;
    private String title;
    private Long authorId;
    private Long authorReputation;
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount;
    private int countAnswer;
    private int countValuable;
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private Enum<VoteType> isUserVote;
    private List<TagDto> listTagDto;
    private List<CommentDto> listCommentDto;
    private Boolean isUserBookmark;
    private Boolean isUserAnswerVote;
    private List<AnswerDto> listAnswerDto;
}


