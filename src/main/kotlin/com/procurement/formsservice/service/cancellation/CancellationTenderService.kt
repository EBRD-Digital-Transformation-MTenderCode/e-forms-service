package com.procurement.formsservice.service.cancellation

import com.procurement.formsservice.model.cancellation.tender.CancellationTenderContext
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderData
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface CancellationTenderService {
    fun cancel(queryParameters: CancellationTenderParameters): String
}

@Service
class CancellationTenderServiceImpl(
    private val formTemplateService: FormTemplateService,
    private val publicPointService: PublicPointService
) :
    CancellationTenderService {
    private val cancelationTenderTemplate = formTemplateService[KindTemplate.CANCELLATION, KindEntity.TENDER]

    override fun cancel(queryParameters: CancellationTenderParameters): String {
        val cpid = queryParameters.cpid.value
        val records = publicPointService.getCancellationTenderData(cpid = cpid).records

        val ms = getMS(cpid, records)

        val data = CancellationTenderContext(
            parameters = CancellationTenderContext.Parameters(
                ocid = cpid
            ),
            tender = CancellationTenderContext.Tender(
                title = ms.compiledRelease.tender.title,
                description = ms.compiledRelease.tender.description
            )
        )

        return formTemplateService.evaluate(
            template = cancelationTenderTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun getMS(cpid: String, records: List<CancellationTenderData.Record>): CancellationTenderData.Record {
        return records.first {
            it.ocid == cpid
        }
    }
}