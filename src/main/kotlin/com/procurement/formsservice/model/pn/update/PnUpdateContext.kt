package com.procurement.formsservice.model.pn.update

class PnUpdateContext(
    val parameters: Parameters,
    val procuringEntity: ProcuringEntity,
    val tender: Tender
) {
    data class Parameters(
        val ocid: String
    )

    data class ProcuringEntity(
        val name: String, // MS.parties[role="procuringEntity"].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val contactPoint: ContactPoint,
        val uris: Uris
    ) {
        data class Uris(
            val country: String, // /country/MS.parties[role="procuringEntity"].address.addressDetails.country.id?lang=langFromRequest
            val region: String, // /region?lang=langFromRequest&country=MS.parties[role="procuringEntity"].address.addressDetails.country.id
            val locality: String, // /locality?lang=langFromRequest&country=MS.parties[role="procuringEntity"].address.addressDetails.country.id&region=MS.parties[role="procuringEntity"].address.addressDetails.region.id
            val registrationScheme: String // /registration-scheme?lang=langFromRequest&country=MS.parties[role="procuringEntity"].address.addressDetails.country.id
        )

        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // MS.parties[role="procuringEntity"].address.streetAddress
            val postalCode: String? // MS.parties[role="procuringEntity"].address.postalCode
        ) {
            data class Country(
                val id: String, // MS.parties[role="procuringEntity"].address.addressDetails.country.id
                val description: String // MS.parties[role="procuringEntity"].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // MS.parties[role="procuringEntity"].address.addressDetails.region.id
                val description: String // MS.parties[role="procuringEntity"].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // MS.parties[role="procuringEntity"].address.addressDetails.locality.scheme
                val id: String, // MS.parties[role="procuringEntity"].address.addressDetails.locality.id
                val description: String // MS.parties[role="procuringEntity"].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // MS.parties[role="procuringEntity"].identifier.scheme
            val id: String, // MS.parties[role="procuringEntity"].identifier.id
            val legalName: String, // MS.parties[role="procuringEntity"].identifier.legalName
            val uri: String? // MS.parties[role="procuringEntity"].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // MS.parties[role="procuringEntity"].additionalIdentifier[i].scheme
            val id: String, // MS.parties[role="procuringEntity"].additionalIdentifier[i].id
            val legalName: String, // MS.parties[role="procuringEntity"].additionalIdentifier[i].legalName
            val uri: String? // MS.parties[role="procuringEntity"].additionalIdentifier[i].uri
        )

        data class ContactPoint(
            val name: String, // MS.parties[role="procuringEntity"].contactPoint.name
            val url: String?, // MS.parties[role="procuringEntity"].contactPoint.url
            val telephone: String, // MS.parties[role="procuringEntity"].contactPoint.telephone
            val email: String, // MS.parties[role="procuringEntity"].contactPoint.email
            val faxNumber: String? // MS.parties[role="procuringEntity"].contactPoint.faxNumber
        )
    }

    data class Tender(
        val title: String, // MS.tender.title
        val description: String, // MS.tender.description
        val documents: List<Document>?, // PN.tender.documents
        val lots: List<Lot>?, // PN.tender.lots
        val procurementMethodDetails: String, // MS.tender.procurementMethodDetails
        val procurementMethodRationale: String?, // MS.tender.procurementMethodRationale
        val procurementMethodAdditionalInfo: String?, // MS.tender.procurementMethodAdditionalInfo
        val legalBasis: String,  // MS.tender.legalBasis
        val validityPeriod: String, // PN.tender.tenderPeriod.startDate
        val budgetBreakdown: List<BudgetBreakdown>,
        val uris: Uris,
        val currency: String // MS.tender.value.currency
    ) {
        data class Document(
            val id: String, // PN.tender.documents[hasn`t field relatedLots].id
            val type: String, // PN.tender.documents[hasn`t field relatedLots].documentType
            val title: String?, // PN.tender.documents[hasn`t field relatedLots].title
            val description: String? // PN.tender.documents[hasn`t field relatedLots].description
        )

        data class Lot(
            val id: String, // PN.tender.lots[i].id
            val internalId: String?, // PN.tender.lots[i].internalId
            val title: String, // PN.tender.lots[i].title
            val description: String, // PN.tender.lots[i].description
            val value: Value,
            val performance: Performance,
            val items: List<Item>?, // PN.tender.items
            val documents: List<Document>? // PN.tender.documents
        ) {
            data class Value(
                val amount: Double, // PN.tender.lots[i].value.amount
                val currency: String // PN.tender.lots[i].value.currency
            )

            data class Performance(
                val placeOfPerformance: PlaceOfPerformance?, // PN.tender.lots[i].placeOfPerformance
                val deliveryPeriod: DeliveryPeriod
            ) {
                data class PlaceOfPerformance(
                    val address: Address?, // PN.tender.lots[i].placeOfPerformance.address
                    val description: String? // PN.tender.lots[i].placeOfPerformance.description
                ) {
                    data class Address(
                        val country: Country,
                        val region: Region,
                        val locality: Locality,
                        val streetAddress: String, // PN.tender.lots[i].placeOfPerformance.address.streetAddress
                        val postalCode: String? // PN.tender.lots[i].placeOfPerformance.address.postalCode
                    ) {
                        data class Country(
                            val id: String, // PN.tender.lots[i].placeOfPerformance.address.addressDetails.country.id
                            val description: String // PN.tender.lots[i].placeOfPerformance.address.addressDetails.country.description
                        )

                        data class Region(
                            val id: String, // PN.tender.lots[i].placeOfPerformance.address.addressDetails.region.id
                            val description: String // PN.tender.lots[i].placeOfPerformance.address.addressDetails.region.description
                        )

                        data class Locality(
                            val scheme: String, // PN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.scheme
                            val id: String, // PN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.id
                            val description: String // PN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.description
                        )
                    }
                }

                data class DeliveryPeriod(
                    val startDate: String, // PN.tender.lots[i].contractPeriod.startDate
                    val endDate: String // PN.tender.lots[i].contractPeriod.startDate
                )
            }

            data class Item(
                val id: String, // PN.tender.items[relatedLot = lot.id].id
                val internalId: String?, // PN.tender.items[relatedLot = lot.id].internalId
                val relatedLot: String, // PN.tender.items[relatedLot = lot.id].relatedLot
                val description: String?, // PN.tender.items[relatedLot = lot.id] description
                val quantity: Quantity,
                val classification: Classification,
                val additionalClassifications: List<AdditionalClassification>?
            ) {
                data class Quantity(
                    val quantity: Double, // PN.tender.items[relatedLot = lot.id] quantity
                    val unitClass: UnitClass,
                    val unit: Unit
                ) {
                    data class UnitClass(
                        val id: String, // temporarily empty
                        val description: String // temporarily empty
                    )

                    data class Unit(
                        val id: String, // PN.tender.items[relatedLot = lot.id] unit.id
                        val description: String // PN.tender.items[relatedLot = lot.id] unit.name
                    )
                }

                data class Classification(
                    val scheme: String, // PN.tender.items[relatedLot = lot.id] classification.scheme
                    val id: String, // PN.tender.items[relatedLot = lot.id] classification.id
                    val description: String, // PN.tender.items[relatedLot = lot.id] classification.description
                    val title: String // "PN.tender.items[relatedLot = lot.id] classification.id - PN.tender.items[relatedLot = lot.id] classification.description"
                )

                data class AdditionalClassification(
                    val scheme: String, // PN.tender.items[relatedLot = lot.id] additionalClassifications[i].scheme
                    val id: String, // PN.tender.items[relatedLot = lot.id] additionalClassifications[i].id
                    val description: String, // PN.tender.items[relatedLot = lot.id] additionalClassifications[i].description
                    val title: String // "PN.tender.items[relatedLot = lot.id] additionalClassifications[i].id - PN.tender.items[relatedLot = lot.id] additionalClassifications[i].description"
                )
            }

            data class Document(
                val id: String, // PN.tender.documents[relatedLots[0] = lot.id].id
                val type: String, // PN.tender.documents[relatedLots[0] = lot.id].documentType
                val title: String?, // PN.tender.documents[relatedLots[0] = lot.id].title
                val description: String?, // PN.tender.documents[relatedLots[0] = lot.id].description
                val relatedLots: String // PN.tender.documents[relatedLots[0] = lot.id].relatedLots[0]
            )
        }

        data class BudgetBreakdown(
            val id: String, // MS.planning.budget.budgetBreakdown[i].id
            val amount: Amount
        ) {
            data class Amount(
                val amount: Double, // MS.planning.budget.budgetBreakdown[i].amount.amount
                val currency: String // MS.planning.budget.budgetBreakdown[i].amount.currency
            )
        }

        data class Uris(
            val country: String, // /country?lang=langFromRequest
            val region: String, // /region?lang=langFromRequest&country=$country$
            val locality: String, // /locality?lang=langFromRequest&country=$country$&region=$region$
            val unitClass: String, // /unit-class?lang=langFromRequest
            val unit: String, // /unit?lang=langFromRequest&unitClass=$unitClass$
            val cpv: String, // /cpv?lang=langFromRequest&code=MS.tender.classification.id
            val cpvs: String, // /cpvs?lang=langFromRequest
            val pmd: String // /pmd?lang=langFromRequest&country=(ocds-t1s2t3->MD<-1532010121824-PN-1532010122650)
        )
    }
}
