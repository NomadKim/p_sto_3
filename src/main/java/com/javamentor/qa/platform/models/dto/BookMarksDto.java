package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMarksDto {
    private long questionId;
    private String title;
    private List<TagDto> tag;
    private long countAnswer;
    private long countVote;
    private long countView;
    private LocalDateTime persistQuestionDate;

    public BookMarksDto(long questionId, String title, long countAnswer, long countVote, long countView, LocalDateTime persistQuestionDate) {
        this.questionId = questionId;
        this.title = title;
        this.countAnswer = countAnswer;
        this.countVote = countVote;
        this.countView = countView;
        this.persistQuestionDate = persistQuestionDate;
    }

}