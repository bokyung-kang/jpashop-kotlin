package com.study.jpashop.api.controller.dto

import com.study.jpashop.domain.entity.Address
import com.study.jpashop.domain.entity.Member
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class MemberRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[가-힣]*\$")
    val name: String,

    @field:Range(min = 18, max = 99)
    val age: Int,

    @field:Size(min = 1, max = 10)
    @field:NotBlank
    val city: String = "",
    val street: String = "",

    @field:Pattern(regexp = "\\d{3}-\\d{3}|\\d{5}")
    val zipcode: String,
) {
    fun toEntity(): Member {
        return Member(name, age, Address(city, street, zipcode))
    }
}

data class MemberResponse(
    val id: Long,
    val name: String,
    val age: Int,
    val address: Address,
) {
    constructor(member: Member) : this(
        member.id,
        member.name,
        member.age,
        member.address
    )
}
