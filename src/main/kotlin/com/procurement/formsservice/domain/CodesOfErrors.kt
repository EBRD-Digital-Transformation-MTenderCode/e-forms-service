package com.procurement.formsservice.domain

import com.procurement.formsservice.configuration.properties.GlobalProperties
import org.springframework.http.HttpStatus

enum class CodesOfErrors(val httpStatus: HttpStatus, group: String, id: String) {
    REQUEST_PARAMETER_MISSING(httpStatus = HttpStatus.BAD_REQUEST, group = "06", id = "01"),
    REQUEST_PARAMETER_INVALID_NUMBER(httpStatus = HttpStatus.BAD_REQUEST, group = "06", id = "02"),
    REQUEST_PARAMETER_INVALID_TYPE(httpStatus = HttpStatus.BAD_REQUEST, group = "06", id = "03"),
    REQUEST_PARAMETER_INVALID_VALUE(httpStatus = HttpStatus.BAD_REQUEST, group = "06", id = "04"),
    REQUEST_PARAMETER_STATE(httpStatus = HttpStatus.UNPROCESSABLE_ENTITY, group = "06", id = "05"),
    EI_NOT_CONTAIN_FS(httpStatus = HttpStatus.UNPROCESSABLE_ENTITY, group = "07", id = "01"),
    LOT_NOT_FOUND(httpStatus = HttpStatus.NOT_FOUND, group = "07", id = "02"),
    INTERNAL_SERVER_ERROR(httpStatus = HttpStatus.INTERNAL_SERVER_ERROR, group = "00", id = "00");

    val code: String = "${httpStatus.value()}.${GlobalProperties.serviceId}.$group.$id"

    override fun toString(): String = code
}