package com.study.jpashop.domain.entity

import javax.persistence.*

@Entity
class Delivery(

    @Embedded
    val address: Address,

    @Enumerated(EnumType.STRING)
    val status: DeliveryStatus = DeliveryStatus.READY,
) : BaseEntity() {

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null
        protected set

    // 매핑 메서드
    fun addOrder(order: Order) {
        order.let {
            this.order = order
        }
    }

    companion object {
        fun create(address: Address) =
            Delivery(address = address)
    }
}
