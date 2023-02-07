package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatDto {
    private long id;
    private String chatName;
    private MessageDto lastMessageDto;

    public GroupChatDto(long id, String chatName, long messageId, String message, String nickName, long userId, String image, LocalDateTime persistDateTime) {
        this.id = id;
        this.chatName = chatName;
        this.lastMessageDto = new MessageDto(messageId, message, nickName, userId, image, persistDateTime);
    }
}
