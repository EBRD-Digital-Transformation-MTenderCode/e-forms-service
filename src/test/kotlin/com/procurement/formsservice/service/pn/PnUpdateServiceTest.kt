package com.procurement.formsservice.service.pn

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.pn.update.PnUpdateData
import com.procurement.formsservice.model.pn.update.PnUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PnUpdateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService

    private lateinit var service: PnUpdateService

    private val DATA_FOR_UPDATE_PN = genPnUpdateData()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1534846158170-PN-1534846158284"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()

        service = PnUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService,
            objectMapper = objectMapper
        )
    }

    @Test
    fun update() {
        val params = genParams()

        whenever(publicPointService.getPnUpdateData(any()))
            .thenReturn(DATA_FOR_UPDATE_PN)

        val json = service.update(params)
        assertNotNull(json)
    }

    private fun genParams(): PnUpdateParameters =
        PnUpdateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, Array<String>>()
                .apply {
                    this["lang"] = arrayOf("EN")
                    this["ocid"] = arrayOf(OCID)
                }
            )
        )

    private fun genPnUpdateData(): PnUpdateData {
        val file = RESOURCES.load("json/DATA_FOR_UPDATE_PN.json")
        return jsonJacksonMapper.toObject(file)
    }
}
