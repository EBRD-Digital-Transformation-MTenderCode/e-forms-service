package com.procurement.formsservice.service

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.award.update.AwardUpdateParameters
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
        val json = service.update(params)
        assertNotNull(json)
    }

    private fun genParams(): AwardUpdateParameters =
        AwardUpdateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, Array<String>>()
                .apply {
                    this["lang"] = arrayOf("EN")
                    this["award-id"] = arrayOf(AWARD_ID)
                    this["lot-id"] = arrayOf(LOT_ID)
                }
            )
        )
}
