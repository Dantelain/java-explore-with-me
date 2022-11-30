package ru.practicum.explore.with.me.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.comments.dto.CommentsCreateDto;
import ru.practicum.explore.with.me.comments.dto.CommentsDto;
import ru.practicum.explore.with.me.comments.dto.CommentsEditDto;
import ru.practicum.explore.with.me.comments.dto.CommentsMapper;
import ru.practicum.explore.with.me.comments.model.Comment;
import ru.practicum.explore.with.me.comments.model.State;
import ru.practicum.explore.with.me.comments.repo.CommentsRepo;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.events.repo.EventRepo;
import ru.practicum.explore.with.me.exception.NotFoundException;
import ru.practicum.explore.with.me.exception.ValidationException;
import ru.practicum.explore.with.me.requests.model.Status;
import ru.practicum.explore.with.me.requests.repo.RequestsRepo;
import ru.practicum.explore.with.me.users.model.User;
import ru.practicum.explore.with.me.users.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentsServiceImpl implements CommentsService {

    private CommentsRepo commentsRepo;
    private EventRepo eventRepo;
    private RequestsRepo requestsRepo;
    private UserRepo userRepo;

    @Override
    public List<CommentsDto> getAll(Long userId, Long eventId, Integer from, Integer size, String state) {
        int page = from / size;
        Page<Comment> commentList = commentsRepo.findAll((root, query, cb) ->
                        cb.and(
                                (userId != null) ? cb.equal(root.get("author").get("id"), userId) : root.isNotNull(),
                                (eventId != null) ? cb.equal(root.get("event").get("id"), eventId) : root.isNotNull(),
                                (state != null) ? cb.equal(root.get("state"), State.valueOf(state)) : cb.equal(root.get("state"), State.APPROVED)
                        ),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")));
        return commentList.stream().map(CommentsMapper::toCommentsDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentsDto> getAllOwner(Long userId, Long eventId, Integer from, Integer size) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Просматривать все комметарии к событию может только владелец события ", userId.toString());
        }
        int page = from / size;
        Page<Comment> commentList = commentsRepo.findAll((root, query, cb) ->
                cb.and(
                        cb.equal(root.get("event").get("id"), eventId),
                        root.get("state").in(State.APPROVED, State.HIDDEN)
                ),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreate")));
        return commentList.stream().map(CommentsMapper::toCommentsDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentsDto approve(Long commentId) {
        Comment comment = commentsRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        comment.setState(State.APPROVED);
        return CommentsMapper.toCommentsDto(commentsRepo.save(comment));
    }

    @Override
    @Transactional
    public CommentsDto hidden(Long commentId) {
        Comment comment = commentsRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        comment.setState(State.HIDDEN);
        return CommentsMapper.toCommentsDto(commentsRepo.save(comment));
    }

    @Override
    @Transactional
    public void deleteForAdmin(Long commentId) {
        commentsRepo.deleteById(commentId);
    }

    @Override
    public void delete(Long userId, Long commentId) {
        commentsRepo.deleteByAuthorAndId(User.builder().id(userId).build(), commentId);
    }

    @Override
    @Transactional
    public CommentsDto create(Long userId, CommentsCreateDto commentsCreateDto) {
        User author = userRepo.findById(commentsCreateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepo.findById(commentsCreateDto.getEventId())
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .dateCreate(LocalDateTime.now())
                .text(commentsCreateDto.getText())
                .state(State.CREATED)
                .build();
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            requestsRepo.findOne((root, query, cb) ->
                            cb.and(
                                    cb.equal(root.get("event"), event),
                                    cb.equal(root.get("requester"), author),
                                    cb.equal(root.get("status"), Status.CONFIRMED)
                            ))
                    .orElseThrow(() -> new NotFoundException("Комментарий на прошедшие события могут оставлять только пользователи посетившие мероприятие"));
        }
        return CommentsMapper.toCommentsDto(commentsRepo.save(comment));
    }

    @Override
    public CommentsDto edit(Long userId, CommentsEditDto commentsEditDto) {
        Comment comment = commentsRepo.findById(commentsEditDto.getId())
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        comment.setState(State.EDITED);
        comment.setEdited(true);
        comment.setText(commentsEditDto.getText());
        comment.setDateEdit(LocalDateTime.now());
        return CommentsMapper.toCommentsDto(commentsRepo.save(comment));
    }

}
