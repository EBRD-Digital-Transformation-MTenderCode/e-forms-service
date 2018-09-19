package com.procurement.formsservice.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux

@Configuration
@ComponentScan(basePackages = ["com.procurement.formsservice.controller"])
@EnableWebFlux
class WebConfiguration
