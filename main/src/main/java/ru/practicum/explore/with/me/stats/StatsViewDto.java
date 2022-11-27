package ru.practicum.explore.with.me.stats;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsViewDto {

    private String app;
    private String uri;
    @Builder.Default
    private Long hits = 0L;

}
