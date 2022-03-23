package com.study.jpashop.domain.entity.dto

import com.study.jpashop.domain.entity.OrderStatus

data class OrderSearchRequest(
    var name: String?,
    var status: OrderStatus?,
    var ageGoe: Int?,
    var ageLoe: Int?,
)