package com.study.jpashop.domain.entity.dto

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderItem
import com.study.jpashop.domain.entity.OrderStatus
import java.time.LocalDateTime

data class OrderDto(
    var orderId: Long,
    var name: String,
    var age: Int,
    var orderDate: LocalDateTime,
    var orderStatus: OrderStatus,
    var address: Address,
    var orderItems: List<OrderItemDto>,
) {

    constructor(projection: OrderProjection, orderItems: List<OrderItemProjection>) : this(
        orderId = projection.orderId,
        name = projection.name,
        age = projection.age,
        orderDate = projection.orderDate,
        orderStatus = projection.orderStatus,
        address = projection.address,
        orderItems = orderItems.map(::OrderItemDto)
    )

    constructor(order: Order) : this(
        orderId = order.id,
        name = order.member.name,
        age = order.member.age,
        orderDate = order.orderDate,
        orderStatus = order.status,
        address = order.delivery.address,
        orderItems = order.orderItems.map(::OrderItemDto)
    )
}

data class OrderItemDto(
    var itemName: String,
    var orderPrice: Int,
    var count: Int,
) {

    constructor(projection: OrderItemProjection) : this(
        itemName = projection.itemName,
        orderPrice = projection.orderPrice,
        count = projection.count
    )

    constructor(orderItem: OrderItem) : this(
        itemName = orderItem.item.name ?: "",
        orderPrice = orderItem.orderPrice,
        count = orderItem.count
    )
}