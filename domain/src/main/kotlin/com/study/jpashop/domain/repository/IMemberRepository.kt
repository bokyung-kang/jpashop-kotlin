package com.study.jpashop.domain.repository

import com.study.jpashop.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IMemberRepository : JpaRepository<Member, Long> {
    fun findByName(name: String): List<Member>

    // 일괄 업데이트 후에는 영속성 컨텍스트를 초기화 해준다
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    fun bulkAgePlus(@Param("age") age: Int): Int
}
