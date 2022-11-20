package ru.practicum.explore.with.me.compilations.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.compilations.dto.CompilationsMapper;
import ru.practicum.explore.with.me.compilations.dto.NewCompilationsDto;
import ru.practicum.explore.with.me.compilations.model.Compilations;
import ru.practicum.explore.with.me.compilations.repo.CompilationRepo;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.exception.NotFoundException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CompilationsServiceImpl implements CompilationsService {

    private CompilationRepo compilationRepo;
    private EntityManager entityManager;

    @Transactional
    @Override
    public CompilationDto add(NewCompilationsDto newCompilationsDto) {
        Compilations compilations = compilationRepo.saveAndFlush(CompilationsMapper.toCompilations(newCompilationsDto));
        entityManager.refresh(compilations);
        return CompilationsMapper.toCompilationDto(compilations);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        return compilationRepo.findAll(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pinned"), pinned),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(CompilationsMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompilationDto getOne(Long compId) {
        Compilations compilations = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        log.info("compilations - {}", compilations.getEvents());
        return CompilationsMapper.toCompilationDto(compilations);
    }

    @Override
    public void delete(Long compId) {
        compilationRepo.deleteById(compId);
    }

    @Override
    public void deleteEvent(Long compId, Long eventId) {
        Compilations compilations = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        Set<Event> events = compilations.getEvents();
        events.remove(Event.builder().id(eventId).build());
        compilationRepo.save(compilations);
    }

    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilations compilations = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        Set<Event> events = compilations.getEvents();
        events.add(Event.builder().id(eventId).build());
        compilationRepo.save(compilations);
    }

    @Override
    public void unPin(Long compId) {
        Compilations compilations = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        compilations.setPinned(false);
        compilationRepo.save(compilations);
    }

    @Override
    public void pin(Long compId) {
        Compilations compilations = compilationRepo.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        compilations.setPinned(true);
        compilationRepo.save(compilations);
    }


}
