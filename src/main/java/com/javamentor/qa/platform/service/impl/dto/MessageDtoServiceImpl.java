package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import org.springframework.stereotype.Service;

@Service
public class MessageDtoServiceImpl extends PageDtoServiceImpl<MessageDto> implements MessageDtoService {}
