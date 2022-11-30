package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.service.CommentsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
@Validated
public class AdminCommentsController {

    private CommentsService commentsService;

    @GetMapping
    public List<CommentsDto> getAll(@RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) Long eventId,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                    @RequestParam(required = false) String state) {
        log.info("запрос GET, userId - {}, eventId - {}, from - {}, size - {}, state - {}", userId, eventId, from, size, state);
        return commentsService.getAll(userId, eventId, from, size, state);
    }

    @PatchMapping("/{commentId}/approve")
    public CommentsDto approve(@PathVariable Long commentId) {
        log.info("запрос PATH approve, commentId - {}", commentId);
        return commentsService.approve(commentId);
    }

    @PatchMapping("/{commentId}/hidden")
    public CommentsDto hidden(@PathVariable Long commentId) {
        log.info("запрос PATH hidden, commentId - {}", commentId);
        return commentsService.hidden(commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteAdmin(@PathVariable Long commentId) {
        log.info("запрос DELETE, commentId - {}", commentId);
        commentsService.deleteForAdmin(commentId);
    }
}
