package com.procurement.formsservice.service

import com.procurement.formsservice.model.award.update.AwardUpdateContext
import com.procurement.formsservice.model.award.update.AwardUpdateParameters
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

interface AwardService {
    fun update(queryParameters: AwardUpdateParameters): Mono<String>
}

@Service
class AwardServiceImpl(private val formTemplateService: FormTemplateService) : AwardService {
    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.AWARD]

    override fun update(queryParameters: AwardUpdateParameters): Mono<String> = mono {
        val data = AwardUpdateContext(
            parameters = AwardUpdateContext.Parameters(
                lotId = queryParameters.lotId
            )
        )
        formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }
}
