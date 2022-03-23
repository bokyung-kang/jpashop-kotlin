package com.study.jpashop.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.study.jpashop"])
@EnableJpaRepositories(basePackages = ["com.study.jpashop"])
@EntityScan(basePackages = ["com.study.jpashop"])
@EnableJpaAuditing
class JpaShopApplication

fun main(args: Array<String>) {
    runApplication<JpaShopApplication>(*args)
}
