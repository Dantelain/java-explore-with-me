package ru.practicum.explore.with.me.controllers.secret;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.comments.dto.CommentsCreateDto;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.dto.CommentsEditDto;
import ru.practicum.explore.with.me.comments.service.CommentsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class UserCommentsController {

    private CommentsService commentsService;

    @GetMapping
    public List<CommentsDto> getAll(@PathVariable Long userId,
                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("запрос GET, userId - {}, from - {}, size - {}, state - {}", userId, from, size, state);
        return commentsService.getAll(userId, null, from, size, state);
    }

    @PostMapping
    public CommentsDto create(@PathVariable Long userId, @Valid @RequestBody CommentsCreateDto commentsCreateDto) {
        log.info("запрос POST, userId - {}, commentsCreateDto - {}", userId, commentsCreateDto);
        return commentsService.create(userId, commentsCreateDto);
    }

    @PutMapping
    public CommentsDto edit(@PathVariable Long userId, @Valid @RequestBody CommentsEditDto commentsEditDto) {
        log.info("запрос PUT, userId - {}, commentsEditDto - {}", userId, commentsEditDto);
        return commentsService.edit(userId, commentsEditDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("запрос PUT, userId - {}, commentId - {}", userId, commentId);
        commentsService.delete(userId, commentId);
    }

}
