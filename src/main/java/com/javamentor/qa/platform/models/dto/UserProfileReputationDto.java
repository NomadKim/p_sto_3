package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileReputationDto {
    private int reputation;
    private Long questionId;
    private String title;
    private LocalDateTime persistDate;
}
