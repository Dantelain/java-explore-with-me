package ru.practicum.explore.with.me.controllers.open;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.compilations.dto.CompilationDto;
import ru.practicum.explore.with.me.compilations.service.CompilationsService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationsController {

    private CompilationsService compilationsService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        return compilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getOne(@PathVariable Long compId) {
        return compilationsService.getOne(compId);
    }
}
