package com.procurement.formsservice.service

import com.procurement.formsservice.definition.CommonNamesParameters
import com.procurement.formsservice.definition.fs.FsFunderValues
import com.procurement.formsservice.definition.fs.FsNamesParameters
import com.procurement.formsservice.definition.fs.FsNamesParameters.EI_OCID
import com.procurement.formsservice.definition.fs.FsPayerValues
import com.procurement.formsservice.definition.fs.fsDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.definition.parameter.parametersDefinition
import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.Parameters
import com.procurement.formsservice.json.context
import com.procurement.formsservice.json.exception.ValidationParametersException
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface FSService {
    fun form(request: ServerHttpRequest): Mono<FormRS>
}

@Service
class FSServiceImpl(private val mdmRepository: MDMRepository, private val publicPointService: PublicPointService) :
    FSService {
    private val fsDefinition = fsDefinition()

    private val fsParams = parametersDefinition {
        CommonNamesParameters.LANGUAGE to string {
            required = false
            default = StringParameterValueDefinition("EN")
        }
        CommonNamesParameters.COUNTRY to string {
            required = true
        }
        FsNamesParameters.EI_OCID to string {
            required = true
        }
        FsNamesParameters.FUNDER to string {
            allowableValues = setOf(FsFunderValues.BUYER, FsFunderValues.STATE, FsFunderValues.DONOR)
            required = true
        }
        FsNamesParameters.PAYER to string {
            allowableValues = setOf(FsPayerValues.BUYER, FsPayerValues.THIRD_PARTY, FsPayerValues.FOUNDER)
            required = true
        }
        FsNamesParameters.IS_EU_FUNDED to boolean {
            required = true
        }
    }

    override fun form(request: ServerHttpRequest): Mono<FormRS> = mono {
        val parameters = getContextParameters(request = request)
        val data = publicPointService.getDataEI(parameters[EI_OCID].value)
        val context = context(mdmRepository, parameters = parameters) { publicData = data }
        val formSchema = fsDefinition.buildForm(context)
        val formData = fsDefinition.buildData(context)
        FormRS(schema = formSchema, data = formData)
    }

    private fun getContextParameters(request: ServerHttpRequest): Parameters =
        fsParams.getContextParameters(request).also { validateParameters(it) }

    private fun validateParameters(parameters: Parameters): Parameters {
        val payer = parameters[FsNamesParameters.PAYER]
        val funder = parameters[FsNamesParameters.FUNDER]
        if (payer == FsPayerValues.FOUNDER && funder != FsFunderValues.DONOR)
            throw ValidationParametersException(
                errors = listOf(
                    ErrorRS.Error(
                        code = "request.funder.state",
                        description = "Invalid value of the parameter 'funder (${funder.value})' when the parameter payer is 'funder'"
                    )
                )
            )

        return parameters
    }
}
