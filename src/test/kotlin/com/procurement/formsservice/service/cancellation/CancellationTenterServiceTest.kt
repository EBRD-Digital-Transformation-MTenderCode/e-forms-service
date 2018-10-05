package com.procurement.formsservice.service.cancellation

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderData
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CancellationTenterServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: CancellationTenderService

    private val DATA_FOR_CANCELLATION_TENDER = genDataForCancellationTender()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1536828233229"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()


        service = CancellationTenderServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genParams()

        whenever(publicPointService.getCancellationTenderData(any()))
            .thenReturn(DATA_FOR_CANCELLATION_TENDER)

        val json = service.cancel(params)
        assertNotNull(json)
    }

    private fun genParams(): CancellationTenderParameters =
        CancellationTenderParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, Array<String>>()
                .apply {
                    this["lang"] = arrayOf("EN")
                    this["ocid"] = arrayOf(OCID)
                }
            )
        )

    private fun genDataForCancellationTender(): CancellationTenderData {
        val file = RESOURCES.load("json/DATA_FOR_CANCELLATION_TENDER.json")
        return jsonJacksonMapper.toObject(file)
    }
}
