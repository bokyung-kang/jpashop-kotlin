package com.study.jpashop.domain.entity

import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.extensions.validate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    member: Member,

    delivery: Delivery,

    @Column(nullable = false)
    val orderDate: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.ORDER,

) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    var member: Member = member
        protected set(value) {
            field = value
            member.orders.let {
                member.addOrder(this)
            }
        }

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var delivery: Delivery = delivery
        protected set(value) {
            field = value
            delivery.addOrder(this)
        }

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    private val _orderItems: MutableList<OrderItem> = mutableListOf()

    /**
     * backing property를 이용해서 immutable한 List를 취득하도록 한다
     */
    val orderItems: List<OrderItem>
        get() = _orderItems.toList()

    /**
     * 생성 메서드
     */
    companion object {
        fun create(member: Member, delivery: Delivery, vararg orderItems: OrderItem): Order {
            val order = Order(
                member = member,
                delivery = delivery
            )
            // 매핑 설정
            order.member = member
            order.delivery = delivery
            orderItems.forEach { order.addOrderItem(it) }
            return order
        }
    }

    // 매핑 메서드
    private fun addOrderItem(orderItem: OrderItem) {
        orderItem.let {
            _orderItems.add(orderItem)
            orderItem.addOrder(this)
        }
    }

    // 비즈니스 로직
    /**
     * 주문 취소
     */
    fun cancel() {
        validate(delivery.status != DeliveryStatus.COMP) { ErrorCode.ALREADY_DELIVERED }
        status = OrderStatus.CANCEL
        for (orderItem: OrderItem in orderItems) {
            orderItem.cancel()
        }
    }

    val totalPrice: Int
        get() = orderItems.sumOf(OrderItem::totalPrice)
}