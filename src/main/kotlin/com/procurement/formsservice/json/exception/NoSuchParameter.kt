package com.procurement.formsservice.json.exception

import com.procurement.formsservice.domain.response.ErrorRS

class NoSuchParameter(val error: ErrorRS.Error) : RuntimeException("No such parameter '$error'.")