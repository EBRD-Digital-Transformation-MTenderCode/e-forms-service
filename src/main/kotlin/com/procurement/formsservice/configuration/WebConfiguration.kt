package com.procurement.formsservice.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.procurement.formsservice.controller"])
class WebConfiguration
