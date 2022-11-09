package ru.practicum.explore.with.me.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.explore.with.me.stats.StatsService;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StatsService statsService;

    public WebConfig(StatsService statsService) {
        this.statsService = statsService;
    }

    @Bean
    StatsHandle getSessionManager() {
        return new StatsHandle(statsService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSessionManager())
                .addPathPatterns("/events","/events/**")
                .excludePathPatterns("/resources/**");
    }

}
