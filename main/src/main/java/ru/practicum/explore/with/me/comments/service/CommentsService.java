package ru.practicum.explore.with.me.comments.service;

import ru.practicum.explore.with.me.comments.dto.CommentsCreateDto;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.dto.CommentsEditDto;

import java.util.List;

public interface CommentsService {

    List<CommentsDto> getAll(Long userId, Long eventId, Integer from, Integer size, String state);

    List<CommentsDto> getAllOwner(Long userId, Long eventId, Integer from, Integer size);

    CommentsDto approve(Long commentId);

    CommentsDto hidden(Long commentId);

    void deleteForAdmin(Long commentId);

    CommentsDto edit(Long userId, CommentsEditDto commentsEditDto);

    void delete(Long userId, Long commentId);

    CommentsDto create(Long userId, CommentsCreateDto commentsCreateDto);

    CommentsDto getOne(Long commentId);
}
