package com.study.jpashop.api.controller

import com.study.jpashop.domain.entity.dto.OrderDto
import com.study.jpashop.api.service.OrderService
import com.study.jpashop.domain.entity.dto.OrderSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderService: OrderService,
) {

    /**
     * V1. Entity -> Dto변환
     * - 단점：Lazy loading으로 N+1발생
     * - Order가 100건이면  (1 + 100 Query)
     */
    @GetMapping("/api/v1/orders")
    fun ordersV1(): List<OrderDto> {
        return orderService.ordersV1()
    }

    /**
     * V2. Entity -> Dto변환
     * - fetch join을 이용해서 Query 1번 실행
     * - 단점：페이징 불가능
     * -- distinct를 이용하므로 이 경우는 100건의 Order와 2건의 OrderItem으로 200개가 조회되어 distinct를 처리함
     * -- paging을 넣으면「HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!」
     * -- 로그가 표시되고 Hibernate는 Memory상에서 페이징을 처리하므로 OOM에러 발생 가능성 있음
     * - Collection / Paging이 없는 경우 가장 추천하는 방법
     */
    @GetMapping("/api/v2/orders")
    fun ordersV2(
        @RequestParam("offset", required = false, defaultValue = "0") offset: String,
        @RequestParam("limit", required = false, defaultValue = "100") limit: String,
    ): List<OrderDto> {
        return orderService.ordersV2(offset = offset.toLong(), limit = limit.toLong())
    }

    /**
     * V3. Entity -> Dto변환
     * - ToOne 매핑은 전부 fetch join으로 최적화
     * - Collections은 Lazy Loading을 유지
     * -- order_id in (x,x,x) InQuery가 실행되도록 hibernate.default_batch_fetch_size을 1000으로 설정
     * -- 지정안하면 N+1발생
     * - Collection / Paging 처리에 가장 추천하는 방법
     */
    @GetMapping("/api/v3/orders")
    fun ordersV3(
        @RequestParam("offset", required = false, defaultValue = "0") offset: String,
        @RequestParam("limit", required = false, defaultValue = "100") limit: String,
    ): List<OrderDto> {
        return orderService.ordersV3(offset = offset.toLong(), limit = limit.toLong())
    }

    /**
     * V4. Entity -> Dto변환
     * - Order를 먼저 취득하고 Collections은 Lazy Loading을 유지시키면서 별도로 Select하는 방법
     * - Query는 Root 1번 Collections　N번
     * 단건 조회시 많이 사용하는 방법
     */
    @GetMapping("/api/v4/orders")
    fun ordersV4(): List<OrderDto> {
        return orderService.ordersV4()
    }

    /**
     * V5. Entity -> Dto변환
     * - Query는 Root 1번 Collections 1번
     * 데이터를 한번에 처리하고 싶은 경우 많이 사용하는 방법
     */
    @GetMapping("/api/v5/orders")
    fun ordersV5(): List<OrderDto> {
        return orderService.ordersV5()
    }

    /**
     * Page를 사용해서 페이징
     */
    @GetMapping("/api/v6/orders")
    fun searchOrders(orderSearchRequest: OrderSearchRequest, pageable: Pageable): Page<OrderDto> =
        orderService.searchOrder(orderSearchRequest, pageable)
}