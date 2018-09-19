package com.procurement.formsservice.service

import com.procurement.formsservice.model.answer.create.AnswerCreateContext
import com.procurement.formsservice.model.answer.create.AnswerCreateParameters
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

interface AnswerService {
    fun create(queryParameters: AnswerCreateParameters): Mono<String>
}

@Service
class AnswerServiceImpl(private val formTemplateService: FormTemplateService) : AnswerService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.ANSWER]

    override fun create(queryParameters: AnswerCreateParameters): Mono<String> = mono {
        val data = AnswerCreateContext()
        formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }
}
