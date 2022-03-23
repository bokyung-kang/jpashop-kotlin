package com.study.jpashop.domain.entity.dto

import com.querydsl.core.annotations.QueryProjection
import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.OrderStatus
import java.time.LocalDateTime

data class OrderProjection @QueryProjection constructor(
    var orderId: Long,
    var name: String,
    var age: Int,
    var orderDate: LocalDateTime,
    var orderStatus: OrderStatus,
    var address: Address,
)

data class OrderItemProjection @QueryProjection constructor(
    var orderId: Long,
    var itemName: String,
    var orderPrice: Int,
    var count: Int,
)