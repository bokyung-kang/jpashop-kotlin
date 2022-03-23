package com.study.jpashop.domain.entity

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Member(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    val age: Int = 0,

    @Embedded
    var address: Address,

) : BaseEntity() {

    @OneToMany(mappedBy = "member")
    private val _orders: MutableList<Order> = mutableListOf()

    /**
     * backing property를 이용해서 immutable한 List를 취득하도록 한다
     */
    val orders: List<Order>
        get() = _orders.toList()

    // 매핑 메서드
    fun addOrder(order: Order) {
        order.let {
            _orders.add(order)
        }
    }
}