package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.CreateSingleChatDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SingleChatConverter {

    @Autowired
    UserService userService;

    @Mapping(source = "singleChat.id", target = "id")
    @Mapping(source = "singleChat.useTwo.id", target = "userRecipientId")
    @Mapping(source = "singleChat.userOne.id", target = "userSenderId")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "persistDateTimeLastMessage", ignore = true)
    public abstract SingleChatDto singleChatToSingleChatDto(SingleChat singleChat);

    @Mapping(target = "useTwo", source = "userRecipientId", qualifiedByName = "userMapper")
    @Mapping(target = "userOne", source = "userSenderId", qualifiedByName = "userMapper")
    public abstract SingleChat createSingleChatDtoToSingleChat(CreateSingleChatDto createSingleChatDto);

    @Named("userMapper")
    public User userMapper(Long id){
        return userService.getById(id).get();
    }
}

