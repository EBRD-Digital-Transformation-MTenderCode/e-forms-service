package com.procurement.formsservice.service

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.answer.create.AnswerCreateParameters
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnswerServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var service: AnswerService

    companion object {
        private const val ENQUIRY_ID = "1a8e2f80-8b5f-12e8-a48b-9f2980d5bbdd"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)

        service = AnswerServiceImpl(
            formTemplateService = formTemplateService
        )
    }

    @Test
    fun test() {
        val params = genParams()
        runBlocking {
            val json = service.create(params).block()
            assertNotNull(json)
        }
    }

    private fun genParams(): AnswerCreateParameters =
        AnswerCreateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["enquiry-id"] = listOf(ENQUIRY_ID)
                }
            )
        )
}
