package com.procurement.formsservice.service.ei

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.ei.create.EiCreateParameters
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EiCreateServiceTest : AbstractBase() {
    private val IDENTIFIER_SCHEMA = "MD-ION"
    private val COUNTRY = "MD"

    private lateinit var formTemplateService: FormTemplateService
    private lateinit var mdmRepository: MDMRepository

    private lateinit var service: EiCreateService

    private val allowableIdentifierSchema = setOf(IDENTIFIER_SCHEMA)
    private val allowableCountries = setOf(COUNTRY)

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        mdmRepository = mock()

        service = EiCreateServiceImpl(
            formTemplateService = formTemplateService,
            mdmRepository = mdmRepository
        )
    }

    @Test
    fun create() {
        val params = genEiCreateParameters()
        runBlocking {
            whenever(mdmRepository.countries(any()))
                .thenReturn(allowableCountries)

            whenever(mdmRepository.schemeRegistration(any(), any()))
                .thenReturn(allowableIdentifierSchema)

            val json = service.create(params).block()
            assertNotNull(json)
        }
    }

    @Test
    fun update() {
    }

    private fun genEiCreateParameters(): EiCreateParameters =
        EiCreateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                                                           .apply {
                                                               this["lang"] = listOf("EN")
                                                               this["country"] = listOf(COUNTRY)
                                                               this["identifierSchema"] = listOf(IDENTIFIER_SCHEMA)
                                                           }
            )
        )
}
