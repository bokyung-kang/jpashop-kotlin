package com.study.jpashop.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.jpashop.domain.config.TestConfig
import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Delivery
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderItem
import com.study.jpashop.domain.entity.item.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import javax.persistence.EntityManager

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig::class)
internal class OrderQueryRepositoryTest @Autowired constructor(
    private val em: EntityManager,
    jpaQueryFactory: JPAQueryFactory,
) {

    var orderQueryRepository: OrderQueryRepository = OrderQueryRepository(jpaQueryFactory)

    @BeforeEach
    fun setUp() {
        val member = Member("bokyung", 10, Address("Seoul", "1-2-3", "12345"))
        em.persist(member)

        val book1 = Book.create("Marcin Moskala", "8395452001", "Effective Kotlin", 3000, 200)
        val book2 = Book.create("Marcin Moskala", "839545002", "Effective Kotlin2", 2500, 300)
        em.persist(book1)
        em.persist(book2)

        val orderItem1 = OrderItem.create(book1, 3000, 2)
        val orderItem2 = OrderItem.create(book2, 2500, 3)

        val delivery = Delivery.create(member.address)
        val order = Order.create(member, delivery, orderItem1, orderItem2)
        em.persist(order)
        em.clear()
    }

    @Test
    fun findOrdersQueryDto() {
        assertAll(
            { assertThat(orderQueryRepository.findOrdersQueryDto()).hasSize(1) },
            { assertThat(orderQueryRepository.findOrderItemQueryDto(1L)).hasSize(2) }
        )
    }
}