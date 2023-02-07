package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long userId;
    private Long userReputation;
    private Long questionId;
    private String body;
    private LocalDateTime persistDate;
    private Boolean isHelpful;
    private Boolean isDeleted;
    private LocalDateTime dateAccept;
    private Long countValuable;
    private String image;
    private String nickName;
    private List<CommentDto> listOfComeentsDto;

}
