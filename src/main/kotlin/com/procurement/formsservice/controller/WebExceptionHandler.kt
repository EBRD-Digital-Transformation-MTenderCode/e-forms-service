package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.exception.ValidationQueryParametersException
import com.procurement.formsservice.exception.client.RemoteServiceException
import com.procurement.formsservice.json.exception.NoSuchParameter
import com.procurement.formsservice.json.exception.ValidationParametersException
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
    fun remoteService(exception: RemoteServiceException): Mono<ResponseEntity<*>> {
        log.error(exception.message)

        return Mono.just(
            ResponseEntity.status(exception.code)
                .body(exception.payload)
        )
    }

    @ExceptionHandler(value = [ValidationQueryParametersException::class])
    fun validationQueryParameters(exception: ValidationQueryParametersException): Mono<ResponseEntity<*>> {
        log.error(exception.message)

        return Mono.just(
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRS(exception.errors))
        )
    }

    @ExceptionHandler(value = [ValidationParametersException::class])
    fun validationParameters(exception: ValidationParametersException): Mono<ResponseEntity<*>> {
        log.error(exception.message)

        return Mono.just(
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorRS(exception.errors))
        )
    }

    @ExceptionHandler(value = [NoSuchParameter::class])
    fun validationParameters(exception: NoSuchParameter): Mono<ResponseEntity<*>> {
        log.error(exception.message, exception)

        return Mono.just(
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("")
        )
    }
}
