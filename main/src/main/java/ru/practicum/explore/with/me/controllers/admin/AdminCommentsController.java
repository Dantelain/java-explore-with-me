package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.service.CommentsService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentsController {

    private CommentsService commentsService;

    @GetMapping
    public List<CommentsDto> getAll(@RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) Long eventId,
                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String state) {
        log.info("запрос GET, userId - {}, eventId - {}, from - {}, size - {}, state - {}", userId, eventId, from, size, state);
        return commentsService.getAll(userId, eventId, from, size, state);
    }

    @PatchMapping("/{commentId}/{state}")
    public CommentsDto moderation(@PathVariable Long commentId, @PathVariable String state) {
        log.info("запрос PATH, commentId - {}, state - {}", commentId, state);
        return commentsService.moderation(commentId, state);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        log.info("запрос DELETE, commentId - {}", commentId);
        commentsService.delete(commentId);
    }
}
