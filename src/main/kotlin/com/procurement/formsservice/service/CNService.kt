package com.procurement.formsservice.service

import com.procurement.formsservice.definition.CommonNamesParameters
import com.procurement.formsservice.definition.cn.CnNamesParameters
import com.procurement.formsservice.definition.cn.CnPmd
import com.procurement.formsservice.definition.cn.CnProcuringEntity
import com.procurement.formsservice.definition.cn.CnResponsibleContactPerson
import com.procurement.formsservice.definition.cn.cnDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.definition.parameter.parametersDefinition
import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.Parameters
import com.procurement.formsservice.json.context
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface CNService {
    fun form(request: ServerHttpRequest): Mono<FormRS>
}

@Service
class CNServiceImpl(private val mdmRepository: MDMRepository, private val publicPointService: PublicPointService) :
    CNService {
    private val cnDefinition = cnDefinition()

    private val cnParams = parametersDefinition {
        CommonNamesParameters.LANGUAGE to string {
            required = false
            default = StringParameterValueDefinition("EN")
        }
        CommonNamesParameters.COUNTRY to string {
            required = true
        }
        CnNamesParameters.EI_OCID to string {
            required = true
        }
        CnNamesParameters.PROCURING_ENTITY to string {
            allowableValues = setOf(CnProcuringEntity.BUYER, CnProcuringEntity.THIRD_PARTY)
            required = true
        }
        CnNamesParameters.RESPONSIBLE_CONTRACT_PERSON to string {
            allowableValues = setOf(CnResponsibleContactPerson.BUYER, CnResponsibleContactPerson.THIRD_PARTY)
            required = true
        }
        CnNamesParameters.PMD to string {
            allowableValues = setOf(CnPmd.OT, CnPmd.RT)
            required = true
        }
    }

    override fun form(request: ServerHttpRequest): Mono<FormRS> = mono {
        val parameters = getContextParameters(request = request)
        val data = publicPointService.getDataEI(parameters[CnNamesParameters.EI_OCID].value)
        val context = context(mdmRepository, parameters = parameters) { publicData = data }
        val formSchema = cnDefinition.buildForm(context)
        val formData = cnDefinition.buildData(context)
        FormRS(schema = formSchema, data = formData)
    }

    private fun getContextParameters(request: ServerHttpRequest): Parameters = cnParams.getContextParameters(request)
}
