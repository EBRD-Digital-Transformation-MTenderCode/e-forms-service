package com.procurement.formsservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    value = [
        WebConfiguration::class,
        ServiceConfiguration::class
    ]
)
class ApplicationConfiguration