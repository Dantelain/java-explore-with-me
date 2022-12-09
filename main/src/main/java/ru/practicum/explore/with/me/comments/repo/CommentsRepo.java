package ru.practicum.explore.with.me.comments.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.with.me.comments.model.Comment;
import ru.practicum.explore.with.me.users.model.User;

public interface CommentsRepo extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    void deleteByIdAndAuthor(Long id, User author);
}
