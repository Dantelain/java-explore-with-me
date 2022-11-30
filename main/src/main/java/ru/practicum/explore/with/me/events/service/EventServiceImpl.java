package ru.practicum.explore.with.me.events.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.categories.model.Categories;
import ru.practicum.explore.with.me.events.dto.*;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.events.model.EventSort;
import ru.practicum.explore.with.me.events.model.Location;
import ru.practicum.explore.with.me.events.model.State;
import ru.practicum.explore.with.me.events.repo.EventRepo;
import ru.practicum.explore.with.me.exception.NotFoundException;
import ru.practicum.explore.with.me.exception.ValidationException;
import ru.practicum.explore.with.me.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private EventRepo eventRepo;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EventFullDto getOne(Long eventId) {
        Event event = eventRepo.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(List<Long> users,
                                     List<String> states,
                                     List<Long> categories,
                                     String rangeStart,
                                     String rangeEnd,
                                     Integer from,
                                     Integer size) {
        int page = from / size;
        LocalDateTime currentDate = LocalDateTime.now();
        Page<Event> events = eventRepo.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                (users != null) ? root.get("initiator").in(users) : root.isNotNull(),
                                (states != null) ? root.get("state").in(states.stream().map(el -> State.valueOf(el).ordinal()).collect(Collectors.toList())) : root.isNotNull(),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                criteriaBuilder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentDate)
                        ),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto publish(Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto reject(Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<EventShortDto> getAll(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      String rangeStart,
                                      String rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      Integer from,
                                      Integer size) {
        int page = from / size;
        LocalDateTime currentDate = LocalDateTime.now();
        EventSort eventSort;
        try {
            eventSort = EventSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Использован недопустимый вариант сортировки", sort);
        }
        Sort mySort;
        switch (eventSort) {
            case EVENT_DATE:
                mySort = Sort.by(Sort.Direction.ASC, "eventDate");
                break;
            case VIEWS:
                mySort = Sort.by(Sort.Direction.ASC, "views");
                break;
            default:
                mySort = Sort.by(Sort.Direction.ASC, "id");
        }
        Page<Event> events = eventRepo.findAll((root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("state"), State.PUBLISHED.ordinal()),
                                (categories != null) ? root.get("categories").in(categories) : root.isNotNull(),
                                (paid != null) ? criteriaBuilder.equal(root.get("paid"), paid) : root.isNotNull(),
                                (!rangeStart.isEmpty() && !rangeEnd.isEmpty()) ?
                                        criteriaBuilder.and(
                                                criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                criteriaBuilder.lessThan(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                        ) : criteriaBuilder.lessThan(root.get("eventDate"), currentDate),
                                (onlyAvailable) ? criteriaBuilder.or(
                                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                                        criteriaBuilder.and(
                                                criteriaBuilder.notEqual(root.get("participantLimit"), 0),
                                                criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
                                        )) : root.isNotNull(),
                                (text != null) ? criteriaBuilder.or(
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                                ) : root.isNotNull()),
                PageRequest.of(page, size, mySort));
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        int page = from / size;
        return eventRepo.findAll(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("initiator"),
                                User.builder().id(userId).build()), PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto edit(Long userId, EditEventDto editEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(editEventDto.getEventDate())) {
            throw new ValidationException("Дата события должна быть позже 2-х часов от текущей", editEventDto.getEventDate().toString());
        }
        Event event = eventRepo.findById(editEventDto.getEventId())
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Редактировать может только владелец события " + event.getInitiator(), userId.toString());
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Изменить можно только отмененные события или события в состоянии ожидания модерации", event.getState().toString());
        }
        if (event.getState().equals(State.CANCELED)) {
            event.setState(State.PENDING);
        }
        verifyChange(event, editEventDto);
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto edit(Long eventId, NewEventDto newEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEventDto.getEventDate())) {
            throw new ValidationException("Дата события должна быть позже 2-х часов от текущей", newEventDto.getEventDate().toString());
        }
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        verifyChange(event, newEventDto);
        if (newEventDto.getLocation() != null) {
            Location location = Location.builder()
                    .lon(newEventDto.getLocation().getLon())
                    .lat(newEventDto.getLocation().getLat())
                    .build();
            event.setLocation(location);
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto getOne(Long userId, Long eventId) {
        Event event = eventRepo.findByIdAndInitiator(eventId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        Event event = eventRepo.findByIdAndInitiator(eventId, User.builder().id(userId).build())
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEventDto.getEventDate())) {
            throw new ValidationException("Дата события должна быть позже 2-х часов от текущей", newEventDto.getEventDate().toString());
        }
        Event event = EventMapper.toEvent(userId, newEventDto);
        return EventMapper.toEventFullDto(eventRepo.save(event));
    }


    private <T extends EventBaseDto> void verifyChange(Event event, T dto) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isEmpty()) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategories(Categories.builder().id(dto.getCategory()).build());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            event.setTitle(dto.getTitle());
        }
    }

}
