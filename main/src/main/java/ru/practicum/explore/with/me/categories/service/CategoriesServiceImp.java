package ru.practicum.explore.with.me.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.categories.dto.CategoriesDto;
import ru.practicum.explore.with.me.categories.dto.CategoriesMapper;
import ru.practicum.explore.with.me.categories.model.Categories;
import ru.practicum.explore.with.me.categories.repo.CategoriesRepo;
import ru.practicum.explore.with.me.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoriesServiceImp implements CategoriesService {

    private CategoriesRepo categoriesRepo;

    @Override
    public CategoriesDto add(CategoriesDto categoriesDto) {
        Categories categories = categoriesRepo.save(CategoriesMapper.toCategories(categoriesDto));
        return CategoriesMapper.toCategoriesDto(categories);
    }

    @Override
    public CategoriesDto edit(CategoriesDto categoriesDto) {
        Categories categories = categoriesRepo.findById(categoriesDto.getId())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (categoriesDto.getName() != null && !categoriesDto.getName().isEmpty()) {
            categories.setName(categoriesDto.getName());
        }
        categoriesRepo.saveAndFlush(categories);
        return CategoriesMapper.toCategoriesDto(categories);
    }

    @Override
    public void delete(Long catId) {
        categoriesRepo.deleteById(catId);
    }

    @Override
    public CategoriesDto getOne(Long catId) {
        Categories categories = categoriesRepo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        return CategoriesMapper.toCategoriesDto(categories);
    }

    @Override
    public List<CategoriesDto> getAll(Integer from, Integer size) {
        int page = from / size;
        return categoriesRepo.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map(CategoriesMapper::toCategoriesDto)
                .collect(Collectors.toList());

    }

}
