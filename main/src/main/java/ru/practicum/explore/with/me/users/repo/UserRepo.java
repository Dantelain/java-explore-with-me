package ru.practicum.explore.with.me.users.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.users.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
