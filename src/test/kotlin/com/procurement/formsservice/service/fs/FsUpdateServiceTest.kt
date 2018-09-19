package com.procurement.formsservice.service.fs

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.exception.query.QueryParameterStateException
import com.procurement.formsservice.model.ei.update.EiUpdateData
import com.procurement.formsservice.model.fs.create.FsCreateData
import com.procurement.formsservice.model.fs.create.FsFunder
import com.procurement.formsservice.model.fs.create.FsCreateParameters
import com.procurement.formsservice.model.fs.create.FsPayer
import com.procurement.formsservice.model.fs.update.FsUpdateEiData
import com.procurement.formsservice.model.fs.update.FsUpdateFsData
import com.procurement.formsservice.model.fs.update.FsUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class FsUpdateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: FsUpdateService

    private val FS_UPDATE_FI_DATA = getFsUpdateEiData()
    private val FS_UPDATE_FS_DATA = getFsUpdateFsData()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1534235784518-FS-1534235785182"
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()


        service = FsUpdateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @Test
    fun test() {
        val params = genFsUpdateParameters()
        runBlocking {
            whenever(publicPointService.getFsUpdateEiData(any()))
                .thenReturn(FS_UPDATE_FI_DATA)
            whenever(publicPointService.getFsUpdateFsData(any(), any()))
                .thenReturn(FS_UPDATE_FS_DATA)

            service.update(params).block()
        }
    }

    private fun genFsUpdateParameters(): FsUpdateParameters =
        FsUpdateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["ocid"] = listOf(OCID)
                }
            )
        )

    private fun getFsUpdateEiData(): FsUpdateEiData {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_EI.json")
        return jsonJacksonMapper.toObject(json)
    }

    private fun getFsUpdateFsData(): FsUpdateFsData {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_FS.json")
        return jsonJacksonMapper.toObject(json)
    }
}
