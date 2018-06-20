package com.procurement.formsservice.configuration

import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.MDMRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(value = ["com.procurement.formsservice.service"])
class ServiceConfiguration {
    @Bean
    fun mdmRepository(): MDMRepository = MDMRepositoryImpl()
}
