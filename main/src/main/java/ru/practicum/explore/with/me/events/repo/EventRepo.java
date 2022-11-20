package ru.practicum.explore.with.me.events.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.events.model.State;
import ru.practicum.explore.with.me.users.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByIdAndInitiator(Long eventId, User user);

    Optional<Event> findByIdAndState(Long eventId, State state);

    List<Event> findAllByInitiator(User user);
}
