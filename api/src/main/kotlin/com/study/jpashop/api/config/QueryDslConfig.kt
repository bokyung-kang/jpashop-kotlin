package com.study.jpashop.api.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QueryDslConfig(
    private val em: EntityManager,
) {
    @Bean
    fun queryFactory() = JPAQueryFactory(em)
}