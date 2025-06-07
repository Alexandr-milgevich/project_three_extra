package com.gigabank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации параметров пагинации для контроллеров Spring MVC.
 */
@Configuration
public class PaginationConfig implements WebMvcConfigurer {
    /**
     * Настройка параметров пагинации для контроллеров Spring MVC.
     *
     * @return кастомизатор для PageableHandlerMethodArgumentResolver,
     * который управляет параметрами пагинации в запросах.
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return resolver -> {
            // Нумерация страницы с 1
            resolver.setOneIndexedParameters(true);
            // Значение по умолчанию для Pageable,
            // если в запросе не переданы параметры пагинации:
            // страница 0 (первая страница в базе, несмотря на 1-индексацию параметров),
            // размер страницы 10 элементов
            resolver.setFallbackPageable(PageRequest.of(0, 10));
            // Максимальный размер страницы — 100 элементов,
            // если клиент запросит больше, это ограничение не позволит этого сделать
            resolver.setMaxPageSize(100); // максимум на странице
        };
    }
}