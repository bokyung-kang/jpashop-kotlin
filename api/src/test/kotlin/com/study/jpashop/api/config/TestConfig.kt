package com.study.jpashop.api.config

import com.querydsl.jpa.impl.JPAQueryFactory
import javax.persistence.*
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 *  @author bokyung
 */
@TestConfiguration
class TestConfig(
    private val em: EntityManager
) {
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}