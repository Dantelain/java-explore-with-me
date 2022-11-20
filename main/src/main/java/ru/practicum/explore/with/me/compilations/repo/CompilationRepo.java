package ru.practicum.explore.with.me.compilations.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.compilations.model.Compilations;

public interface CompilationRepo extends JpaRepository<Compilations, Long>, JpaSpecificationExecutor<Compilations> {
}
