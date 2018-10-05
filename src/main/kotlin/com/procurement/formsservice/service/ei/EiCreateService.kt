package com.procurement.formsservice.service.ei

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.query.QueryParameterValidationException
import com.procurement.formsservice.model.ei.create.EiCreateContext
import com.procurement.formsservice.model.ei.create.EiCreateParameters
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import org.springframework.stereotype.Service
import java.util.*

interface EiCreateService {
    fun create(queryParameters: EiCreateParameters): String
}

@Service
class EiCreateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val mdmRepository: MDMRepository) :
    EiCreateService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.EI]

    override fun create(queryParameters: EiCreateParameters): String {

        validation(queryParameters)

        val data = EiCreateContext(
            parameters = EiCreateContext.Parameters(
                country = queryParameters.country,
                registrationScheme = queryParameters.identifierSchema
            ),
            uris = EiCreateContext.Uris(
                cpv = "${MDMKind.CPV}?lang=${queryParameters.lang}",
                country = "${MDMKind.COUNTRY}/${queryParameters.country}?lang=${queryParameters.lang}",
                region = "${MDMKind.REGION}?lang=${queryParameters.lang}&country=${queryParameters.country}",
                locality = "${MDMKind.LOCALITY}?lang=${queryParameters.lang}&country=${queryParameters.country}&region=\$region\$",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=${queryParameters.lang}&country=${queryParameters.country}"
            )
        )

        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun validation(queryParameters: EiCreateParameters) {
        val lang = queryParameters.lang
        val country = queryParameters.country

        val countries = mdmRepository.countries(lang)
        if (!countries.contains(country))
            throw QueryParameterValidationException(
                name = CommonQueryParametersBinder.COUNTRY.name,
                value = country
            )

        val schemesRegistration = mdmRepository.schemeRegistration(lang, country)
        if (!schemesRegistration.contains(queryParameters.identifierSchema))
            throw QueryParameterValidationException(
                name = EiCreateParameters.IDENTIFIER_SCHEMA.name,
                value = queryParameters.identifierSchema
            )
    }
}
