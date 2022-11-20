package ru.practicum.explore.with.me.controllers.open;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.categories.dto.CategoriesDto;
import ru.practicum.explore.with.me.categories.service.CategoriesService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesController {

    private CategoriesService categoriesService;

    @GetMapping("/{catId}")
    public CategoriesDto getOne(@PathVariable Long catId) {
        return categoriesService.getOne(catId);
    }

    @GetMapping
    public List<CategoriesDto> getAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoriesService.getAll(from, size);
    }
}
