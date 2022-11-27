package ru.practicum.explore.with.me.comments.service;

import ru.practicum.explore.with.me.comments.dto.CommentsCreateDto;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.dto.CommentsEditDto;

import java.util.List;

public interface CommentsService {

    List<CommentsDto> getAll(Long userId, Long eventId, Integer from, Integer size, String state);

    CommentsDto moderation(Long commentId, String state);

    void delete(Long commentId);

    CommentsDto edit(Long userId, CommentsEditDto commentsEditDto);

    void delete(Long userId, Long commentId);

    CommentsDto create(Long userId, CommentsCreateDto commentsCreateDto);
}
