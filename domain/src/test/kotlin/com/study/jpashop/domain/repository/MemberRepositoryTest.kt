package com.study.jpashop.domain.repository

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class MemberRepositoryTest @Autowired constructor(
    private val memberRepository: IMemberRepository
) {

    @Test
    fun findByName() {
        val member = Member("bokyung", 10, Address("Seoul", "1-2-3", "12345"))
        memberRepository.save(member)
        val findMember = memberRepository.findByName("bokyung")
        Assertions.assertThat(findMember).containsExactly(member)
    }
}