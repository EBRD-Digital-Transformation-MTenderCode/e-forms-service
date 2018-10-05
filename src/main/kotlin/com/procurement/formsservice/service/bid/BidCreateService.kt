package com.procurement.formsservice.service.bid

import com.procurement.formsservice.domain.OCID
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.bid.LotNotFoundException
import com.procurement.formsservice.model.bid.create.BidCreateContext
import com.procurement.formsservice.model.bid.create.BidCreateData
import com.procurement.formsservice.model.bid.create.BidCreateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface BidCreateService {
    fun create(queryParameters: BidCreateParameters): String
}

@Service
class BidCreateServiceImpl(private val formTemplateService: FormTemplateService,
                           private val publicPointService: PublicPointService) :
    BidCreateService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.BID]

    override fun create(queryParameters: BidCreateParameters): String {
        val ocid = queryParameters.ocid
        val cpid = queryParameters.ocid.toCPID()
        val release = publicPointService.getBidCreateData(cpid = cpid.value, ocid = ocid.value).releases[0]

        val data = BidCreateContext(
            parameters = BidCreateContext.Parameters(
                ocid = queryParameters.ocid.toString(),
                lotId = queryParameters.lotid
            ),
            uris = uris(queryParameters),
            amount = amount(queryParameters, release.tender)
        )
        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    fun uris(queryParameters: BidCreateParameters): BidCreateContext.Uris {
        val country = queryParameters.ocid.country
        return BidCreateContext.Uris(
            country = "${MDMKind.COUNTRY}?lang=${queryParameters.lang}",
            region = "${MDMKind.REGION}?lang=${queryParameters.lang}&country=\$country\$",
            locality = "${MDMKind.LOCALITY}?lang=${queryParameters.lang}&country=\$country\$&region=\$region\$",
            registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=${queryParameters.lang}&country=\$country\$",
            currency = "${MDMKind.CURRENCY}?lang=${queryParameters.lang}&country=$country"
        )
    }

    fun amount(queryParameters: BidCreateParameters, tender: BidCreateData.Release.Tender): BidCreateContext.Amount? =
        if (queryParameters.ocid.entity == OCID.Entity.EV) {
            findLot(queryParameters = queryParameters, tender = tender).let {
                BidCreateContext.Amount(
                    currency = it.value.currency,
                    maxAmount = it.value.amount
                )
            }
        } else null

    fun findLot(queryParameters: BidCreateParameters,
                tender: BidCreateData.Release.Tender): BidCreateData.Release.Tender.Lot =
        tender.lots.find {
            it.id == queryParameters.lotid
        } ?: throw LotNotFoundException(ocid = queryParameters.ocid.toString(), lotid = queryParameters.lotid)
}
