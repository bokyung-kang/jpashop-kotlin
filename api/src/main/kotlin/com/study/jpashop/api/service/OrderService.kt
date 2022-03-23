package com.study.jpashop.api.service

import com.study.jpashop.domain.entity.Delivery
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderItem
import com.study.jpashop.domain.entity.dto.OrderDto
import com.study.jpashop.domain.entity.dto.OrderProjection
import com.study.jpashop.domain.entity.dto.OrderSearchRequest
import com.study.jpashop.domain.extensions.findItem
import com.study.jpashop.domain.extensions.findMember
import com.study.jpashop.domain.extensions.findOrder
import com.study.jpashop.domain.repository.IItemRepository
import com.study.jpashop.domain.repository.IMemberRepository
import com.study.jpashop.domain.repository.IOrderRepository
import com.study.jpashop.domain.repository.OrderQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
    private val memberRepository: IMemberRepository,
    private val itemRepository: IItemRepository,
    private val orderRepository: IOrderRepository,
    private val orderQueryRepository: OrderQueryRepository,
) {
    fun ordersV1(): List<OrderDto> {
        val orders = orderRepository.findAll()
        return orders.map(::OrderDto)
    }

    fun ordersV2(offset: Long, limit: Long): List<OrderDto> {
        val orders = orderQueryRepository.findAllWithItem(offset, limit)
        return orders.map(::OrderDto)
    }

    fun ordersV3(offset: Long, limit: Long): List<OrderDto> {
        val orders = orderQueryRepository.findAllWithMemberDelivery(offset, limit)
        return orders.map(::OrderDto)
    }

    fun ordersV4(): List<OrderDto> {
        val orders = orderQueryRepository.findOrdersQueryDto()
        return orders.map { order ->
            val orderItems = orderQueryRepository.findOrderItemQueryDto(order.orderId)
            OrderDto(order, orderItems)
        }
    }

    fun ordersV5(): List<OrderDto> {
        val orders = orderQueryRepository.findOrdersQueryDto()
        val orderItemMap = orderQueryRepository.findOrderItemMap(toOrderIds(orders))
        return orders.map { order -> OrderDto(order, orderItemMap[order.orderId]!!) }
    }

    fun searchOrder(orderSearchRequest: OrderSearchRequest, pageable: Pageable): Page<OrderDto> {
        val orders = orderQueryRepository.findOrderSearch(orderSearchRequest, pageable)
        return orders.map { order ->
            val orderItems = orderQueryRepository.findOrderItemQueryDto(order.orderId)
            OrderDto(order, orderItems)
        }
    }

    @Transactional
    fun order(memberId: Long, itemId: Long, count: Int): Long {
        val member = memberRepository.findMember(memberId)
        val item = itemRepository.findItem(itemId)
        val delivery = Delivery.create(address = member.address)
        val orderItem = OrderItem.create(item, item.price, count)
        val order = Order.create(member, delivery, orderItem)
        orderRepository.save(order)
        return order.id
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val order = orderRepository.findOrder(orderId)
        order.cancel()
    }

    private fun toOrderIds(result: List<OrderProjection>): List<Long> {
        return result.map { it.orderId }
    }
}