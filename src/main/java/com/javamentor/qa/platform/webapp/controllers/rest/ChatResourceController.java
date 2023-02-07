package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.dto.CreateGroupChatDto;
import com.javamentor.qa.platform.models.dto.CreateSingleChatDto;
import com.javamentor.qa.platform.models.dto.MessageDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.Chat;
import com.javamentor.qa.platform.models.entity.chat.ChatType;
import com.javamentor.qa.platform.models.entity.chat.GroupChat;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.ChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.GroupChatDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.MessageDtoService;
import com.javamentor.qa.platform.service.abstracts.model.GroupChatService;
import com.javamentor.qa.platform.service.abstracts.model.SingleChatService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.impl.dto.SingleChatDtoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@Api("Chats Api")
@RequestMapping("api/user/chat")
@AllArgsConstructor
public class ChatResourceController {

    private final SingleChatDtoServiceImpl singleChatDtoService;
    private final SingleChatService singleChatService;
    private final GroupChatDtoService groupChatDtoService;
    private final MessageDtoService messageDtoService;
    private final UserService userService;
    private final GroupChatService groupChatService;
    private final ChatDtoService chatDtoService;


    @GetMapping("/single")
    @ApiOperation("Возвращает SingleChatDto авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все SingleChatDto авторизованного пользователя"),
            @ApiResponse(code = 400, message = "Неправильные параметры запроса"),
    })
    public ResponseEntity<PageDto<SingleChatDto>> receiveAllSingleChatOfUser(
            @RequestParam("page") Integer page,
            @RequestParam("items") Integer items,
            @RequestParam(value = "sortAscendingFlag", required = false, defaultValue = "false") Boolean sortAscendingFlag)
    {

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("userId", userId);
        params.put("sortAscendingFlag", sortAscendingFlag);

        return new ResponseEntity<>(singleChatDtoService.getPageDto("paginationAllSingleChatsOfUser",
                params), HttpStatus.OK);
    }

    @GetMapping("/group")
    @ApiOperation("Возвращает объект PageDto с items <GroupChatDto> с учетом заданных параметров пагинации.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения с учетом заданных параметров пагинации."),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы."),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getGroupChatOutPutWithAllMessage(
            @RequestParam("page") Integer currentPage,
            @RequestParam(value = "items", defaultValue = "10") Integer items)
    {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPage);
        params.put("itemsOnPage", items);
        params.put("userId", userId);

        return new ResponseEntity<>(groupChatDtoService.getPageDto(
                "paginationGroupChat", params), HttpStatus.OK);
    }

    @GetMapping("/{id}/single/message")
    @ApiOperation("Возвращает все сообщения singleChat как объект класса PageDto<MessageDto> с учетом заданных параметров пагинации, " +
            "Сообщения сортируются по дате добавления: сначала самые новые.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения, отсортированные по дате добавление, сначала самые новые " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getAllMessageDtoInSingleChatSortedByPersistDate(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "sortAscendingFlag", required = false, defaultValue = "false") Boolean sortAscendingFlag,
            @PathVariable("id") Long chatId){

        if (!singleChatService.existsById(chatId)) {
            return new ResponseEntity<>("Чат с данным ID = " + chatId + ", не найден.", HttpStatus.NOT_FOUND);
        }

        if (singleChatService.isStatusDeleted(chatId)) {
            return new ResponseEntity<>("Чат с данным ID = " + chatId + ", не найден.", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("chatId", chatId);
        params.put("sortAscendingFlag", sortAscendingFlag);

        return new ResponseEntity<>(messageDtoService.getPageDto(
                "paginationAllMessagesSortedByPersistDate", params), HttpStatus.OK);
    }
    @GetMapping
    @ApiOperation("Возвращает сообщения в Single и Group чатам по заданному параметру")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Выполнен поиск по заданной строке!"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр поиска!"),
            @ApiResponse(code = 500, message = "По данному запросу ничего не было найдено")
    })
    public ResponseEntity<List<ChatDto>> findStringInSingleAndGroupChats(
            @RequestParam(value = "findMessage") String findMessages) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return new ResponseEntity<>(chatDtoService.getChatsByString(userId, findMessages), HttpStatus.OK);
    }

    @PostMapping("/group")
    @ApiOperation("Добавление нового группового чата. В RequestBody ожидает объект CreateGroupChatDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Групповой чат успешно добавлен"),
            @ApiResponse(code = 400, message = "Поле объекта CreateGroupChatDto " +
                    "userIds должно быть заполнено")
    })
    public ResponseEntity<?> addGroupChat(@Valid @RequestBody CreateGroupChatDto createGroupChatDto) {
        GroupChat groupChat = new GroupChat();
        Set<User> users = new HashSet<>();
        users.addAll(userService.getUsersByIds(createGroupChatDto.getUserIds()));
        groupChat.setUsers(users);
        Chat chat = new Chat(ChatType.GROUP);
        groupChat.setTitle(createGroupChatDto.getChatName());
        groupChat.setChat(chat);
        groupChatService.persist(groupChat);
        return new ResponseEntity<>("Групповой чат успешно добавлен", HttpStatus.OK);
    }

    @PostMapping(value = "/single")
    @ResponseBody
    @ApiOperation(value = "Добавление singleChat")
    @ApiResponses({
            @ApiResponse(code = 200, message = "SingleChat добавлен"),
            @ApiResponse(code = 400, message = "SingleChat не был добавлен")
    })
    public ResponseEntity<?> addSingleChat(@Valid @RequestBody CreateSingleChatDto createSingleChatDto){
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<User> user = userService.getById(createSingleChatDto.getUserRecipientId());
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body("UserRecipientId doesn't exist");
        }

        SingleChat singleChat = new SingleChat();
        singleChat.setUserOne(sender);
        singleChat.setUseTwo(user.get());
        singleChatService.persist(singleChat);
        singleChatService.addSingleChatAndMessage(singleChat, createSingleChatDto.getMessage());
        return new ResponseEntity<>("SingleChat чат успешно добавлен",HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаления чата по Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Чат успешно удален"),
            @ApiResponse(code = 400, message = "Чат не принадлежит текущему пользователю"),
            @ApiResponse(code = 404, message = "Чат или пользователь не найден")})
    public ResponseEntity<?> deleteChatById(@PathVariable Long id){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (groupChatService.existsById(id)){
            groupChatService.deleteChatFromUser(id, user);
            return new ResponseEntity<>("Чат успешно удален", HttpStatus.OK);
        }

        if (singleChatService.existsById(id)) {
            singleChatService.deleteChatFromUser(id, user);
            return new ResponseEntity<>("Чат успешно удален", HttpStatus.OK);
        }

        return new ResponseEntity<>("Ошибка удаления. Чат не найден.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{chatId}/message/find")
    @ApiOperation("Возвращает сообщения в чате в виде PageDto<MessageDto> при не точном поиске")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно возвращены все сообщения"),
            @ApiResponse(code = 400, message = "Не введены все обязательные параметры, либо введены не корректно")
    })
    public ResponseEntity<PageDto<MessageDto>> searchByChatMessage(
            @RequestParam(value = "items", defaultValue = "20") Integer items,
            @RequestParam("page") Integer page,
            @RequestParam("word") String word,
            @PathVariable Long chatId) {

        Map<String, Object> params = new HashMap<>();
        params.put("chatId", chatId);
        params.put("itemsOnPage", items);
        params.put("currentPageNumber", page);
        params.put("word", word);

        return new ResponseEntity<>(messageDtoService.getPageDto("paginationSearchByChatMessage", params), HttpStatus.OK);
    }

    @PostMapping("/group/{id}/join")
    @ApiOperation("Добавляет пользователя в групповой чат")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь успешно добавлен"),
            @ApiResponse(code = 404, message = "Пользователь или групповой чат не найден"),
            @ApiResponse(code = 409, message = "Пользователь уже есть в групповом чате")})
    public ResponseEntity<?> joinGroupChat(@PathVariable("id") Long chatId, @RequestBody Long userId) {

        Optional<User> userOptional = userService.getById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        Optional<GroupChat> chatOptional = groupChatService.getGroupChatWithUsersById(chatId);
        if (chatOptional.isEmpty()) {
            return new ResponseEntity<>("Групповой чат не найден", HttpStatus.BAD_REQUEST);
        }
        GroupChat chat = chatOptional.get();

        Set<User> chatUsers = chat.getUsers();
        if (chatUsers.contains(user)) {
            return new ResponseEntity<>("Пользователь уже есть в групповом чате", HttpStatus.BAD_REQUEST);
        }

        chatUsers.add(user);
        chat.setUsers(chatUsers);
        groupChatService.update(chat);
        return new ResponseEntity<>("Пользователь успешно добавлен в групповой чат", HttpStatus.OK);
    }

}