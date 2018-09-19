package com.procurement.formsservice.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(value = ["com.procurement.formsservice.repository"])
class RepositoryConfiguration
