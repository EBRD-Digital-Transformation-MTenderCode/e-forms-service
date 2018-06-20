package com.procurement.formsservice.exception

import com.procurement.formsservice.domain.response.ErrorRS

class ValidationQueryParametersException(val errors: List<ErrorRS.Error>) :
    RuntimeException("Error of validation query parameters: '$errors'")