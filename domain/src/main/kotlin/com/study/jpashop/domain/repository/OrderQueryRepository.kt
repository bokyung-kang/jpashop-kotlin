package com.study.jpashop.domain.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.jpashop.domain.entity.dto.OrderSearchRequest
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.OrderStatus
import com.study.jpashop.domain.entity.QDelivery.delivery
import com.study.jpashop.domain.entity.QMember.member
import com.study.jpashop.domain.entity.QOrder.order
import com.study.jpashop.domain.entity.QOrderItem.orderItem
import com.study.jpashop.domain.entity.dto.OrderItemProjection
import com.study.jpashop.domain.entity.dto.OrderProjection
import com.study.jpashop.domain.entity.dto.QOrderItemProjection
import com.study.jpashop.domain.entity.dto.QOrderProjection
import com.study.jpashop.domain.entity.item.QItem.item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class OrderQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {

    fun findAllWithMemberDelivery(offset: Long, limit: Long): List<Order> {
        return queryFactory
            .selectFrom(order)
            .join(order.member, member).fetchJoin()
            .join(order.delivery, delivery).fetchJoin()
            .offset(offset)
            .limit(limit)
            .fetch()
    }

    fun findAllWithItem(offset: Long, limit: Long): List<Order> {
        return queryFactory
            .selectFrom(order).distinct()
            .join(order.member, member).fetchJoin()
            .join(order.delivery, delivery).fetchJoin()
            .join(order._orderItems, orderItem).fetchJoin()
            .join(orderItem.item, item).fetchJoin()
            .offset(offset) // pageNo는 0부터 스타트（zero index）
            .limit(limit) // 취득 건수
            .fetch()
    }

    fun findOrderItemQueryDto(orderId: Long): List<OrderItemProjection> {
        return queryFactory
            .select(
                QOrderItemProjection(
                    orderItem.order.id,
                    item.name,
                    orderItem.orderPrice,
                    orderItem.count
                )
            )
            .from(orderItem)
            .join(orderItem.item, item)
            .where(orderItem.order.id.eq(orderId))
            .fetch()
    }

    /**
     * @QueryProjection를 이용해서 필요한 컬럼만 취득
     */
    fun findOrdersQueryDto(): List<OrderProjection> {
        return queryFactory
            .select(
                QOrderProjection(
                    order.id,
                    member.name,
                    member.age,
                    order.orderDate,
                    order.status,
                    delivery.address
                )
            )
            .from(order)
            .join(order.member, member)
            .join(order.delivery, delivery)
            .fetch()
    }

    fun findOrderItemMap(orderIds: List<Long>): Map<Long, List<OrderItemProjection>> {
        val orderItems = queryFactory.select(
            QOrderItemProjection(
                orderItem.order.id,
                item.name,
                orderItem.orderPrice,
                orderItem.count
            )
        )
            .from(orderItem)
            .join(orderItem.item, item)
            .where(orderItem.order.id.`in`(orderIds))
            .fetch()
        return orderItems.groupBy { it.orderId }
    }

    fun findOrderSearch(orderSearchRequest: OrderSearchRequest, pageable: Pageable): Page<OrderProjection> {
        val content = queryFactory
            .select(
                QOrderProjection(
                    order.id,
                    member.name,
                    member.age,
                    order.orderDate,
                    order.status,
                    delivery.address
                )
            )
            .from(order)
            .join(order.member, member)
            .join(order.delivery, delivery)
            .where(
                // 재이용 가능한 메서드
                statusEq(orderSearchRequest.status),
                usernameEq(orderSearchRequest.name),
                ageGoe(orderSearchRequest.ageGoe),
                ageLoe(orderSearchRequest.ageLoe)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            // Sort는 PageRequest를 이용하는게 가능하지만 query에 추가하는게 더 간단하다
            // PageRequest.of(pageable.offset, pageable.pageSize.toLong(), Sort.by(Sort.Direction.DESC, "orderDate"))
            .orderBy(order.orderDate.desc(), member.name.desc())
            .fetch()

        // 처음 페이지/마지막 페이지의 건수가, 페이지사이즈보다 적은 경우 Count Query를 실행하지 않는다
        // QuerydslRepositorySupport를 이용하면 페이징 처리를 하는 것이 간단하지만 
        // SpringData Sort기능이 정상동작 하지 않기 때문에 , QuerydslRepositorySupport를 별도로 개발해야 함
        return PageableExecutionUtils.getPage(
            content,
            pageable
        ) { findOrderSearchTotal(orderSearchRequest).fetchOne()!! }
    }

    fun findOrderSearchTotal(orderSearchRequest: OrderSearchRequest): JPAQuery<Long> {
        return queryFactory
            .select(
                member.count()
            )
            .from(order)
            .join(order.member, member)
            .join(order.delivery, delivery)
            .where(
                statusEq(orderSearchRequest.status),
                usernameEq(orderSearchRequest.name),
                ageGoe(orderSearchRequest.ageGoe),
                ageLoe(orderSearchRequest.ageLoe)
            )
    }

    private fun statusEq(orderStatus: OrderStatus?): BooleanExpression? {
        return orderStatus?.let { order.status.eq(orderStatus) }
    }

    private fun usernameEq(username: String?): BooleanExpression? {
        return username?.let { member.name.eq(username) }
    }

    private fun ageGoe(ageGoe: Int?): BooleanExpression? {
        return ageGoe?.let { member.age.goe(ageGoe) }
    }

    private fun ageLoe(ageLoe: Int?): BooleanExpression? {
        return ageLoe?.let { member.age.loe(ageLoe) }
    }
}