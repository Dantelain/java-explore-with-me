package ru.practicum.explore.with.me.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.with.me.dto.StatsViewDto;
import ru.practicum.explore.with.me.model.StatsHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepo extends JpaRepository<StatsHit, Long>, JpaSpecificationExecutor<StatsHit> {

    @Query("SELECT new ru.practicum.explore.with.me.dto.StatsViewDto(st.app, st.uri, COUNT(st.ip)) " +
            "FROM StatsHit AS st " +
            "WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatsViewDto> countTotalIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.explore.with.me.dto.StatsViewDto(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM StatsHit AS st " +
            "WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatsViewDto> countTotalIpDistinct(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

}
