package ru.practicum.explore.with.me.events.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.explore.with.me.categories.model.Categories;
import ru.practicum.explore.with.me.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation; //Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id")
    @ToString.Exclude
    private Categories categories;
    @Builder.Default
    private Long confirmedRequests = 0L; //Количество одобренных заявок на участие в данном событии
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(length = 7000)
    private String description; //Полное описание события
    private LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid; //Нужно ли оплачивать участие
    @Builder.Default
    private Integer participantLimit = 0; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private Boolean requestModeration = true; //Нужна ли пре-модерация заявок на участие
    @Enumerated
    private State state; //Список состояний жизненного цикла события
    private String title;

    public void incrementConfirmedRequests() {
        confirmedRequests++;
    }

    public void decrementConfirmedRequests() {
        confirmedRequests--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
