package com.procurement.formsservice.json.exception

class ParsePayloadException(message: String, exception: Throwable) : RuntimeException(message, exception)