package ru.practicum.explore.with.me.requests.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.requests.model.Request;
import ru.practicum.explore.with.me.users.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestsRepo extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

    List<Request> findAllByRequester(User user);

    Optional<Request> findByIdAndRequester(Long id, User user);

    Optional<Request> findByRequesterAndEvent(User user, Event event);
}
