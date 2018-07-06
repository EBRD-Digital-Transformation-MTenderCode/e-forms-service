package com.procurement.formsservice.service

import com.procurement.formsservice.definition.CommonNamesParameters
import com.procurement.formsservice.definition.ei.EiNamesParameters
import com.procurement.formsservice.definition.ei.eiDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.definition.parameter.parametersDefinition
import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.context
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface EIService {
    fun form(request: ServerHttpRequest): Mono<FormRS>
}

@Service
class EIServiceImpl(private val mdmRepository: MDMRepository) : EIService {
    private val eiDefinition = eiDefinition()

    private val eiParams = parametersDefinition {
        CommonNamesParameters.LANGUAGE to string {
            required = false
            default = StringParameterValueDefinition("EN")
        }
        CommonNamesParameters.COUNTRY to string()
        EiNamesParameters.IDENTIFIER_SCHEMA to string()
    }

    override fun form(request: ServerHttpRequest): Mono<FormRS> = mono {
        eiParams.getContextParameters(request).let {
            val context = context(mdmRepository, parameters = it)
            val formSchema = eiDefinition.buildForm(context)
            val formData = eiDefinition.buildData(context)
            FormRS(schema = formSchema, data = formData)
        }
    }
}
