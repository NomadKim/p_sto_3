package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.entity.chat.Message;
import com.javamentor.qa.platform.models.entity.user.MessageStar;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.ChatService;
import com.javamentor.qa.platform.service.abstracts.model.MessageService;
import com.javamentor.qa.platform.service.abstracts.model.MessageStarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Api("Message Api")
@RequestMapping("api/user/message")
@AllArgsConstructor
public class MessageResourceController {
    private final MessageStarService messageStarService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final MessageDtoService messageDtoService;

    @DeleteMapping("/star")
    @ApiOperation(value = "Удаление сообщения из избранных пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Сообщение удаленно из избранных"),
            @ApiResponse(code = 404, message = "Сообщение не найдено"),
            @ApiResponse(code = 400, message = "Сообщение не принадлежит авторизованному пользователю")
    })
    @ApiParam(name = "id", value = "message id", required = true)
    public ResponseEntity<?> deleteMessageStarById(@NotNull @RequestParam("id") Long id) {

        Optional<MessageStar> messageStar = messageStarService.getById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (messageStar.isEmpty()) {
            return new ResponseEntity<>("Сообщение не найдено", HttpStatus.NOT_FOUND);
        }

        if (messageStar.get().getUser().getId() != user.getId()) {
            return new ResponseEntity<>("Сообщение другово пользователя не может быть удаленно", HttpStatus.BAD_REQUEST);
        }

        messageStarService.deleteById(id);
        return new ResponseEntity<>("Сообщение удаленно из избранных", HttpStatus.OK);
    }

    @PostMapping("/{id}/star")
    @ApiOperation("При переходе на сообщение c messageId=*, message добавляется в messageStar(избранные сообщения) авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Сообщение успешно добавлено в избранные"),
            @ApiResponse(code = 404, message = "Сообщение с messageId=* не найдено"),
            @ApiResponse(code = 400, message = "Нельзя добавлять в избранные более трех сообщений, либо формат введенного messageId является не верным"),
            @ApiResponse(code = 400, message = "Юзер не состоит в чате")

    })

    public ResponseEntity<?> insertMessageToMessageStarByMessageId(@PathVariable("id") Long id) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Message> message = messageService.getById(id);

        if (message.isEmpty()) {
            return new ResponseEntity<>("Сообщение с id=" + id + " не найден", HttpStatus.NOT_FOUND);
        }


        if (!chatService.isChatHasUser(message.get().getChat().getId(), userPrincipal.getId())) {
            return new ResponseEntity<>("Юзер не состоит в чате", HttpStatus.BAD_REQUEST);
        }

        if (!messageStarService.isUserHasNoMoreThanThreeMessageStar(userPrincipal.getId())) {
            return new ResponseEntity<>("Нельзя добавлять в избранные более трех сообщений", HttpStatus.BAD_REQUEST);
        }


        MessageStar messageStar = new MessageStar();
        messageStar.setUser(userPrincipal);
        messageStar.setMessage(message.get());
        messageStarService.persist(messageStar);
        return new ResponseEntity<>("Сообщение с id=" + id + " успешно добавлено в избранные", HttpStatus.OK);
    }

    @GetMapping("/global/find")
    @ApiOperation(value = "Возвращает объект PageDto с items <MessageDto> в глобальном чате по неточному поиску с " +
            "учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения, которые содержат строку text=*"),
            @ApiResponse(code = 400, message = "Отсутствует обязательный параметр(ы) либо неверный формат ввода: text, currentPageNumber, itemsOnPage")
    })
    public ResponseEntity<?> findMessagesInGlobalChatPaginationByText(
            @RequestParam("text") String text,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("items") Integer itemsOnPage) {
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("currentPageNumber", currentPage);
        params.put("itemsOnPage", itemsOnPage);

        PageDto<MessageDto> pageDto = messageDtoService.getPageDto("paginationFindMessagesInGlobalChatByText", params);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
