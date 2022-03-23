package com.study.jpashop.domain.repository

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Delivery
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderItem
import com.study.jpashop.domain.entity.OrderStatus
import com.study.jpashop.domain.entity.item.Book
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class OrderRepositoryTest @Autowired constructor(
    private val em: EntityManager,
    private var orderRepository: IOrderRepository
) {

    @BeforeEach
    fun setUp() {
        val member1 = Member("bokyung", 10, Address("Seoul", "1-2-3", "12345"))
        val member2 = Member("bokyung2", 10, Address("Seoul", "1-2-3", "12345"))
        em.persist(member1)
        em.persist(member2)

        val book1 = Book.create("Marcin Moskala", "8395452837", "Effective Kotlin1", 3000, 100)
        val book2 = Book.create("Marcin Moskala", "8395452837", "Effective Kotlin2", 2500, 200)
        val book3 = Book.create("Marcin Moskala", "8395452837", "Effective Kotlin3", 2000, 300)
        val book4 = Book.create("Marcin Moskala", "8395452837", "Effective Kotlin4", 1500, 400)
        em.persist(book1)
        em.persist(book2)
        em.persist(book3)
        em.persist(book4)

        orderRepository.save(
            Order.create(
                member1,
                Delivery(member1.address),
                OrderItem.create(book1, book1.price, 2),
                OrderItem.create(book2, book2.price, 5)
            )
        )
        orderRepository.save(
            Order.create(
                member2,
                Delivery(member2.address),
                OrderItem.create(book3, book3.price, 10),
                OrderItem.create(book4, book4.price, 15)
            )
        )
        em.flush()
        em.clear()
    }

    @Test
    fun findByStatus() {
        assertAll(
            { Assertions.assertThat(orderRepository.findByStatus(OrderStatus.ORDER)).hasSize(2) }
        )
    }
}