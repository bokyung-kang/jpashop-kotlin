package com.study.jpashop.api

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Delivery
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderItem
import com.study.jpashop.domain.entity.item.Book
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Profile("local")
@Component
class InitDb(private val initService: InitService) {

    @PostConstruct
    fun init() {
        initService.initOrder()
    }

    @Component
    @Transactional
    class InitService(private val em: EntityManager) {
        fun initOrder() {
            for (i in 1..100) {
                val no = i.toString().padStart(3, '0')
                val member = Member("bokyung$i", i, Address("Seoul", "1-2-3", "12345"))
                em.persist(member)

                val book1 = Book.create("Marcin Moskala", "8395452$no", "Effective Kotlin$i-1", 3000, 200)
                val book2 = Book.create("Marcin Moskala", "8395452$no", "Effective Kotlin$i-2", 2500, 300)
                em.persist(book1)
                em.persist(book2)

                val orderItem1 = OrderItem.create(book1, 3000, 2)
                val orderItem2 = OrderItem.create(book2, 2500, 3)

                val delivery = Delivery.create(member.address)
                val order = Order.create(member, delivery, orderItem1, orderItem2)
                em.persist(order)
            }
        }
    }
}