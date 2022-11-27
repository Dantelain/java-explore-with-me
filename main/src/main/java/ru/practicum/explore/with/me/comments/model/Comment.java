package ru.practicum.explore.with.me.comments.model;

import lombok.*;
import ru.practicum.explore.with.me.events.model.Event;
import ru.practicum.explore.with.me.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    private LocalDateTime dateCreate;
    private LocalDateTime dateEdit;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(length = 2000)
    private String text;
    @Builder.Default
    private Boolean edited = false;
    @Enumerated
    private State state;

}
