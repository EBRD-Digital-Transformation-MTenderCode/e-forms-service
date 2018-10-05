package com.procurement.formsservice.service.ei

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.ei.update.EiUpdateData
import com.procurement.formsservice.model.ei.update.EiUpdateParameters
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EiUpdateServiceTest : AbstractBase() {
    private val OCID = "ocds-t1s2t3-MD-1534235784518"

    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var mdmRepository: MDMRepository

    private lateinit var service: EiUpdateService

    private val EI_UPDATE_DATA = getEiUpdateData()

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()
        mdmRepository = mock()

        service = EiUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun update() {
        val params = genEiCreateParameters()

        whenever(publicPointService.getEiUpdateData(any()))
            .thenReturn(EI_UPDATE_DATA)

        val json = service.update(params)
        assertNotNull(json)
    }

    private fun genEiCreateParameters(): EiUpdateParameters =
        EiUpdateParameters(
            queryParameters = sensitiveQueryParameters(
                mutableMapOf<String, Array<String>>()
                    .apply {
                        this["lang"] = arrayOf("EN")
                        this["ocid"] = arrayOf(OCID)
                    }
            )
        )

    private fun getEiUpdateData(): EiUpdateData {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_EI.json")
        return jsonJacksonMapper.toObject<EiUpdateData>(json)
    }
}
