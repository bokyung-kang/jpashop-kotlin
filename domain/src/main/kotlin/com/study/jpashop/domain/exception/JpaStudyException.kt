package com.study.jpashop.domain.exception

class JpaStudyException(val errorCode: ErrorCode = ErrorCode.UNKNOWN) : RuntimeException()
