package ru.practicum.explore.with.me.controllers.open;

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
@RequestMapping(path = "/comments")
@Validated
public class CommentsController {

    private CommentsService commentsService;

    @GetMapping("/{eventId}")
    public List<CommentsDto> getAll(@PathVariable Long eventId,
                                    @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("запрос GET, eventId - {}, from - {}, size - {}", eventId, from, size);
        return commentsService.getAll(null, eventId, from, size, null);
    }

}
