package com.study.jpashop.api.exception

import com.study.jpashop.api.controller.ApiResponse
import com.study.jpashop.domain.exception.ApiErrorCode
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
@Order(1)
class NoHandlerFoundExceptionHandler() {
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFoundException(exception: NoHandlerFoundException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ApiResponse.error(
                    ApiErrorCode.NOT_FOUND,
                    HttpStatus.NOT_FOUND.reasonPhrase
                )
            )
    }
}