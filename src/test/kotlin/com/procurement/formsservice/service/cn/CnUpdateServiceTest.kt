package com.procurement.formsservice.service.cn

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.cn.create.CnCreateData
import com.procurement.formsservice.model.cn.create.CnCreateParameters
import com.procurement.formsservice.model.cn.create.CnPmd
import com.procurement.formsservice.model.cn.create.CnProcuringEntity
import com.procurement.formsservice.model.cn.create.CnResponsibleContactPerson
import com.procurement.formsservice.model.cn.update.CnUpdateData
import com.procurement.formsservice.model.cn.update.CnUpdateParameters
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class CnUpdateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService

    private lateinit var service: CnUpdateService

    private val DATA_FOR_UPDATE_CN = genCnUpdateData()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1534853430662-EV-1534853430760"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()

        service = CnUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService,
            objectMapper = objectMapper
        )
    }

    @Test
    fun update() {
        val params = genParams()
        runBlocking {
            whenever(publicPointService.getCnUpdateData(any()))
                .thenReturn(DATA_FOR_UPDATE_CN)

            val json = service.update(params).block()
            assertNotNull(json)
        }
    }

    private fun genParams(): CnUpdateParameters =
        CnUpdateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["ocid"] = listOf(OCID)
                }
            )
        )

    private fun genCnUpdateData(): CnUpdateData {
        val file = RESOURCES.load("json/DATA_FOR_UPDATE_CN.json")
        return jsonJacksonMapper.toObject(file)
    }
}
