package com.procurement.formsservice.service.pn

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.domain.query.v4.sensitiveQueryParameters
import com.procurement.formsservice.model.pn.create.PnCreateData
import com.procurement.formsservice.model.pn.create.PnCreateParameters
import com.procurement.formsservice.model.pn.create.PnPmd
import com.procurement.formsservice.model.pn.create.PnProcuringEntity
import com.procurement.formsservice.model.pn.create.PnResponsibleContactPerson
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.FormTemplateServiceImpl
import com.procurement.formsservice.service.PublicPointService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

class PnCreateServiceTest : AbstractBase() {
    private lateinit var formTemplateService: FormTemplateService
    private lateinit var publicPointService: PublicPointService
    private lateinit var mdmRepository: MDMRepository

    private lateinit var service: PnCreateService

    private val EI_FOR_PN = genEiForPn()
    private val allowablePmd = enumValues<PnPmd>().map { it.toString() }.toSet()

    companion object {
        private const val OCID = "ocds-t1s2t3-MD-1532010121824-EV-1532010122650"

        @JvmStatic
        fun genTestData(): Stream<Arguments> {
            val list = mutableListOf<Arguments>()
            for (procuringEntity in enumValues<PnProcuringEntity>()) {
                for (responsibleContactPerson in enumValues<PnResponsibleContactPerson>()) {
                    for (pmd in enumValues<PnPmd>()) {
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

        service = PnCreateServiceImpl(
            formTemplateService = formTemplateService,
            publicPointService = publicPointService,
            mdmRepository = mdmRepository
        )
    }

    @ParameterizedTest
    @MethodSource("genTestData")
    fun test(procuringEntity: PnProcuringEntity,
             responsibleContactPerson: PnResponsibleContactPerson,
             pmd: PnPmd) {
        val params = genParams(procuringEntity, responsibleContactPerson, pmd)

        whenever(publicPointService.getPnCreateData(any()))
            .thenReturn(EI_FOR_PN)

        whenever(mdmRepository.pmd(any(), any()))
            .thenReturn(allowablePmd)

        val json = service.create(params)
        assertNotNull(json)
    }

    private fun genParams(procuringEntity: PnProcuringEntity,
                          responsibleContactPerson: PnResponsibleContactPerson,
                          pmd: PnPmd): PnCreateParameters =
        PnCreateParameters(
            queryParameters = sensitiveQueryParameters(
                mutableMapOf<String, Array<String>>()
                    .apply {
                        this["lang"] = arrayOf("EN")
                        this["ocid"] = arrayOf(OCID)
                        this["procuringEntity"] = arrayOf(procuringEntity.name)
                        this["responsibleContactPerson"] = arrayOf(responsibleContactPerson.name)
                        this["pmd"] = arrayOf(pmd.name)
                    }
            )
        )

    private fun genEiForPn(): PnCreateData {
        val file = RESOURCES.load("json/DATA_FOR_CREATE_PN.json")
        return jsonJacksonMapper.toObject(file)
    }
}
