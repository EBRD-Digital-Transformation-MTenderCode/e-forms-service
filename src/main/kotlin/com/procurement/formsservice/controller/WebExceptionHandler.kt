package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.exception.ValidationQueryParametersException
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

    @ExceptionHandler(value = [ValidationQueryParametersException::class])
    fun validationQueryParameters(exception: ValidationQueryParametersException): Mono<ResponseEntity<*>> {
        log.error(exception.message)

        return Mono.just(
            ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorRS(exception.errors))
        )
    }
}