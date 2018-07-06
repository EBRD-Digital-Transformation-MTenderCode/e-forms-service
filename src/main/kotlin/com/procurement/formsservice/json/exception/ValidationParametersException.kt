package com.procurement.formsservice.json.exception

import com.procurement.formsservice.domain.response.ErrorRS

class ValidationParametersException(val errors: List<ErrorRS.Error>) :
    RuntimeException("Error of validation parameters: '$errors'")