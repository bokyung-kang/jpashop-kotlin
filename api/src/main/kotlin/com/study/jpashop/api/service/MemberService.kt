package com.study.jpashop.api.service

import com.study.jpashop.api.controller.dto.MemberResponse
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.extensions.findMember
import com.study.jpashop.domain.repository.IMemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: IMemberRepository,
) {

    fun findMember(id: Long): MemberResponse {
        val member = memberRepository.findMember(id)
        return MemberResponse(member)
    }

    @Transactional
    fun join(member: Member): Long {
        validateDuplicateMember(member)
        memberRepository.save(member)
        return member.id
    }

    @Transactional
    fun update(id: Long, name: String) {
        val member = memberRepository.findMember(id)
        member.name = name
    }

    private fun validateDuplicateMember(member: Member) {
        val findMember = memberRepository.findByName(member.name)
        check(findMember.isEmpty()) {
            ErrorCode.ALREADY_EXISTS_MEMBER
        }
    }
}