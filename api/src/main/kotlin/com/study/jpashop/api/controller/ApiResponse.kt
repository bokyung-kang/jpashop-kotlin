package com.study.jpashop.api.controller

import com.study.jpashop.domain.exception.ApiErrorCode
import com.study.jpashop.domain.exception.ErrorCode

data class ApiResponse<T>(
    val code: String = "",
    val message: String? = "",
    val body: T? = null,
) {
    companion object {
        fun error(message: String?): ApiResponse<Unit> =
            ApiResponse(message = message)

        fun error(code: ApiErrorCode, message: String?): ApiResponse<Unit> =
            ApiResponse(code = code.code, message = message)

        fun error(code: ErrorCode, message: String?): ApiResponse<Unit> =
            ApiResponse(code = code.name, message = message)

        fun <T> success(body: T?): ApiResponse<T> = ApiResponse(body = body)
    }
}