package ru.practicum.explore.with.me.categories.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.categories.model.Categories;

public interface CategoriesRepo extends JpaRepository<Categories, Long> {
}
