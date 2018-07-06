package com.procurement.formsservice.configuration

import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.MDMRepositoryImpl
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ComponentScan(value = ["com.procurement.formsservice.service"])
class ServiceConfiguration {
    @Bean
    @LoadBalanced
    fun loadBalancedWebClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean
    fun mdmRepository(): MDMRepository = MDMRepositoryImpl(webClientBuilder = loadBalancedWebClientBuilder())
}
