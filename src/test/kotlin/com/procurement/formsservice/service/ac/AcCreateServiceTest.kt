package com.procurement.formsservice.service.ac

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.ac.create.AwardContractCreateData
import com.procurement.formsservice.model.ac.create.AwardContractCreateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AcCreateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: AcCreateService

    private val DATA_FOR_AC = genAwardContractCreateData()

    companion object {
        private const val OCID = "ocds-b3wdp1-MD-1543489512611-EV-1543495269659"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()

        service = AcCreateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genAwardContractCreateParameters()

        whenever(
            publicPointService.getAwardContractCreateData(
                eq("ocds-b3wdp1-MD-1543489512611"),
                eq("ocds-b3wdp1-MD-1543489512611-EV-1543495269659")
            )
        ).thenReturn(DATA_FOR_AC)

        val json = service.create(params)
        assertNotNull(json)
    }

    private fun genAwardContractCreateParameters(): AwardContractCreateParameters =
        AwardContractCreateParameters(
            queryParameters = sensitiveQueryParameters(
                mutableMapOf<String, Array<String>>()
                    .apply {
                        this["lang"] = arrayOf("EN")
                        this["ocid"] = arrayOf(OCID)
                    }
            )
        )

    private fun genAwardContractCreateData(): AwardContractCreateData {
        val file = RESOURCES.load("json/DATA_FOR_CREATE_AC.json")
        return jsonJacksonMapper.toObject(file)
    }
}
