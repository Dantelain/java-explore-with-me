package ru.practicum.explore.with.me.compilations.model;

import lombok.*;
import ru.practicum.explore.with.me.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Compilations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Event> events;
    private Boolean pinned;
    private String title;

}
