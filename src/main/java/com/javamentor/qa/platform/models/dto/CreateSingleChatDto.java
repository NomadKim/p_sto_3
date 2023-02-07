package com.javamentor.qa.platform.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ApiModel
public class CreateSingleChatDto {


    @ApiModelProperty(notes = "получатель сообщения")
    @NotNull(message = "Значение UserId отсутствует" )
    private Long userRecipientId; //userId

    private Long userSenderId;

    @ApiModelProperty
    @NotBlank
    private String message;

}
