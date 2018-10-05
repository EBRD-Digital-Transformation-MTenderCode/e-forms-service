package com.procurement.formsservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan(basePackages = ["com.procurement.formsservice.controller"])
class WebConfiguration {
    @Bean
    fun webClient(): RestTemplate = RestTemplate()
}
