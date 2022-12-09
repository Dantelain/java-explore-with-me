package ru.practicum.explore.with.me.comments.model;

import lombok.*;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "comment")
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @NotNull
    private LocalDateTime dateCreate;
    private LocalDateTime dateEdit;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(length = 2000)
    @NotNull
    private String text;
    @Builder.Default
    private Boolean edited = false;
    @Enumerated
    @NotNull
    private State state;

}
