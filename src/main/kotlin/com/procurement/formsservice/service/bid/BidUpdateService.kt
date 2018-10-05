package com.procurement.formsservice.service.bid

import com.procurement.formsservice.domain.OCID
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.bid.LotNotFoundException
import com.procurement.formsservice.model.bid.update.BidUpdateContext
import com.procurement.formsservice.model.bid.update.BidUpdateData
import com.procurement.formsservice.model.bid.update.BidUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface BidUpdateService {
    fun update(queryParameters: BidUpdateParameters): String
}

@Service
class BidUpdateServiceImpl(private val formTemplateService: FormTemplateService,
                           private val publicPointService: PublicPointService) : BidUpdateService {
    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.BID]

    override fun update(queryParameters: BidUpdateParameters): String {
        val ocid = queryParameters.ocid
        val cpid = ocid.toCPID()
        val records = publicPointService.getBidUpdateData(cpid = cpid.value).records

        val data = BidUpdateContext(
            parameters = BidUpdateContext.Parameters(
                ocid = queryParameters.ocid.toString(),
                lotId = queryParameters.lotid
            ),
            uris = uris(queryParameters),
            amount = amount(queryParameters = queryParameters, records = records)
        )
        return formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    fun uris(queryParameters: BidUpdateParameters): BidUpdateContext.Uris {
        val country = queryParameters.ocid.country
        return BidUpdateContext.Uris(
            country = "${MDMKind.COUNTRY}?lang=${queryParameters.lang}",
            region = "${MDMKind.REGION}?lang=${queryParameters.lang}&country=\$country\$",
            locality = "${MDMKind.LOCALITY}?lang=${queryParameters.lang}&country=\$country\$&region=\$region\$",
            registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=${queryParameters.lang}&country=\$country\$",
            currency = "${MDMKind.CURRENCY}?lang=${queryParameters.lang}&country=$country"
        )
    }

    fun amount(queryParameters: BidUpdateParameters, records: List<BidUpdateData.Record>): BidUpdateContext.Amount? =
        if (queryParameters.ocid.entity == OCID.Entity.EV) {
            findLot(queryParameters = queryParameters, records = records).let {
                BidUpdateContext.Amount(
                    currency = it.value.currency,
                    maxAmount = it.value.amount
                )
            }
        } else null

    private fun findLot(queryParameters: BidUpdateParameters,
                        records: List<BidUpdateData.Record>): BidUpdateData.Record.CompiledRelease.Tender.Lot {
        for (record in records) {
            val lots = record.compiledRelease.tender.lots ?: continue
            for (lot in lots) {
                if (lot.id == queryParameters.lotid)
                    return lot
            }
        }
        throw LotNotFoundException(ocid = queryParameters.ocid.toString(), lotid = queryParameters.lotid)
    }
}
