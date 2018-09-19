package com.procurement.formsservice.service.bid

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.bid.update.BidUpdateData
import com.procurement.formsservice.model.bid.update.BidUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BidUpdateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: BidUpdateService

    private val DATA_FOR_BID = genDataForUpdateBID()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1532011121824-EV-1532010122650"
        private const val LOT_ID = "0b93d403-a534-11e8-9139-b5b419c3af61"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()


        service = BidUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genBidUpdateParameters()
        runBlocking {
            whenever(publicPointService.getBidUpdateData(any()))
                .thenReturn(DATA_FOR_BID)

            val json = service.update(params).block()
            assertNotNull(json)
        }
    }

    private fun genBidUpdateParameters(): BidUpdateParameters =
        BidUpdateParameters(
            queryParameters = sensitiveQueryParameters(
                mutableMapOf<String, List<String>>()
                    .apply {
                        this["lang"] = listOf("EN")
                        this["ocid"] = listOf(OCID)
                        this["lot-id"] = listOf(LOT_ID)
                    }
            )
        )

    private fun genDataForUpdateBID(): BidUpdateData {
        val file = RESOURCES.load("json/DATA_FOR_UPDATE_BID.json")
        return jsonJacksonMapper.toObject(file)
    }
}
