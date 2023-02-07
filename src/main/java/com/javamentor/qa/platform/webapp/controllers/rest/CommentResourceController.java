package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.entity.question.CommentQuestion;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.model.CommentQuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/comment")
@Api("Comment Api")
@AllArgsConstructor
public class CommentResourceController {

    private final CommentDtoService commentDtoService;
    private final CommentQuestionService commentQuestionService;
    private final QuestionService questionService;

    @PostMapping("/question/{questionId}")
    @ApiOperation(value = "Добавляет комментарий к вопросу с questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Добавлен комментарий к вопросу с questionId=*"),
            @ApiResponse(code = 400, message = "Комментарий пустой либо неверный формат переменной questionId. Ожидается целое положительное число > 0"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> addCommentByQuestionId(@Valid @RequestBody String comment, @PathVariable("questionId") Long questionId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Question> question = questionService.getById(questionId);

        if (question.isEmpty()) {
            return new ResponseEntity<>("Вопрос с id=" + questionId + " не найден", HttpStatus.NOT_FOUND);
        }

        if (comment.isBlank()) {
            return new ResponseEntity<>("Комментарий пустой", HttpStatus.BAD_REQUEST);
        }

        CommentQuestion commentQuestion = new CommentQuestion();
        commentQuestion.setUser(user);
        commentQuestion.setText(comment);
        commentQuestion.setQuestion(question.get());
        commentQuestionService.persist(commentQuestion);
        return new ResponseEntity<>("Комментарий к вопросу с id=" + questionId + " успешно добавлен", HttpStatus.OK);
    }

    @GetMapping("/question/{questionId}")
    @ApiOperation(value = "Возвращает объект PageDto с items <CommentDto> с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все комментарии к вопросу с questionId=*"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> getCommentsPaginationByQuestionId(
            @PathVariable Long questionId,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam(required = false, name = "items", defaultValue = "10") Integer itemsOnPage) {

        if (!questionService.existsById(questionId)) {
            return new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("questionId", questionId);
        params.put("currentPageNumber", currentPage);
        params.put("itemsOnPage", itemsOnPage);

        PageDto<CommentDto> pageDto = commentDtoService.getPageDto("paginationCommentsQuestion", params);

        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
