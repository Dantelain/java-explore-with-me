package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.categories.dto.CategoriesDto;
import ru.practicum.explore.with.me.categories.service.CategoriesService;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {
    private CategoriesService categoriesService;

    @PostMapping
    public CategoriesDto add(@Valid @RequestBody CategoriesDto categoriesDto) {
        return categoriesService.add(categoriesDto);
    }

    @PatchMapping
    public CategoriesDto edit(@Valid @RequestBody CategoriesDto categoriesDto) {
        return categoriesService.edit(categoriesDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        categoriesService.delete(catId);
    }

}
