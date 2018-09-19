package com.procurement.formsservice.service

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.award.update.AwardUpdateParameters
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AwardServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var service: AwardService

    companion object {
        private const val AWARD_ID = "1a8e2f80-8b5f-12e8-a48b-9f2980d5bbdd"
        private const val LOT_ID = "1a8e2f80-8b5f-12e8-a48b-9f2980d5bbdd"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)

        service = AwardServiceImpl(
            formTemplateService = formTemplateService
        )
    }

    @Test
    fun test() {
        val params = genParams()
        runBlocking {
            val json = service.update(params).block()
            assertNotNull(json)
        }
    }

    private fun genParams(): AwardUpdateParameters =
        AwardUpdateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["award-id"] = listOf(AWARD_ID)
                    this["lot-id"] = listOf(LOT_ID)
                }
            )
        )
}
