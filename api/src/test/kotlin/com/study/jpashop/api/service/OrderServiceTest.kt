package com.study.jpashop.api.service

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.entity.OrderStatus
import com.study.jpashop.domain.entity.item.Book
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.exception.JpaStudyException
import com.study.jpashop.domain.extensions.findOrder
import com.study.jpashop.domain.repository.IOrderRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var orderRepository: IOrderRepository

    @Test
    @DisplayName("주문")
    fun order() {

        // given
        val member = createMember()
        val book = createBook()
        val orderCount = 2

        // when
        val orderId = orderService.order(member.id, book.id, orderCount)

        // then
        val findOrder = orderRepository.findOrder(orderId)

        assertThat(findOrder.status).isEqualTo(OrderStatus.ORDER)
        assertThat(findOrder.orderItems.size).isEqualTo(1)
        assertThat(findOrder.totalPrice).isEqualTo(3000 * orderCount)
        assertThat(book.stockQuantity).isEqualTo(98)
        assertThat(findOrder.member.orders[0].id).isEqualTo(orderId)
        assertThat(findOrder.delivery.order?.id).isEqualTo(orderId)
    }

    @Test
    @DisplayName("주문 아이템 변경")
    fun modifyItem() {
        // given
        val member = createMember()
        val book = createBook()
        book.change(name = "Marcin Moskala111", price = 3400, stockQuantity = 200)
        val orderCount = 2

        // when
        val orderId = orderService.order(member.id, book.id, orderCount)

        // then
        val findOrder = orderRepository.findOrder(orderId)

        assertThat(findOrder.status).isEqualTo(OrderStatus.ORDER)
        assertThat(findOrder.orderItems.size).isEqualTo(1)
        assertThat(findOrder.totalPrice).isEqualTo(3400 * orderCount)
        assertThat(book.stockQuantity).isEqualTo(198)
        assertThat(findOrder.member.orders[0].id).isEqualTo(orderId)
        assertThat(findOrder.delivery.order?.id).isEqualTo(orderId)
    }

    @Test
    @DisplayName("재고 없음")
    fun outOfStock() {

        // given
        val member = createMember()
        val book = createBook()
        val orderCount = 101

        // when
        val thrown = Assertions.assertThrows(JpaStudyException::class.java) {
            orderService.order(member.id, book.id, orderCount)
        }

        // then
        assertThat(ErrorCode.OUT_OF_STOCK).isEqualTo(thrown.errorCode)
    }

    @Test
    @DisplayName("주문 취소")
    fun cancel() {

        // given
        val member = createMember()
        val book = createBook()
        val orderCount = 2
        val orderId = orderService.order(member.id, book.id, orderCount)

        // when
        orderService.cancelOrder(orderId)

        // then
        val findOrder = orderRepository.findOrder(orderId)

        assertThat(findOrder.status).isEqualTo(OrderStatus.CANCEL)
        assertThat(book.stockQuantity).isEqualTo(100)
    }

    private fun createMember(): Member {
        val member = Member("bokyung", 10, Address("Seoul", "1-2-3", "12345"))
        em.persist(member)
        return member
    }

    private fun createBook(): Book {
        val book = Book("Effective Kotlin", 3000, 100, "Marcin Moskala", "8395452837")
        em.persist(book)
        return book
    }
}