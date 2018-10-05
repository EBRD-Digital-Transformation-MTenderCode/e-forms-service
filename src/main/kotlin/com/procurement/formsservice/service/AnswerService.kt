package com.procurement.formsservice.service

import com.procurement.formsservice.model.answer.create.AnswerCreateContext
import com.procurement.formsservice.model.answer.create.AnswerCreateParameters
import org.springframework.stereotype.Service
import java.util.*

interface AnswerService {
    fun create(queryParameters: AnswerCreateParameters): String
}

@Service
class AnswerServiceImpl(private val formTemplateService: FormTemplateService) : AnswerService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.ANSWER]

    override fun create(queryParameters: AnswerCreateParameters): String {
        val data = AnswerCreateContext()
        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }
}
