package com.procurement.formsservice.service.fs

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.exception.query.QueryParameterStateException
import com.procurement.formsservice.model.fs.create.FsCreateData
import com.procurement.formsservice.model.fs.create.FsCreateParameters
import com.procurement.formsservice.model.fs.create.FsFunder
import com.procurement.formsservice.model.fs.create.FsPayer
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class FsCreateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var service: FsCreateService

    private val EI_FOR_FS = genEiForFs()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1534157336217"

        @JvmStatic
        fun genTestData(): Stream<Arguments> {
            val list = mutableListOf<Arguments>()
            for (fsFunder in enumValues<FsFunder>()) {
                for (fsPayer in enumValues<FsPayer>()) {
                    list.add(
                        Arguments.of(fsFunder.name, fsPayer.name, true)
                    )

                    list.add(
                        Arguments.of(fsFunder.name, fsPayer.name, false)
                    )
                }
            }
            return list.asSequence().asStream()
        }
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()

        service = FsCreateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService
        )
    }

    @ParameterizedTest
    @MethodSource("genTestData")
    fun test(fsFunder: FsFunder, fsPayer: FsPayer, isEuropeanUnionFunded: Boolean) {
        val params = genFsCreateParameters(fsFunder, fsPayer, isEuropeanUnionFunded)

        whenever(publicPointService.getFsCreateData(any()))
            .thenReturn(EI_FOR_FS)

        when {
            fsFunder == FsFunder.BUYER && fsPayer == FsPayer.FUNDER -> {
                assertThrows(QueryParameterStateException::class.java) {
                    service.create(params)
                }
            }
            fsFunder == FsFunder.STATE && fsPayer == FsPayer.FUNDER -> {
                assertThrows(QueryParameterStateException::class.java) {
                    service.create(params)
                }
            }
            else -> {
                val json = service.create(params)
                assertNotNull(json)
            }
        }
    }

    private fun genFsCreateParameters(fsFunder: FsFunder,
                                      fsPayer: FsPayer,
                                      isEuropeanUnionFunded: Boolean): FsCreateParameters =
        FsCreateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, Array<String>>()
                .apply {
                    this["lang"] = arrayOf("RU")
                    this["ocid"] = arrayOf(OCID)
                    this["funder"] = arrayOf(fsFunder.name)
                    this["payer"] = arrayOf(fsPayer.name)
                    this["isEuropeanUnionFunded"] = arrayOf(isEuropeanUnionFunded.toString())
                }
            )
        )

    private fun genEiForFs(): FsCreateData {
        val file = RESOURCES.load("json/DATA_FOR_CREATE_FS.json")
        return jsonJacksonMapper.toObject(file)
    }
}
