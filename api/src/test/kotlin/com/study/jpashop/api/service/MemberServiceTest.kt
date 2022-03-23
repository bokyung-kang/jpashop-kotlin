package com.study.jpashop.api.service

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Member
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.exception.JpaStudyException
import com.study.jpashop.domain.extensions.findMember
import com.study.jpashop.domain.repository.IMemberRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MemberServiceTest {

    lateinit var memberService: MemberService

    @MockK
    lateinit var memberRepository: IMemberRepository

    @BeforeEach
    fun setUp() {
        memberService = MemberService(memberRepository)
    }

    @Test
    @DisplayName("가입")
    fun joinTest() {
        val member = createMember(1L)

        every { memberRepository.findByName(member.name) } returns emptyList()
        every { memberRepository.save(member) } returns member

        val actualId = memberService.join(member)

        assertThat(actualId).isEqualTo(member.id)

        verify(exactly = 1) { memberRepository.save(member) }
    }

    @Test
    @DisplayName("중복가입 에러")
    fun joinFail_01() {

        val member = createMember(1L)

        every { memberRepository.findByName(member.name) } returns listOf(member)

        val thrown = Assertions.assertThrows(IllegalStateException::class.java) {
            memberService.join(member)
        }

        assertThat(ErrorCode.ALREADY_EXISTS_MEMBER.name).isEqualTo(thrown.message)

        verify(exactly = 1) { memberRepository.findByName(member.name) }
    }

    @Test
    @DisplayName("존재하지 않는 유저정보 조회")
    fun joinFail_02() {
        val member = createMember(2L)

        every { memberRepository.findMember(member.id) } throws JpaStudyException(ErrorCode.NOT_EXISTS_MEMBER)

        val thrown = Assertions.assertThrows(JpaStudyException::class.java) {
            memberRepository.findMember(member.id)
        }

        assertThat(ErrorCode.NOT_EXISTS_MEMBER).isEqualTo(thrown.errorCode)

        verify(exactly = 1) { memberRepository.findMember(member.id) }
    }

    private fun createMember(memberId: Long): Member {
        return Member("bokyung", 10, Address("Seoul", "1-2-3", "12345"))
            .apply { id = memberId }
    }
}