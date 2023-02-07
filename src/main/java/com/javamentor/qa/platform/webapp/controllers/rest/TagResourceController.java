package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TagViewDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/tag")
@Api("Tag Api")
@AllArgsConstructor
public class TagResourceController {

    private final TrackedTagDtoService trackedTagDtoService;
    private final TagDtoService tagDtoService;
    private final TagService tagService;
    private final TagConverter tagConverter;
    private final TrackedTagService trackedTagService;
    private final IgnoredTagService ignoredTagService;

    @GetMapping("/tracked")
    @ApiOperation(value = "Возвращает все отслеживаемые текущим пользователем тэги")
    @ApiResponse(code = 200, message = "Получены все отслежиаемые текущим пользователем тэги")
    public ResponseEntity<List<TagDto>> getAllTrackedTags() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        List<TagDto> tagDtos = trackedTagDtoService.getTrackedTags(userId);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Возвращает все игнорируемые текущим пользователем тэги")
    @ApiResponse(code = 200, message = "Получены все игнорируемые текущим пользователем тэги")
    @GetMapping("/ignored")
    public ResponseEntity<List<TagDto>> getAllIgnoredTags() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<TagDto> tagDtos = tagDtoService.getIgnoredTags(userId);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    @GetMapping("/name")
    @ApiOperation("Выводит все тэги, отсортированные по имени, с учетом заданных параметров пагинации")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все тэги, отсортированные по имени, с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<TagViewDto>> getAllTagsOrderByNamePagination(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "items", required = false,
                    defaultValue = "10") Integer items,
            @RequestParam(value = "filter", required = false,
                    defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("tagsFilter", filter);

        PageDto<TagViewDto> pageDto = tagDtoService.getPageDto("paginationAllTagsSortedByName", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Добавляет с tagId=* в отслеживаемые текущим пользователем")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тэг с tagId=* добавлен в отслеживаемые"),
            @ApiResponse(code = 400, message = "Неверный формат введенного tagId"),
            @ApiResponse(code = 404, message = "Тэг с tagId=* не найден")
    })
    @PostMapping("/{tagId}/tracked")
    public ResponseEntity<?> addTrackedTag(@PathVariable("tagId") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (tagService.checkedAndIgnoredContainTag(tagId, user.getId())) {
                TrackedTag trackedTag = new TrackedTag();
                trackedTag.setTrackedTag(tag);
                trackedTag.setUser(user);
                trackedTagService.persist(trackedTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Тэг с tagId=*" + tagId + " не найден", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(
            value = "Добавляет тэг с tagId=* в игнорируемые текущим пользователем")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тэг с tagId=* добавлен в игнорируемые"),
            @ApiResponse(code = 400, message = "Неверный формат введенного tagId"),
            @ApiResponse(code = 404, message = "Тэг с tagId=* не найден")
    })
    @PostMapping("/{tagId}/ignored")
    public ResponseEntity<?> addIgnoredTag(@PathVariable("tagId") Long tagId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tag> optionalTag = tagService.getById(tagId);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            if (tagService.checkedAndIgnoredContainTag(tagId, user.getId())) {
                IgnoredTag ignoredTag = new IgnoredTag();
                ignoredTag.setIgnoredTag(tag);
                ignoredTag.setUser(user);
                ignoredTagService.persist(ignoredTag);
            }
            TagDto tagDto = tagConverter.tagToTagDto(tag);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Тэг с tagId=" + tagId + " не найден", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/latter")
    @ApiOperation("Поиск тегов по букве или слову. " +
            "Выдается 10 самых популярных тегов для текущего поиска. " +
            "В качестве параметров передается searchString - строка или буква.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все тэги, содержащие в названии searchString=*"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: searchString")
    })
    public ResponseEntity<List<TagDto>> getTop10FoundTags(@RequestParam(value = "searchString") String searchString) {
        List<TagDto> tagDtos = tagDtoService.getTop10FoundTags(searchString);
        return new ResponseEntity<>(tagDtos, HttpStatus.OK);
    }

    @GetMapping("/popular")
    @ApiOperation("Выводит все тэги, отсортированные по популярности, с учетом заданных параметров пагинации, " +
            " где популярность определяется количеством вопросов закрепеленных за тегом")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все тэги, отсортированные по популярности, с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<TagViewDto>> getAllTagsOrderByPopularPagination(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "items", required = false, defaultValue = "10") Integer items,
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);
        params.put("tagsFilter", filter);

        PageDto<TagViewDto> pageDto = tagDtoService.getPageDto("paginationAllTagsSortedByPopular", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Удаляет тэг из отслеживаемых текущим пользователем по tagId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тэг с tagId=* успешно удален из отслеживаемых"),
            @ApiResponse(code = 400, message = "Тэг с tagId= в отслеживаемых не найден или формат введенного tagId не верный")
    })
    @DeleteMapping("/{tagId}/tracked")
    public ResponseEntity<?> deleteTrackedTagByTagId(@PathVariable("tagId") Long tagId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!trackedTagService.getTagIfNotExist(tagId, user.getId())) {
            return ResponseEntity.badRequest().body("Тэг с tagId=" + tagId + " в отслеживаемых не найден");
        }

        trackedTagService.deleteTrackedTagByTagIdAndUserId(tagId, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(
            value = "Удаляет тэг из игнорируемых текущим пользователем по tagId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Тэг с tagId=* успешно удален из игнорируемых"),
            @ApiResponse(code = 400, message = "Тэг с tagId= в игнорируемых не найден или формат введенного tagId не верный")
    })
    @DeleteMapping("/{tagId}/ignored")
    public ResponseEntity<?> deleteIgnoredTagByTagId(@PathVariable("tagId") Long tagId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!ignoredTagService.getTagIfNotExist(tagId, user.getId())) {
            return ResponseEntity.badRequest().body("Тэг с tagId=" + tagId + " в игнорируемых не найден");
        }

        ignoredTagService.deleteIgnoredTagByTagIdAndUserId(tagId, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/date")
    @ApiOperation("Выводит все тэги, отсортированные по дате добавления, с учетом заданных параметров пагинации, " +
            " где первый тэг, является самым новым")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все тэги, отсортированные по дате, с учетом заданных параметров пагинации"),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы"),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<PageDto<TagViewDto>> getAllTagsOrderByDateDesc(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "items", required = false, defaultValue = "10") Integer items) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", page);
        params.put("itemsOnPage", items);

        PageDto<TagViewDto> pageDto = tagDtoService.getPageDto("paginationAllTagsSortedByDate", params);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
