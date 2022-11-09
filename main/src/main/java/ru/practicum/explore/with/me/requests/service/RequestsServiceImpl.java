package ru.practicum.explore.with.me.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.events.model.State;
import ru.practicum.explore.with.me.events.repo.EventRepo;
import ru.practicum.explore.with.me.exception.NotFoundException;
import ru.practicum.explore.with.me.exception.ValidationException;
import ru.practicum.explore.with.me.requests.dto.RequestDto;
import ru.practicum.explore.with.me.requests.dto.RequestMapper;
import ru.practicum.explore.with.me.requests.model.Request;
import ru.practicum.explore.with.me.requests.model.Status;
import ru.practicum.explore.with.me.requests.repo.RequestsRepo;
import ru.practicum.explore.with.me.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private RequestsRepo requestsRepo;
    private EventRepo eventRepo;

    @Override
    public List<RequestDto> getAll(Long userId) {
        List<Request> requestList = requestsRepo.findAllByRequester(User.builder().id(userId).build());
        return requestList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAll(Long userId, Long eventId) {
        List<Event> events = eventRepo.findAllByInitiator(User.builder().id(userId).build());
        List<Request> requestList = requestsRepo.findAll((root, query, criteriaBuilder) ->
                root.get("event").in(events.stream().map(Event::getId).collect(Collectors.toList())));
        return requestList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = eventOne(eventId, userId, true);
        Long requestsCount = requestsCount(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestsCount) {
            throw new ValidationException("У события достигнут лимит запросов на участие", event.getParticipantLimit().toString());
        }
        Request request = requestOne(reqId, event);
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            return RequestMapper.toRequestDto(request);
        }
        request.setStatus(Status.CONFIRMED);
        RequestDto requestDto = RequestMapper.toRequestDto(requestsRepo.save(request));
        event.incrementConfirmedRequests();
        eventRepo.save(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == requestsCount + 1) {
            List<Request> requestList = requestsRepo.findAll(((root, query, criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("event"), event.getId()),
                            criteriaBuilder.equal(root.get("status"), Status.PENDING.ordinal())
                    )));
            requestList.forEach(el -> el.setStatus(Status.REJECTED));
            requestsRepo.saveAll(requestList);
        }
        return requestDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = eventOne(eventId, userId, true);
        Request request = requestOne(reqId, event);
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementConfirmedRequests();
            eventRepo.save(event);
        }
        request.setStatus(Status.REJECTED);
        return RequestMapper.toRequestDto(requestsRepo.save(request));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto add(Long userId, Long eventId) {
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(Event.builder().id(eventId).build())
                .requester(User.builder().id(userId).build())
                .status(Status.PENDING)
                .build();
        Event event = eventOne(eventId, userId, false);
        Long requestsCount = requestsCount(event);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestsCount) {
            throw new ValidationException("У события достигнут лимит запросов на участие", event.getParticipantLimit().toString());
        }
        requestsRepo.findByRequesterAndEvent(
                        User.builder().id(userId).build(),
                        Event.builder().id(eventId).build())
                .ifPresent(rq -> {
                    throw new ValidationException("Нельзя добавить повторный запрос", "");
                });
        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            event.incrementConfirmedRequests();
            eventRepo.save(event);
        }
        return RequestMapper.toRequestDto(requestsRepo.save(request));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RequestDto cancel(Long userId, Long requestId) {
        Request request = requestsRepo.findByIdAndRequester(requestId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        Event event = eventRepo.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), request.getEvent().getId()),
                        criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal())
                ))).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (request.getStatus().equals(Status.CONFIRMED)) {
            event.decrementConfirmedRequests();
            eventRepo.save(event);
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(requestsRepo.save(request));
    }

    private Event eventOne(Long eventId, Long userId, Boolean initiator) {
        return eventRepo.findOne(((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), eventId),
                        (initiator) ? criteriaBuilder.equal(root.get("initiator"), userId) : criteriaBuilder.notEqual(root.get("initiator"), userId),
                        criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal())
                ))).orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private Request requestOne(Long reqId, Event event) {
        return requestsRepo.findOne((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), reqId),
                        criteriaBuilder.equal(root.get("event"), event.getId())
                )).orElseThrow(() -> new NotFoundException("Запрос не найден"));
    }

    private Long requestsCount(Event event) {
        return requestsRepo.count((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("event"), event.getId()),
                        criteriaBuilder.equal(root.get("status"), Status.CONFIRMED.ordinal())
                ));
    }

}
