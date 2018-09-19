package com.procurement.formsservice.exception

class TemplateEvaluateException(val msg: String, val exception: Throwable) : RuntimeException(msg, exception)
