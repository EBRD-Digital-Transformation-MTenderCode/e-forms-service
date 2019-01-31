package com.procurement.formsservice.service.ac

import com.procurement.formsservice.model.ac.create.AwardContractCreateContext
import com.procurement.formsservice.model.ac.create.AwardContractCreateData
import com.procurement.formsservice.model.ac.create.AwardContractCreateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface AcCreateService {
    fun create(queryParameters: AwardContractCreateParameters): String
}

@Service
class AcCreateServiceImpl(
    private val formTemplateService: FormTemplateService,
    private val publicPointService: PublicPointService
) : AcCreateService {

    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.AWARD_CONTRACT]

    override fun create(queryParameters: AwardContractCreateParameters): String {
        val cpid = queryParameters.ocid.toCPID().value
        val ocid = queryParameters.ocid.value
        val lang = queryParameters.lang

        val release = publicPointService.getAwardContractCreateData(cpid = cpid, ocid = ocid).releases[0]
        val data = getDate(ocid = ocid, release = release)

        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(lang)
        )
    }

    private fun getDate(ocid: String, release: AwardContractCreateData.Release): AwardContractCreateContext {
        return AwardContractCreateContext(
            parameters = AwardContractCreateContext.Parameters(
                ocid = ocid
            ),
            contracts = release.contracts.asSequence()
                .filter { contract ->
                    contract.statusDetails == "contractProject"
                }
                .map { contract ->
                    AwardContractCreateContext.Contract(
                        id = contract.id
                    )
                }
                .toList()
        )
    }
}
