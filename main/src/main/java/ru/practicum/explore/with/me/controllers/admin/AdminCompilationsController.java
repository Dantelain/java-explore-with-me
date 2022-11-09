package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.compilations.service.CompilationsService;
import ru.practicum.explore.with.me.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.compilations.dto.NewCompilationsDto;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationsController {

    private CompilationsService compilationsService;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationsDto newCompilationsDto) {
        return compilationsService.add(newCompilationsDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        compilationsService.delete(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationsService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationsService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unPin(@PathVariable Long compId) {
        compilationsService.unPin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(@PathVariable Long compId) {
        compilationsService.pin(compId);
    }

}
