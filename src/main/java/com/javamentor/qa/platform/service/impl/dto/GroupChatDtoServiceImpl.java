package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import org.springframework.stereotype.Service;

@Service
public class GroupChatDtoServiceImpl extends PageDtoServiceImpl<GroupChatDto> implements GroupChatDtoService {}
