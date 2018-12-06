package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.CodesOfErrors
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
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class WebExceptionHandler {
    companion object {
        val log: Logger = LoggerFactory.getLogger(WebExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [RemoteServiceException::class])
    fun remoteService(exception: RemoteServiceException): ResponseEntity<*> {
        log.error(exception.message, exception)

        return ResponseEntity.status(exception.code ?: HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(exception.payload)
    }

    @ExceptionHandler(value = [TemplateEvaluateException::class])
    fun templateEvaluate(exception: TemplateEvaluateException): ResponseEntity<*> {
        log.error(exception.message, exception)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorRS(
                    errors = listOf(
                        ErrorRS.Error(
                            code = CodesOfErrors.INTERNAL_SERVER_ERROR.code,
                            description = exception.message!!
                        )
                    )
                )
            )
    }

    @ExceptionHandler(value = [EiNotContainFsException::class])
    fun eiNotContainFsException(exception: EiNotContainFsException): ResponseEntity<*> {
        log.error(exception.message, exception)

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(
                ErrorRS(
                    errors = listOf(
                        ErrorRS.Error(
                            code = CodesOfErrors.EI_NOT_CONTAIN_FS.code,
                            description = exception.message!!
                        )
                    )
                )
            )
    }

    @ExceptionHandler(value = [LotNotFoundException::class])
    fun lotNotFound(exception: LotNotFoundException): ResponseEntity<*> {
        log.error(exception.message, exception)

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorRS(
                    errors = listOf(
                        ErrorRS.Error(
                            code = CodesOfErrors.LOT_NOT_FOUND.code,
                            description = exception.message!!
                        )
                    )
                )
            )
    }

    @ExceptionHandler(value = [QueryParameterException::class])
    fun validationQueryParameters(exception: QueryParameterException): ResponseEntity<*> {
        log.error(exception.message)

        val code: String
        val httpCode: HttpStatus

        when (exception) {
            is QueryParameterMissingException -> {
                code = CodesOfErrors.REQUEST_PARAMETER_MISSING.code
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterInvalidNumberException -> {
                code = CodesOfErrors.REQUEST_PARAMETER_INVALID_NUMBER.code
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterTransformationException -> {
                code = CodesOfErrors.REQUEST_PARAMETER_INVALID_TYPE.code
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterValidationException -> {
                code = CodesOfErrors.REQUEST_PARAMETER_INVALID_VALUE.code
                httpCode = HttpStatus.BAD_REQUEST
            }
            is QueryParameterStateException -> {
                code = CodesOfErrors.REQUEST_PARAMETER_STATE.code
                httpCode = HttpStatus.UNPROCESSABLE_ENTITY
            }
        }
        val description = exception.message!!

        return ResponseEntity.status(httpCode)
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
    }
}
