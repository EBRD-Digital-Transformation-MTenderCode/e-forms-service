package com.procurement.formsservice

import com.procurement.formsservice.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication(
    scanBasePackageClasses = [
        ApplicationConfiguration::class
    ]
)
@EnableCaching
class FormServiceApplication

fun main(args: Array<String>) {
    runApplication<FormServiceApplication>(*args)
}
