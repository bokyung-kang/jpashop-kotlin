package com.study.jpashop.api.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.querydsl.core.util.ArrayUtils
import com.study.jpashop.api.controller.ApiResponse
import com.study.jpashop.domain.exception.ApiErrorCode
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.exception.JpaStudyException
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@RestControllerAdvice
class ExceptionHandler(
    private val messageSource: MessageSource,
) : ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest,
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        val message = when (val exception = ex.cause) {

            is MissingKotlinParameterException -> mutableMapOf<String, MutableList<String>>(
                exception.parameter.name.orEmpty() to mutableListOf(
                    "입력해 주세요."
                )
            ).toString()
            is InvalidFormatException -> mutableMapOf<String, MutableList<String>>(
                exception.path.last().fieldName.orEmpty() to mutableListOf(
                    "형식이 맞지 않습니다."
                )
            ).toString()
            else -> exception?.message.orEmpty()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ApiErrorCode.JSON_CONVERT_ERROR, message))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest,
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ApiErrorCode.JSON_CONVERT_ERROR, ex.messages()))
    }

    /**
     * 같은 항목에 여러 에러가 있어도 Map에 세팅해 줌
     */
    private fun MethodArgumentNotValidException.messages(): String {
        val resultParam: MutableMap<String, MutableList<String>> = mutableMapOf()
        bindingResult.fieldErrors.forEach {
            val arguments = it.arguments?.let { arg ->
                ArrayUtils.subarray(arg, 1, arg.size)
            }

            val message = try {
                messageSource.getMessage(it.codes!![0], arguments, Locale.getDefault())
            } catch (e: NoSuchMessageException) {
                it.defaultMessage!!
            }

            if (resultParam.containsKey(it.field)) {
                resultParam.getValue(it.field).add(message)
            } else {
                resultParam[it.field] = mutableListOf(message)
            }
        }
        return resultParam.toSortedMap().toString()
    }

    /**
     * 에러코드가 있으면 에러에 해당하는 메세지를 세팅
     */
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)

        var errorMessage: String? = HttpStatus.BAD_REQUEST.reasonPhrase
        exception.message?.let {
            val errorCode: ErrorCode? = ErrorCode.of(exception.message!!)
            errorMessage = errorCode?.let { messageSource.getMessage(errorCode.message, null, Locale.getDefault()) }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ApiErrorCode.VALIDATION_ERROR, errorMessage))
    }

    /**
     * JpaStudyException 발생 시 세팅한 에러코드에 해당하는 메세지를 세팅
     */
    @ExceptionHandler(JpaStudyException::class)
    fun handleJpaStudyException(exception: JpaStudyException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.error(
                    exception.errorCode,
                    messageSource.getMessage(exception.errorCode.message, null, Locale.getDefault())
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(ApiErrorCode.COMMON_ERROR, exception.message!!))
    }
}