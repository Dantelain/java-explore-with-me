package ru.practicum.explore.with.me.configuration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.practicum.explore.with.me.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class StatsHandle implements HandlerInterceptor {

    private final StatsService statsService;

    public StatsHandle(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           HttpServletResponse response, @NonNull Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (response.getStatus() == 200) {
            statsService.statsHit(request);
        }

    }

}
