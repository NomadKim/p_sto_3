package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleChatDto {

    private Long id;
    private Long userRecipientId;
    private Long userSenderId;
    private String name;
    private String image;
    private String lastMessage;
    private LocalDateTime persistDateTimeLastMessage;

    public SingleChatDto(Long id, String name ,String image, String lastMessage, LocalDateTime persistDateTimeLastMessage) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.persistDateTimeLastMessage = persistDateTimeLastMessage;
    }
}
