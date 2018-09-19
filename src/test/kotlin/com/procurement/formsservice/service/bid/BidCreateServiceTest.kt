package com.procurement.formsservice.service.bid

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.bid.create.BidCreateData
import com.procurement.formsservice.model.bid.create.BidCreateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BidCreateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: BidCreateService

    private val DATA_FOR_BID = genDataForBID()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1532011121824-EV-1532010122650"
        private const val LOT_ID = "1a8e2f80-8b5f-12e8-a48b-9f2980d5bbdd"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()


        service = BidCreateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genParams()
        runBlocking {
            whenever(publicPointService.getBidCreateData(any(), any()))
                .thenReturn(DATA_FOR_BID)

            val json = service.create(params).block()
            assertNotNull(json)
        }
    }

    private fun genParams(): BidCreateParameters =
        BidCreateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["ocid"] = listOf(OCID)
                    this["lot-id"] = listOf(LOT_ID)
                }
            )
        )

    private fun genDataForBID(): BidCreateData {
        val file = RESOURCES.load("json/DATA_FOR_CREATE_BID.json")
        return jsonJacksonMapper.toObject(file)
    }
}
