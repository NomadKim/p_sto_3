package com.javamentor.qa.platform.models.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class CreateGroupChatDto {

    private String chatName;

    @NotEmpty
    private List<Long> userIds;
}
