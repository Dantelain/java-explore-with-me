package ru.practicum.explore.with.me.compilations.service;

import ru.practicum.explore.with.me.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.compilations.dto.NewCompilationsDto;

import java.util.List;

public interface CompilationsService {
    CompilationDto add(NewCompilationsDto newCompilationsDto);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getOne(Long compId);

    void delete(Long compId);

    void deleteEvent(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);

    void unPin(Long compId);

    void pin(Long compId);
}
