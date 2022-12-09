package ru.practicum.explore.with.me.controllers.secret;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.comments.dto.CommentsCreateDto;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.dto.CommentsEditDto;
import ru.practicum.explore.with.me.comments.service.CommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
@Validated
public class UserCommentsController {

    private CommentsService commentsService;

    @GetMapping
    public List<CommentsDto> getAll(@PathVariable Long userId,
                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") @PositiveOrZero Integer size,
                                    @RequestParam(required = false, defaultValue = "ALL") @Positive String state) {
        log.warn("запрос GET, userId - {}, from - {}, size - {}, state - {}", userId, from, size, state);
        return commentsService.getAll(userId, null, from, size, state);
    }

    @PostMapping
    public CommentsDto create(@PathVariable Long userId, @Valid @RequestBody CommentsCreateDto commentsCreateDto) {
        log.warn("запрос POST, userId - {}, commentsCreateDto - {}", userId, commentsCreateDto);
        return commentsService.create(userId, commentsCreateDto);
    }

    @PatchMapping
    public CommentsDto edit(@PathVariable Long userId, @Valid @RequestBody CommentsEditDto commentsEditDto) {
        log.warn("запрос PUT, userId - {}, commentsEditDto - {}", userId, commentsEditDto);
        return commentsService.edit(userId, commentsEditDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.warn("запрос PUT, userId - {}, commentId - {}", userId, commentId);
        commentsService.delete(userId, commentId);
    }

    @GetMapping("/{eventId}")
    public List<CommentsDto> getAllCommentEvent(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.warn("запрос GET, userId - {}, eventId - {}, from - {}, size - {}", userId, eventId, from, size);
        return commentsService.getAllOwner(userId, eventId, from, size);
    }

}
