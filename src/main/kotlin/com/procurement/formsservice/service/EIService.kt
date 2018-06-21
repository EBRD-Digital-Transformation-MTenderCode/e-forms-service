package com.procurement.formsservice.service

import com.procurement.formsservice.definition.eiDefinition
import com.procurement.formsservice.domain.request.getCountry
import com.procurement.formsservice.domain.request.getIdentifierSchema
import com.procurement.formsservice.domain.request.getLanguage
import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.exception.ValidationQueryParametersException
import com.procurement.formsservice.json.MDMRepository
import com.procurement.formsservice.json.Parameters
import com.procurement.formsservice.json.Parameters.Companion.COUNTRY
import com.procurement.formsservice.json.Parameters.Companion.IDENTIFIER_SCHEMA
import com.procurement.formsservice.json.Parameters.Companion.LANGUAGE
import com.procurement.formsservice.json.context
import com.procurement.formsservice.json.parameters
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

interface EIService {
    fun form(request: ServerHttpRequest): Mono<FormRS>
}

@Service
class EIServiceImpl(private val mdmRepository: MDMRepository) : EIService {
    override fun form(request: ServerHttpRequest): Mono<FormRS> {
        return getParameters(request).map {
            val context = context(mdmRepository, parameters = it)
            val schema = eiDefinition.buildForm(context)
            val data = eiDefinition.buildData(context)
            FormRS(schema = schema, data = data)
        }
    }

    private fun getParameters(request: ServerHttpRequest): Mono<Parameters> {
        val queryParams = request.queryParams

        val errors = mutableListOf<ErrorRS.Error>()

        val lang = queryParams.getLanguage()

        val country = queryParams.getCountry().apply {
            if (this.isBlank()) errors.add(
                ErrorRS.Error(
                    code = "request.country.missing",
                    description = "The query parameter 'country' is missing"
                )
            )
        }

        val identifierSchema = queryParams.getIdentifierSchema().apply {
            if (this.isBlank()) errors.add(
                ErrorRS.Error(
                    code = "request.identifierSchema.missing",
                    description = "The query parameter 'identifierSchema' is missing"
                )
            )
        }

        return if (errors.isNotEmpty())
            Mono.error(ValidationQueryParametersException(errors = errors))
        else
            Mono.just(
                parameters {
                    this[LANGUAGE] = lang
                    this[COUNTRY] = country
                    this[IDENTIFIER_SCHEMA] = identifierSchema
                }
            )
    }
}