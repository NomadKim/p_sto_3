package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotEmpty
    private List<TagDto> tags;
}
