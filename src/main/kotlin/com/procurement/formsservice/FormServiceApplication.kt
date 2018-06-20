package com.procurement.formsservice

import com.procurement.formsservice.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackageClasses = [
        ApplicationConfiguration::class
    ]
)
class FormServiceApplication

fun main(args: Array<String>) {
    runApplication<FormServiceApplication>(*args)
}
