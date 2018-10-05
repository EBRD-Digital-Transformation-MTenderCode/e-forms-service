package com.procurement.formsservice.service

import com.procurement.formsservice.model.award.update.AwardUpdateContext
import com.procurement.formsservice.model.award.update.AwardUpdateParameters
import org.springframework.stereotype.Service
import java.util.*

interface AwardService {
    fun update(queryParameters: AwardUpdateParameters): String
}

@Service
class AwardServiceImpl(private val formTemplateService: FormTemplateService) : AwardService {
    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.AWARD]

    override fun update(queryParameters: AwardUpdateParameters): String {
        val data = AwardUpdateContext(
            parameters = AwardUpdateContext.Parameters(
                lotId = queryParameters.lotId
            )
        )
        return formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }
}
