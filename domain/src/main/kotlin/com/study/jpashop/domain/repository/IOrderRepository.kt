package com.study.jpashop.domain.repository

import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderStatus
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface IOrderRepository : JpaRepository<Order, Long> {

    // EntityGraph는 LeftOuterJoin만 사용가능. 다른 Join을 이용하는 경우에는 JPQL + FetchJoin을 이용
    @EntityGraph(attributePaths = ["member", "delivery"])
    fun findByStatus(status: OrderStatus): List<Order>
}