package com.procurement.formsservice.model.cn.update

class CnUpdateContext(
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
                val url: String, // MS.parties[role="procuringEntity"].contactPoint.url
                val telephone: String, // MS.parties[role="procuringEntity"].contactPoint.telephone
                val email: String, // MS.parties[role="procuringEntity"].contactPoint.email
                val faxNumber: String? // MS.parties[role="procuringEntity"].contactPoint.faxNumber
        )
    }

    data class Tender(
            val title: String, // MS.tender.title
            val description: String, // MS.tender.description
            val documents: List<Document>?,
            val lots: List<Lot>?,
            val procurementMethodDetails: String, // MS.tender.procurementMethodDetails
            val legalBasis: String,  // MS.tender.legalBasis
            val validityPeriod: String?, // CN.tender.tenderPeriod.endDate
            val budgetBreakdown: List<BudgetBreakdown>,
            val uris: Uris,
            val currency: String // MS.tender.value.currency
    ) {
        data class Document(
                val id: String, // CN.tender.documents[hasn`t field relatedLots].id
                val type: String, // CN.tender.documents[hasn`t field relatedLots].documentType
                val title: String?, // CN.tender.documents[hasn`t field relatedLots].title
                val description: String? // CN.tender.documents[hasn`t field relatedLots].description
        )

        data class Lot(
                val id: String, // CN.tender.lots[i].id
                val title: String, // CN.tender.lots[i].title
                val description: String, // CN.tender.lots[i].description
                val value: Value,
                val performance: Performance,
                val items: List<Item>?,
                val documents: List<Document>?
        ) {
            data class Value(
                    val amount: Double, // CN.tender.lots[i].value.amount
                    val currency: String // CN.tender.lots[i].value.currency
            )

            data class Performance(
                    val placeOfPerformance: PlaceOfPerformance,
                    val deliveryPeriod: DeliveryPeriod
            ) {
                data class PlaceOfPerformance(
                        val address: Address,
                        val description: String? // CN.tender.lots[i].placeOfPerformance.description
                ) {
                    data class Address(
                            val country: Country,
                            val region: Region,
                            val locality: Locality,
                            val streetAddress: String, // CN.tender.lots[i].placeOfPerformance.address.streetAddress
                            val postalCode: String? // CN.tender.lots[i].placeOfPerformance.address.postalCode
                    ) {
                        data class Country(
                                val id: String, // CN.tender.lots[i].placeOfPerformance.address.addressDetails.country.id
                                val description: String // CN.tender.lots[i].placeOfPerformance.address.addressDetails.country.description
                        )

                        data class Region(
                                val id: String, // CN.tender.lots[i].placeOfPerformance.address.addressDetails.region.id
                                val description: String // CN.tender.lots[i].placeOfPerformance.address.addressDetails.region.description
                        )

                        data class Locality(
                                val scheme: String, // CN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.scheme
                                val id: String, // CN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.id
                                val description: String // CN.tender.lots[i].placeOfPerformance.address.addressDetails.locality.description
                        )
                    }
                }

                data class DeliveryPeriod(
                        val startDate: String, // CN.tender.lots[i].contractPeriod.startDate
                        val endDate: String // CN.tender.lots[i].contractPeriod.startDate
                )
            }

            data class Item(
                    val id: String, // CN.tender.items[relatedLot = lot.id].id
                    val relatedLot: String, // CN.tender.items[relatedLot = lot.id].relatedLot
                    val description: String?, // CN.tender.items[relatedLot = lot.id] description
                    val quantity: Quantity,
                    val classification: Classification,
                    val additionalClassifications: List<AdditionalClassification>?
            ) {
                data class Quantity(
                        val quantity: Double, // CN.tender.items[relatedLot = lot.id] quantity
                        val unitClass: UnitClass,
                        val unit: Unit
                ) {
                    data class UnitClass(
                            val id: String, // temporarily empty
                            val description: String // temporarily empty
                    )

                    data class Unit(
                            val id: String, // CN.tender.items[relatedLot = lot.id] unit.id
                            val description: String // CN.tender.items[relatedLot = lot.id] unit.name
                    )
                }

                data class Classification(
                        val scheme: String, // CN.tender.items[relatedLot = lot.id] classification.scheme
                        val id: String, // CN.tender.items[relatedLot = lot.id] classification.id
                        val description: String, // CN.tender.items[relatedLot = lot.id] classification.description
                        val title: String // "CN.tender.items[relatedLot = lot.id] classification.id - CN.tender.items[relatedLot = lot.id] classification.description"
                )

                data class AdditionalClassification(
                        val scheme: String, // CN.tender.items[relatedLot = lot.id] additionalClassifications[i].scheme
                        val id: String, // CN.tender.items[relatedLot = lot.id] additionalClassifications[i].id
                        val description: String, // CN.tender.items[relatedLot = lot.id] additionalClassifications[i].description
                        val title: String // "CN.tender.items[relatedLot = lot.id] additionalClassifications[i].id - CN.tender.items[relatedLot = lot.id] additionalClassifications[i].description"
                )
            }

            data class Document(
                    val id: String, // CN.tender.documents[relatedLots[0] = lot.id].id
                    val type: String, // CN.tender.documents[relatedLots[0] = lot.id].documentType
                    val title: String?, // CN.tender.documents[relatedLots[0] = lot.id].title
                    val description: String?, // CN.tender.documents[relatedLots[0] = lot.id].description
                    val relatedLots: String // CN.tender.documents[relatedLots[0] = lot.id].relatedLots[0]
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
                val pmd: String // /pmd?lang=langFromRequest&country=(ocds-t1s2t3->MD<-1532010121824-EV-1532010122650)
        )
    }
}
