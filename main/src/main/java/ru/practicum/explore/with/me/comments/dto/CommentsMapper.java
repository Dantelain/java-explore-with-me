package ru.practicum.explore.with.me.comments.dto;

import ru.practicum.explore.with.me.comments.model.Comment;

public class CommentsMapper {

    public static CommentsDto toCommentsDto(Comment comment) {
        return CommentsDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .dateCreate(comment.getDateCreate())
                .dateEdit(comment.getDateEdit())
                .text(comment.getText())
                .edited(comment.getEdited())
                .state(comment.getState())
                .build();
    }

}
