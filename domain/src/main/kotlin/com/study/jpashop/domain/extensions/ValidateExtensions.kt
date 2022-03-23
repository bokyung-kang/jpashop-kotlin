@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
package com.study.jpashop.domain.extensions

import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.exception.JpaStudyException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun validate(value: Boolean, errorCode: () -> ErrorCode) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw JpaStudyException(errorCode())
    }
}
