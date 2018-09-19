package com.procurement.formsservice.service.cn

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.model.cn.Role
import com.procurement.formsservice.model.cn.update.CnUpdateContext
import com.procurement.formsservice.model.cn.update.CnUpdateData
import com.procurement.formsservice.model.cn.update.CnUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.IOException
import java.util.*

interface CnUpdateService {
    fun update(queryParameters: CnUpdateParameters): Mono<String>
}

@Service
class CnUpdateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService,
                          private val objectMapper: ObjectMapper) : CnUpdateService {

    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.CN]

    override fun update(queryParameters: CnUpdateParameters): Mono<String> = mono {
        val cpid = queryParameters.ocid.toCPID().value
        val ocid = queryParameters.ocid.value

        val records: List<CnUpdateData.Record> = publicPointService.getCnUpdateData(cpid = cpid).records
        val ms = getMS(cpid, records)
        val cn = getCN(ocid, records)

        val data = CnUpdateContext(
            parameters = CnUpdateContext.Parameters(
                ocid = queryParameters.ocid.value
            ),
            procuringEntity = procuringEntity(queryParameters = queryParameters, ms = ms),
            tender = tender(queryParameters = queryParameters, ms = ms, cn = cn)
        )

        formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun getMS(cpid: String, records: List<CnUpdateData.Record>): CnUpdateData.Record.MS {
        return records.first {
            it.ocid == cpid
        }.let {
            toObject(it.compiledRelease)
        }
    }

    private fun getCN(ocid: String, records: List<CnUpdateData.Record>): CnUpdateData.Record.CN {
        return records.first {
            it.ocid == ocid
        }.let {
            toObject(it.compiledRelease)
        }
    }

    private inline fun <reified T> toObject(json: JsonNode): T = try {
        objectMapper.treeToValue(json, T::class.java)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }

    private fun procuringEntity(queryParameters: CnUpdateParameters,
                                ms: CnUpdateData.Record.MS): CnUpdateContext.ProcuringEntity {
        val party = ms.parties.first {
            it.roles.contains(Role.PROCURING_ENTITY)
        }

        val lang = queryParameters.lang
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality

        return CnUpdateContext.ProcuringEntity(
            name = party.name,
            address = CnUpdateContext.ProcuringEntity.Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                country = CnUpdateContext.ProcuringEntity.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = CnUpdateContext.ProcuringEntity.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = CnUpdateContext.ProcuringEntity.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                )
            ),
            identifier = party.identifier.let { identifier ->
                CnUpdateContext.ProcuringEntity.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            },
            additionalIdentifiers = party.additionalIdentifiers?.map { additionalIdentifier ->
                CnUpdateContext.ProcuringEntity.AdditionalIdentifier(
                    scheme = additionalIdentifier.scheme,
                    id = additionalIdentifier.id,
                    legalName = additionalIdentifier.legalName,
                    uri = additionalIdentifier.uri
                )
            },
            contactPoint = party.contactPoint.let { contactPoint ->
                CnUpdateContext.ProcuringEntity.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            },
            uris = CnUpdateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}/${country.id}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=${country.id}",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=${country.id}&region=${region.id}",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=${country.id}"
            )
        )
    }

    private fun tender(queryParameters: CnUpdateParameters,
                       ms: CnUpdateData.Record.MS,
                       cn: CnUpdateData.Record.CN): CnUpdateContext.Tender {
        val lang = queryParameters.lang

        return CnUpdateContext.Tender(
            title = ms.tender.title,
            description = ms.tender.description,
            documents = tenderDocuments(cn),
            lots = lots(cn),
            procurementMethodDetails = ms.tender.procurementMethodDetails,
            legalBasis = ms.tender.legalBasis,
            validityPeriod = cn.tender.tenderPeriod.endDate,
            budgetBreakdown = budgetBreakdown(ms),
            uris = CnUpdateContext.Tender.Uris(
              country = "${MDMKind.COUNTRY}?lang=$lang",
              region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
              locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
              unitClass = "${MDMKind.UNIT_CLASS}?lang=$lang",
              unit = "${MDMKind.UNIT}?lang=$lang&unitClass=\$unitClass\$",
              cpv = "${MDMKind.CPV}?lang=$lang&code=${ms.tender.classification.id}",
              cpvs = "${MDMKind.CPVS}?lang=$lang",
              pmd = "${MDMKind.PMD}?lang=$lang&country=${queryParameters.ocid.country}"
            ),
            currency = ms.tender.value.currency
        )
    }

    private fun tenderDocuments(cn: CnUpdateData.Record.CN):List<CnUpdateContext.Tender.Document> {
        return cn.tender.documents.filter {
            it.relatedLots == null || it.relatedLots.isEmpty()
        }.map {
            CnUpdateContext.Tender.Document(
                id = it.id,
                type = it.documentType,
                title = it.title,
                description = it.description
            )
        }
    }

    private fun lots(cn: CnUpdateData.Record.CN): List<CnUpdateContext.Tender.Lot>? {
        return cn.tender.lots?.map {
            CnUpdateContext.Tender.Lot(
                id = it.id,
                title = it.title,
                description = it.description,
                value = CnUpdateContext.Tender.Lot.Value(
                    amount = it.value.amount,
                    currency = it.value.currency
                ),
                performance = CnUpdateContext.Tender.Lot.Performance(
                    placeOfPerformance = CnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance(
                        address = CnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address(
                            streetAddress = it.placeOfPerformance.address.streetAddress,
                            postalCode = it.placeOfPerformance.address.postalCode,
                            country = CnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Country(
                                id = it.placeOfPerformance.address.addressDetails.country.id,
                                description = it.placeOfPerformance.address.addressDetails.country.description
                            ),
                            region = CnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Region(
                                id = it.placeOfPerformance.address.addressDetails.region.id,
                                description = it.placeOfPerformance.address.addressDetails.region.description
                            ),
                            locality = CnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Locality(
                                scheme = it.placeOfPerformance.address.addressDetails.locality.scheme,
                                id = it.placeOfPerformance.address.addressDetails.locality.id,
                                description = it.placeOfPerformance.address.addressDetails.locality.description
                            )
                        ),
                        description = it.placeOfPerformance.description
                    ),
                    deliveryPeriod = CnUpdateContext.Tender.Lot.Performance.DeliveryPeriod(
                        startDate = it.contractPeriod.startDate,
                        endDate = it.contractPeriod.endDate
                    )
                ),
                items = items(lotId = it.id, cn = cn),
                documents = documents(lotId = it.id, cn = cn)
            )
        }
    }

    private fun items(lotId: String, cn: CnUpdateData.Record.CN): List<CnUpdateContext.Tender.Lot.Item>? =
        cn.tender.items?.filter {
            it.relatedLot == lotId
        }?.map { item ->
            CnUpdateContext.Tender.Lot.Item(
                id = item.id,
                relatedLot = item.relatedLot,
                description = item.description,
                quantity = CnUpdateContext.Tender.Lot.Item.Quantity(
                    quantity = item.quantity,
                    unitClass = CnUpdateContext.Tender.Lot.Item.Quantity.UnitClass(
                        id = "",
                        description = ""
                    ),
                    unit = CnUpdateContext.Tender.Lot.Item.Quantity.Unit(
                        id = item.unit.id,
                        description = item.unit.name
                    )
                ),
                classification = CnUpdateContext.Tender.Lot.Item.Classification(
                    scheme = item.classification.scheme,
                    id = item.classification.id,
                    description = item.classification.description,
                    title = "${item.classification.id} - ${item.classification.description}"
                ),
                additionalClassifications = item.additionalClassifications?.map {
                    CnUpdateContext.Tender.Lot.Item.AdditionalClassification(
                        scheme = it.scheme,
                        id = it.id,
                        description = it.description,
                        title = "${it.id} - ${it.description}"
                    )
                }
            )
        }

    private fun documents(lotId: String, cn: CnUpdateData.Record.CN): List<CnUpdateContext.Tender.Lot.Document>? =
        cn.tender.documents.filter {
            it.relatedLots != null && it.relatedLots[0] == lotId
        }.map {
            CnUpdateContext.Tender.Lot.Document(
                id = it.id,
                type = it.documentType,
                title = it.title,
                description = it.description,
                relatedLots = it.relatedLots!![0]
            )
        }

    private fun budgetBreakdown(ms: CnUpdateData.Record.MS): List<CnUpdateContext.Tender.BudgetBreakdown> =
        ms.planning.budget.budgetBreakdown.map {
            CnUpdateContext.Tender.BudgetBreakdown(
                id = it.id,
                amount = CnUpdateContext.Tender.BudgetBreakdown.Amount(
                    amount = it.amount.amount,
                    currency = it.amount.currency
                )
            )
        }
}