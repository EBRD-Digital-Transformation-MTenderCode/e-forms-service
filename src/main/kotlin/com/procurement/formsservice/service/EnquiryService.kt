package com.procurement.formsservice.service

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.model.enquiry.create.EnquiryCreateContext
import com.procurement.formsservice.model.enquiry.create.EnquiryCreateParameters
import org.springframework.stereotype.Service
import java.util.*

interface EnquiryService {
    fun create(queryParameters: EnquiryCreateParameters): String
}

@Service
class EnquiryServiceImpl(private val formTemplateService: FormTemplateService) : EnquiryService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.ENQUIRY]

    override fun create(queryParameters: EnquiryCreateParameters): String {
        val data = EnquiryCreateContext(
            parameters = EnquiryCreateContext.Parameters(
                lotId = queryParameters.lotid.takeIf { it.isNotEmpty() }
            ),
            uris = uris(queryParameters)
        )
        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    fun uris(queryParameters: EnquiryCreateParameters): EnquiryCreateContext.Uris {
        return EnquiryCreateContext.Uris(
            country = "${MDMKind.COUNTRY}?lang=${queryParameters.lang}",
            region = "${MDMKind.REGION}?lang=${queryParameters.lang}&country=\$country\$",
            locality = "${MDMKind.LOCALITY}?lang=${queryParameters.lang}&country=\$country\$&region=\$region\$",
            registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=${queryParameters.lang}&country=\$country\$"
        )
    }
}
