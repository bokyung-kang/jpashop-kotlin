package com.study.jpashop.api.controller

import com.study.jpashop.api.controller.dto.MemberRequest
import com.study.jpashop.api.controller.dto.MemberResponse
import com.study.jpashop.api.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("/api/v1/join")
    fun join(@RequestBody @Valid request: MemberRequest): ResponseEntity<Unit> {
        memberService.join(request.toEntity())
        return ResponseEntity.ok().build()
    }

    @GetMapping("/api/v1/user/{id}")
    fun getUser(
        @PathVariable id: Long,
    ): ResponseEntity<ApiResponse<MemberResponse>> {
        val user = memberService.findMember(id)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
}