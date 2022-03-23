package com.study.jpashop.domain.exception

enum class ApiErrorCode(
    val code: String,
) {
    COMMON_ERROR("E0001"),
    INVALID_FORMAT("E0002"),
    MISSING_PARAMETER("E0003"),
    JSON_CONVERT_ERROR("E0004"),
    NOT_FOUND("E0005"),
    NOT_FOUND_ENTITY("E0006"),
    VALIDATION_ERROR("E0007"),
}

enum class ErrorCode(
    val message: String,
) {
    UNKNOWN("unknown.error"),
    REQUIRED("required"),

    NOT_EXISTS_MEMBER("not.exists.member"),
    ALREADY_EXISTS_MEMBER("already.exists.member"),

    NOT_EXISTS_ITEM("not.exists.item"),
    OUT_OF_STOCK("out.of.stock"),

    NOT_EXISTS_ORDER("not.exists.order"),

    ALREADY_DELIVERED("already.delivered"),
    ;

    companion object {
        fun of(code: String): ErrorCode? {
            return values().firstOrNull { it.name == code }
        }
    }
}
