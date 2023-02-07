package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.enums.Period;
import com.javamentor.qa.platform.models.entity.BookMarks;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;

import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.BookMarksService ;
import com.javamentor.qa.platform.service.abstracts.model.VoteOnQuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;

import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/user/question")
@Api("Question Api")
@AllArgsConstructor
public class QuestionResourceController {

    private final TagService tagService;
    private final QuestionDtoService questionDtoService;
    private final QuestionConverter questionConverter;
    private final TagConverter tagConverter;
    private final QuestionService questionService;

    private final BookMarksService bookMarksService;
    private VoteOnQuestionService voteOnQuestionService;
    private QuestionViewedService questionViewedService;

    @GetMapping("/sortedQuestions")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по голосам, ответам и просмотрам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по голосам, ответам и просмотрам"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getQuestionsSortedByVotesAndAnswersAndQuestionViewed(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByVoteAndAnswerAndQuestionView", params), HttpStatus.OK);
    }

    @PostMapping("/{questionId}/view")
    @ApiOperation("При переходе на вопрос c questionId=* авторизованного пользователя, вопрос добавляется в QuestionViewed")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Вопрос просмотрен впервые"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден"),
            @ApiResponse(code = 400, message = "Вопрос уже был просмотрен, либо формат введенного questionId является не верным")
    })
    public ResponseEntity<?> insertAuthUserToQuestionViewedByQuestionId(@PathVariable("questionId") Long questionId) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(questionId);

        if (!question.isPresent()) {
            return new ResponseEntity<>("Вопрос с id=" + questionId + " не найден", HttpStatus.NOT_FOUND);
        }
        if (!questionViewedService.isUserViewedQuestion(userPrincipal.getEmail(), question.get().getId())) {
            questionViewedService.persistQuestionViewed(new QuestionViewed(userPrincipal, question.get(), LocalDateTime.now()));
            return new ResponseEntity<>("Вопрос просмотрен впервые", HttpStatus.OK);
        }

        return new ResponseEntity<>("Вопрос уже был просмотрен", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count")
    @ApiOperation("Получение количества вопросов в базе данных")
    @ApiResponse(code = 200, message = "Получено количество вопросов в базе данных")
    public ResponseEntity<?> getQuestionCount() {
        return new ResponseEntity<>(questionService.getQuestionCount(), HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    @ApiOperation("Возвращает вопрос как объект QuestionDto и тэги, относящиеся к этому вопросу по ИД вопроса")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Показан вопрос с questionId=* и тэги, относящиеся к этому вопросу"),
            @ApiResponse(code = 400, message = "Формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> getQuestionByQuestionIdAndUserId(@PathVariable("questionId") Long questionId) {

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Optional<QuestionDto> questionDto = questionDtoService.getQuestionByQuestionIdAndUserId(questionId, userId);
        return questionDto.isEmpty()
                ? new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/upVote")
    @ApiOperation("Запись в БД голосования со значением UP за вопрос c questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Поднятие репутации вопроса с questionId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> insertUpVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                VoteQuestion upVoteQuestion = new VoteQuestion(sender, question, VoteType.UP_VOTE);
                voteOnQuestionService.persist(upVoteQuestion);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionId}/downVote")
    @ApiOperation("Запись в БД голосования со значением DOWN за вопрос c questionId=*")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Понижение репутации вопроса с questionId=* прошло успешно"),
            @ApiResponse(code = 400, message = "Ошибка голосования: голос уже учтен или формат введенного questionId является не верным"),
            @ApiResponse(code = 404, message = "Вопрос с questionId=* не найден")
    })
    public ResponseEntity<?> insertDownVote(@PathVariable("questionId") Long questionId) {
        User sender = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> optionalQuestion = questionService.getById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!(voteOnQuestionService.getIfNotExists(question.getId(), sender.getId()))) {
                VoteQuestion downVoteQuestion = new VoteQuestion(sender, question, VoteType.DOWN_VOTE);
                voteOnQuestionService.persist(downVoteQuestion);
                return new ResponseEntity<>(voteOnQuestionService.getCountOfVotes(questionId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Ваш голос уже учтен", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Вопрос с questionId=" + questionId + " не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    @ApiOperation("Создание нового вопроса от пользователя. В RequestBody ожидает объект QuestionCreateDto")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ваш вопрос успешно создан"),
            @ApiResponse(code = 400, message = "Объект QuestionCreateDto не передан в RequestBody. Поля объекта QuestionCreateDto title, " +
                    "description должны быть заполнены, в tags должен содержаться как минимум один объект класса TagDto")
    })
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateDto questionCreateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setUser((User) authentication.getPrincipal());
        question.setDescription(questionCreateDto.getDescription());
        question.setTags(tagConverter.listTagDtoToListTag(questionCreateDto.getTags()));

        questionService.persist(question);

        return new ResponseEntity<>(questionConverter.questionToQuestionDto(question), HttpStatus.OK);
    }


    @GetMapping()
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с тэгами по ним с учетом заданных параметров пагинации.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров пагинациим"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getQuestions(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsWithGivenTags", params), HttpStatus.OK);
    }

    @GetMapping("/mostPopularWeek")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> за неделю с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по наибольшей популярности")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы за неделю с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по наибольшей популярности"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> mostPopularQuestionsWeek(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsMostPopularWeek", params), HttpStatus.OK);
    }

    @GetMapping("/noAnswer")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto>, по которым не было ответа " +
            "с тэгами с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы, по которым не было ответа с тэгами " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getQuestionsNoAnswer(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationQuestionsNoAnswer", params), HttpStatus.OK);
    }

    @GetMapping("/new")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с учетом заданных параметров пагинации, " +
            "Вопросы сотртируются по дате добавление: сначала самые новые.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами, отсортированные по дате добавление, сначала самые новые " +
                    "с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getAllQuestionDtoSortedByPersistDate(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsWithTagsSortedByPersistDate", params), HttpStatus.OK);

    }

    @GetMapping("/sortedQuestionsByMonth")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> за месяц с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по голосам, ответам и просмотрам")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы за месяц с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по голосам, ответам и просмотрам"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getQuestionsSortedByVotesAndAnswersAndViewsByMonth(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag,
            @RequestParam(value = "period", required = false, defaultValue = "ALL") Period period) {


        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);
        params.put("period", period);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByVoteAndAnswerAndViewsByMonth", params), HttpStatus.OK);
    }

    @GetMapping("/reputation")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с тэгами по ним с учетом заданных параметров пагинации. " +
            "Вопросы сортируются по наибольшей репутации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами по ним с учетом заданных " +
                    "параметров пагинации. Вопросы отсортированы по наибольшей репутации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 400, message = "В запросе неправильно переданы тэги"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<?> getQuestionsByReputation(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>("Неправильно переданы тэги в списки trackedTag или ignoredTag", HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByReputation", params), HttpStatus.OK);
    }

    @GetMapping("/viewed")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с учетом заданных параметров пагинации, " +
            "Вопросы сортируются по количеству просмотров: сначала самые просматриваемые.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами, отсортированные по количеству просмотров," +
                    " сначала самые просматриваемые с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getAllQuestionDtoSortedByViewCount(
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsWithTagsSortedByViewCount", params), HttpStatus.OK);
    }

    @GetMapping("/vote")
    @ApiOperation("Возращает все вопросы как объект класса PageDto<QuestionViewDto> с учетом заданных параметров пагинации, " +
            "Вопросы сортируются по количеству голосов: сначала самые полезные (больше голосов).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все вопросы с тэгами, отсортированные по количеству голосов," +
                    " сначала самые полезные (больше голосов) с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<QuestionViewDto>> getAllQuestionDtoSortedByVotes (
            @RequestParam("page") Integer page,
            @RequestParam(value = "items", defaultValue = "10") Integer items,
            @RequestParam(value = "trackedTag", defaultValue = "-1") List<Long> trackedTag,
            @RequestParam(value = "ignoredTag", defaultValue = "-1") List<Long> ignoredTag) {

        if (!tagService.isTagsMappingToTrackedAndIgnoredCorrect(trackedTag, ignoredTag)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("trackedTag", trackedTag);
        params.put("ignoredTag", ignoredTag);
        params.put("userId", userId);

        return new ResponseEntity<>(questionDtoService.getPageQuestionsWithTags(
                "paginationAllQuestionsSortedByVoteUseful", params), HttpStatus.OK);
    }

    @PostMapping ("/{id}/bookmark")
    @ApiOperation("Добавление вопроса в закладки авторизованного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ваш вопрос успешно добавлен в закладки"),
            @ApiResponse(code = 202, message = "Вопрос уже добавлен в закладки пользователя"),
            @ApiResponse(code = 404, message = "Вопрос с указанным id не найден"),
    })
    public ResponseEntity<?> addQuestionToBookmarks(@PathVariable ("id") Long id) {
        User userPrincipal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Optional<Question> question = questionService.getById(id);

        if (question.isEmpty()) {
            return new ResponseEntity<>("Вопрос с id=" + id + " не найден", HttpStatus.NOT_FOUND);
        }
        if (!bookMarksService.isQuestionAlreadyExistOnUserBookmarks(userPrincipal.getId(),question.get().getId())) {
            bookMarksService.persist(new BookMarks(userPrincipal, question.get()));
            return new ResponseEntity<>("Вопрос добавлен в закладки текущего пользователя", HttpStatus.OK);
        }
        return new ResponseEntity<>("Вопрос уже добавлен в закладки пользователя", HttpStatus.ACCEPTED);
    }
}