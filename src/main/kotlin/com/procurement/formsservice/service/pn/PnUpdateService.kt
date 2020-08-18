package com.procurement.formsservice.service.pn

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.model.pn.Role
import com.procurement.formsservice.model.pn.update.PnUpdateContext
import com.procurement.formsservice.model.pn.update.PnUpdateData
import com.procurement.formsservice.model.pn.update.PnUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

interface PnUpdateService {
    fun update(queryParameters: PnUpdateParameters): String
}

@Service
class PnUpdateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService,
                          private val objectMapper: ObjectMapper) : PnUpdateService {
    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.PN]

    override fun update(queryParameters: PnUpdateParameters): String {
        val cpid = queryParameters.ocid.toCPID().value
        val ocid = queryParameters.ocid.value

        val records: List<PnUpdateData.Record> = publicPointService.getPnUpdateData(cpid = cpid).records
        val ms = getMS(cpid, records)
        val pn = getPN(ocid, records)

        val data = PnUpdateContext(
            parameters = PnUpdateContext.Parameters(
                ocid = queryParameters.ocid.value
            ),
            procuringEntity = procuringEntity(queryParameters = queryParameters, ms = ms),
            tender = tender(queryParameters = queryParameters, ms = ms, pn = pn)
        )

        return formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun getMS(cpid: String, records: List<PnUpdateData.Record>): PnUpdateData.Record.MS {
        return records.first {
            it.ocid == cpid
        }.let {
            toObject<PnUpdateData.Record.MS>(it.compiledRelease)
        }
    }

    private fun getPN(ocid: String, records: List<PnUpdateData.Record>): PnUpdateData.Record.PN {
        return records.first {
            it.ocid == ocid
        }.let {
            toObject<PnUpdateData.Record.PN>(it.compiledRelease)
        }
    }

    private inline fun <reified T> toObject(json: JsonNode): T = try {
        objectMapper.treeToValue(json, T::class.java)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }

    private fun procuringEntity(queryParameters: PnUpdateParameters,
                                ms: PnUpdateData.Record.MS): PnUpdateContext.ProcuringEntity {
        val party = ms.parties.first {
            it.roles.contains(Role.PROCURING_ENTITY)
        }

        val lang = queryParameters.lang
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality

        return PnUpdateContext.ProcuringEntity(
            name = party.name,
            address = PnUpdateContext.ProcuringEntity.Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                country = PnUpdateContext.ProcuringEntity.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = PnUpdateContext.ProcuringEntity.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = PnUpdateContext.ProcuringEntity.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                )
            ),
            identifier = party.identifier.let { identifier ->
                PnUpdateContext.ProcuringEntity.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            },
            additionalIdentifiers = party.additionalIdentifiers?.map { additionalIdentifier ->
                PnUpdateContext.ProcuringEntity.AdditionalIdentifier(
                    scheme = additionalIdentifier.scheme,
                    id = additionalIdentifier.id,
                    legalName = additionalIdentifier.legalName,
                    uri = additionalIdentifier.uri
                )
            },
            contactPoint = party.contactPoint.let { contactPoint ->
                PnUpdateContext.ProcuringEntity.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            },
            uris = PnUpdateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}/${country.id}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=${country.id}",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=${country.id}&region=${region.id}",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=${country.id}"
            )
        )
    }

    private fun tender(queryParameters: PnUpdateParameters,
                       ms: PnUpdateData.Record.MS,
                       pn: PnUpdateData.Record.PN): PnUpdateContext.Tender {
        val lang = queryParameters.lang

        return PnUpdateContext.Tender(
            title = ms.tender.title,
            description = ms.tender.description,
            documents = tenderDocuments(pn),
            lots = lots(pn),
            procurementMethodDetails = ms.tender.procurementMethodDetails,
            legalBasis = ms.tender.legalBasis,
            validityPeriod = pn.tender.tenderPeriod.startDate,
            budgetBreakdown = budgetBreakdown(ms),
            procurementMethodRationale = ms.tender.procurementMethodDetails,
            procurementMethodAdditionalInfo = ms.tender.procurementMethodAdditionalInfo,
            uris = PnUpdateContext.Tender.Uris(
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

    private fun tenderDocuments(pn: PnUpdateData.Record.PN): List<PnUpdateContext.Tender.Document>? {
        return pn.tender.documents?.asSequence()
            ?.filter {
                it.relatedLots == null || it.relatedLots.isEmpty()
            }?.map {
                PnUpdateContext.Tender.Document(
                    id = it.id,
                    type = it.documentType,
                    title = it.title,
                    description = it.description
                )
            }?.toList()
    }

    private fun lots(pn: PnUpdateData.Record.PN): List<PnUpdateContext.Tender.Lot>? {
        return pn.tender.lots?.map { lot ->
            PnUpdateContext.Tender.Lot(
                id = lot.id,
                internalId = lot.internalId,
                title = lot.title,
                description = lot.description,
                value = PnUpdateContext.Tender.Lot.Value(
                    amount = lot.value.amount,
                    currency = lot.value.currency
                ),
                performance = performance(lot),
                items = items(lotId = lot.id, pn = pn),
                documents = documents(lotId = lot.id, pn = pn)
            )
        }
    }

    private fun performance(lot: PnUpdateData.Record.PN.Tender.Lot): PnUpdateContext.Tender.Lot.Performance {
        val placeOfPerformance = lot.placeOfPerformance?.let {
            PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance(
                address = performanceAddress(lot.placeOfPerformance),
                description = lot.placeOfPerformance.description
            )
        }

        return PnUpdateContext.Tender.Lot.Performance(
            placeOfPerformance = placeOfPerformance,
            deliveryPeriod = PnUpdateContext.Tender.Lot.Performance.DeliveryPeriod(
                startDate = lot.contractPeriod.startDate,
                endDate = lot.contractPeriod.endDate
            )
        )
    }

    private fun performanceAddress(placeOfPerformance: PnUpdateData.Record.PN.Tender.Lot.PlaceOfPerformance): PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address? {
        return placeOfPerformance.address?.let { address ->
            PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                country = PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Country(
                    id = address.addressDetails.country.id,
                    description = address.addressDetails.country.description
                ),
                region = PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Region(
                    id = address.addressDetails.region.id,
                    description = address.addressDetails.region.description
                ),
                locality = PnUpdateContext.Tender.Lot.Performance.PlaceOfPerformance.Address.Locality(
                    scheme = address.addressDetails.locality.scheme,
                    id = address.addressDetails.locality.id,
                    description = address.addressDetails.locality.description
                )
            )
        }
    }

    private fun items(lotId: String, pn: PnUpdateData.Record.PN): List<PnUpdateContext.Tender.Lot.Item>? =
        pn.tender.items?.asSequence()?.filter {
            it.relatedLot == lotId
        }?.map { item ->
            PnUpdateContext.Tender.Lot.Item(
                id = item.id,
                internalId = item.internalId,
                relatedLot = item.relatedLot,
                description = item.description,
                quantity = PnUpdateContext.Tender.Lot.Item.Quantity(
                    quantity = item.quantity,
                    unitClass = PnUpdateContext.Tender.Lot.Item.Quantity.UnitClass(
                        id = "",
                        description = ""
                    ),
                    unit = PnUpdateContext.Tender.Lot.Item.Quantity.Unit(
                        id = item.unit.id,
                        description = item.unit.name
                    )
                ),
                classification = PnUpdateContext.Tender.Lot.Item.Classification(
                    scheme = item.classification.scheme,
                    id = item.classification.id,
                    description = item.classification.description,
                    title = "${item.classification.id} - ${item.classification.description}"
                ),
                additionalClassifications = item.additionalClassifications?.map {
                    PnUpdateContext.Tender.Lot.Item.AdditionalClassification(
                        scheme = it.scheme,
                        id = it.id,
                        description = it.description,
                        title = "${it.id} - ${it.description}"
                    )
                }
            )
        }?.toList()

    private fun documents(lotId: String, pn: PnUpdateData.Record.PN): List<PnUpdateContext.Tender.Lot.Document>? =
        pn.tender.documents?.asSequence()
            ?.filter {
                it.relatedLots != null && it.relatedLots[0] == lotId
            }?.map {
                PnUpdateContext.Tender.Lot.Document(
                    id = it.id,
                    type = it.documentType,
                    title = it.title,
                    description = it.description,
                    relatedLots = it.relatedLots!![0]
                )
            }?.toList()

    private fun budgetBreakdown(ms: PnUpdateData.Record.MS): List<PnUpdateContext.Tender.BudgetBreakdown> =
        ms.planning.budget.budgetBreakdown.map {
            PnUpdateContext.Tender.BudgetBreakdown(
                id = it.id,
                amount = PnUpdateContext.Tender.BudgetBreakdown.Amount(
                    amount = it.amount.amount,
                    currency = it.amount.currency
                )
            )
        }
}
