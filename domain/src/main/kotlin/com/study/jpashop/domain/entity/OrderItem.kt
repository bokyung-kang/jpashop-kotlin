package com.study.jpashop.domain.entity

import com.study.jpashop.domain.entity.item.Item
import javax.persistence.*

@Entity
class OrderItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    val item: Item,

    @Column(nullable = false)
    val orderPrice: Int = 0,

    @Column(nullable = false)
    val count: Int = 0,

) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    var order: Order? = null
        protected set

    /**
     * 생성 메서드
     */
    companion object {
        fun create(item: Item, orderPrice: Int, count: Int): OrderItem {
            item.removeStock(count)
            return OrderItem(
                item = item,
                orderPrice = orderPrice,
                count = count
            )
        }
    }

    // 매핑 메서드
    /**
     * 주문 설정
     */
    fun addOrder(order: Order) {
        order.let {
            this.order = order
        }
    }

    // 비스니스 로직
    /**
     * 주문 취소
     */
    fun cancel() {
        item.addStock(count)
    }

    val totalPrice: Int
        get() = orderPrice * count
}