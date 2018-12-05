package com.procurement.formsservice.service.ac

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.ac.update.AwardContractUpdateData
import com.procurement.formsservice.model.ac.update.AwardContractUpdateParameters
import com.procurement.formsservice.model.ac.update.MSForAwardContractUpdateData
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AcUpdateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: AcUpdateService

    private val DATA_FOR_AC = genAwardContractUpdateData()
    private val DATA_FOR_AC_MS = genMSForAwardContractUpdateData()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1532011121824-EV-1532010122650"
        private const val LOT_ID = "0b93d403-a534-11e8-9139-b5b419c3af61"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()


        service = AcUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genBidUpdateParameters()

        whenever(publicPointService.getAwardContractUpdateData(any(), any()))
            .thenReturn(DATA_FOR_AC)

        whenever(publicPointService.getMSForAwardContractUpdateData(any()))
            .thenReturn(DATA_FOR_AC_MS)

        val json = service.update(params)
        assertNotNull(json)
    }

    private fun genBidUpdateParameters(): AwardContractUpdateParameters =
        AwardContractUpdateParameters(
            queryParameters = sensitiveQueryParameters(
                mutableMapOf<String, Array<String>>()
                    .apply {
                        this["lang"] = arrayOf("EN")
                        this["ocid"] = arrayOf(OCID)
                        this["lot-id"] = arrayOf(LOT_ID)
                    }
            )
        )

    private fun genAwardContractUpdateData(): AwardContractUpdateData {
        val file = RESOURCES.load("json/DATA_FOR_UPDATE_AC.json")
        return jsonJacksonMapper.toObject(file)
    }

    private fun genMSForAwardContractUpdateData(): MSForAwardContractUpdateData {
        val file = RESOURCES.load("json/DATA_FOR_UPDATE_AC_MS.json")
        return jsonJacksonMapper.toObject(file)
    }
}
