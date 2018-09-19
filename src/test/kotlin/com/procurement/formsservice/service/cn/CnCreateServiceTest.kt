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
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class CnCreateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var mdmRepository: MDMRepository

    private lateinit var service: CnCreateService

    private val EI_FOR_CN = genEiForCn()
    private val allowablePmd = enumValues<CnPmd>().map { it.toString() }.toSet()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1532010121824-EV-1532010122650"

        @JvmStatic
        fun genTestData(): Stream<Arguments> {
            val list = mutableListOf<Arguments>()
            for (procuringEntity in enumValues<CnProcuringEntity>()) {
                for (responsibleContactPerson in enumValues<CnResponsibleContactPerson>()) {
                    for (pmd in enumValues<CnPmd>()) {
                        list.add(
                            Arguments.of(
                                procuringEntity,
                                responsibleContactPerson,
                                pmd
                            )
                        )
                    }
                }
            }
            return list.asSequence().asStream()
        }
    }

    @BeforeEach
    fun setUp() {
        formTemplateService = FormTemplateServiceImpl(objectMapper)
        publicPointService = mock()
        mdmRepository = mock()

        service = CnCreateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService,
            mdmRepository = mdmRepository
        )
    }

    @ParameterizedTest
    @MethodSource("genTestData")
    fun test(procuringEntity: CnProcuringEntity,
             responsibleContactPerson: CnResponsibleContactPerson,
             pmd: CnPmd) {
        val params = genParams(procuringEntity, responsibleContactPerson, pmd)
        runBlocking {
            whenever(publicPointService.getCnCreateData(any()))
                .thenReturn(EI_FOR_CN)

            whenever(mdmRepository.pmd(any(), any()))
                .thenReturn(allowablePmd)

            val json = service.create(params).block()
            assertNotNull(json)
        }
    }

    private fun genParams(procuringEntity: CnProcuringEntity,
                          responsibleContactPerson: CnResponsibleContactPerson,
                          pmd: CnPmd): CnCreateParameters =
        CnCreateParameters(
            queryParameters = sensitiveQueryParameters(mutableMapOf<String, List<String>>()
                .apply {
                    this["lang"] = listOf("EN")
                    this["ocid"] = listOf(OCID)
                    this["procuringEntity"] = listOf(procuringEntity.name)
                    this["responsibleContactPerson"] = listOf(responsibleContactPerson.name)
                    this["pmd"] = listOf(pmd.name)
                }
            )
        )

    private fun genEiForCn(): CnCreateData {
        val file = RESOURCES.load("json/DATA_FOR_CREATE_CN.json")
        return jsonJacksonMapper.toObject(file)
    }
}
