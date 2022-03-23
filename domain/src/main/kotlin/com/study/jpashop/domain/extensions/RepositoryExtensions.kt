package com.study.jpashop.domain.extensions

import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.entity.Order
import com.study.jpashop.domain.entity.item.Item
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.exception.JpaStudyException
import com.study.jpashop.domain.repository.IItemRepository
import com.study.jpashop.domain.repository.IMemberRepository
import com.study.jpashop.domain.repository.IOrderRepository
import org.springframework.data.repository.findByIdOrNull

/**
 * Repository 확장함수
 * getById는 데이터가 없는 경우 EntityNotFoundException이 발생하므로
 * findByIdOrNull를 이용해서 데이터를 가져오고 예외처리
 *  @author bokyung
 */
fun IItemRepository.findItem(id: Long): Item =
    findByIdOrNull(id) ?: throw JpaStudyException(ErrorCode.NOT_EXISTS_ITEM)

fun IMemberRepository.findMember(id: Long): Member =
    findByIdOrNull(id) ?: throw JpaStudyException(ErrorCode.NOT_EXISTS_MEMBER)

fun IOrderRepository.findOrder(id: Long): Order =
    findByIdOrNull(id) ?: throw JpaStudyException(ErrorCode.NOT_EXISTS_ORDER)