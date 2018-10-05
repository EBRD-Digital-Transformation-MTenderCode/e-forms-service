package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.exception.TemplateEvaluateException
import com.procurement.formsservice.exception.bid.LotNotFoundException
import com.procurement.formsservice.exception.client.RemoteServiceException
import com.procurement.formsservice.exception.ei.EiNotContainFsException
import com.procurement.formsservice.exception.query.QueryParameterException
import com.procurement.formsservice.exception.query.QueryParameterInvalidNumberException
import com.procurement.formsservice.exception.query.QueryParameterMissingException
import com.procurement.formsservice.exception.query.QueryParameterStateException
import com.procurement.formsservice.exception.query.QueryParameterTransformationException
import com.procurement.formsservice.exception.query.QueryParameterValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reactor.core.publisher.Mono

@ControllerAdvice
class WebExceptionHandler {
    companion object {
        val log: Logger = LoggerFactory.getLogger(WebExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [RemoteServiceException::class])
    fun remoteService(exception: RemoteServiceException): ResponseEntity<*> {
        log.error(exception.message)

        return ResponseEntity.status(exception.code ?: HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exception.payload)
    }

    @ExceptionHandler(value = [TemplateEvaluateException::class])
    fun templateEvaluate(exception: TemplateEvaluateException): Mono<ResponseEntity<*>> {
        log.error(exception.message, exception)

        return Mono.just(
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("")
        )
    }

    @ExceptionHandler(value = [EiNotContainFsException::class])
    fun eiNotContainFsException(exception: EiNotContainFsException): Mono<ResponseEntity<*>> {
        log.error(exception.message, exception)

        return Mono.just(
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(
                    ErrorRS(
                        errors = listOf(
                            ErrorRS.Error(
                                code = "ei.notContain.fs",
                                description = exception.message!!
                            )
                        )
                    )
                )
        )
    }

    @ExceptionHandler(value = [LotNotFoundException::class])
    fun lotNotFound(exception: LotNotFoundException): Mono<ResponseEntity<*>> {
        log.error(exception.message, exception)

        return Mono.just(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                    ErrorRS(
                        errors = listOf(
                            ErrorRS.Error(
                                code = "lot.notFound",
                                description = exception.message!!
                            )
                        )
                    )
                )
        )
    }

    @ExceptionHandler(value = [QueryParameterException::class])
    fun validationQueryParameters(exception: QueryParameterException): Mono<ResponseEntity<*>> {
        log.error(exception.message)

        val code: String
        val httpCode: HttpStatus

        when (exception) {
            is QueryParameterMissingException -> {
                code = "request.parameter.missing"
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterInvalidNumberException -> {
                code = "request.parameter.invalidNumber"
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterTransformationException -> {
                code = "request.parameter.invalidType"
                httpCode = HttpStatus.BAD_REQUEST
            }
//            is QueryParameterFiltrationException -> {
//                code = "request.parameter.noValidValues"
//                httpCode = HttpStatus.BAD_REQUEST
//            }
            is QueryParameterValidationException -> {
                code = "request.parameter.invalidValue"
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterStateException -> {
                code = "request.parameter.state"
                httpCode = HttpStatus.UNPROCESSABLE_ENTITY
            }
        }
        val description = exception.message!!

        return Mono.just(
            ResponseEntity.status(httpCode)
                .body(
                    ErrorRS(
                        errors = listOf(
                            ErrorRS.Error(
                                code = code,
                                description = description
                            )
                        )
                    )
                )
        )
    }
}
