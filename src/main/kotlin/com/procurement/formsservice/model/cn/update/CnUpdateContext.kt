package com.procurement.formsservice.model.cn.update

class CnUpdateContext(
    val parameters: Parameters,
    val procuringEntity: ProcuringEntity,
    val tender: Tender,
    val parentEntity: String // ocds-t1s2t3-MD-1532010121824->PN | EV | NP<-1532010122650
) {
    data class Parameters(
        val ocid: String
    )

    data class ProcuringEntity(
        val id: String, // MS.parties[role="procuringEntity"].id
        val name: String, // MS.parties[role="procuringEntity"].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val contactPoint: ContactPoint,
        val persones: List<Persone>?,
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

        data class Persone(
            val title: String, // MS.parties[role="procuringEntity"].persones[i].title
            val name: String, // MS.parties[role="procuringEntity"].persones[i].name
            val identifier: Identifier,
            val businessFunctions: List<BusinessFunction>
        ) {
            data class Identifier(
                val scheme: String, // MS.parties[role="procuringEntity"].persones[i].identifier.scheme
                val id: String, // MS.parties[role="procuringEntity"].persones[i].identifier.id
                val uri: String? // MS.parties[role="procuringEntity"].persones[i].identifier.uri
            )

            data class BusinessFunction(
                val id: String, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].id
                val type: String, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].type
                val jobTitle: String, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].jobTitle
                val period: Period,
                val documents: List<Document>?
            ) {
                data class Period(
                    val startDate: String // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].period.startDate
                )

                data class Document(
                    val id: String, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].documents[i].id
                    val type: String, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].documents[i].type
                    val title: String?, // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].documents[i].title
                    val description: String? // MS.parties[role="procuringEntity"].persones[i].businessFunctions[i].documents[i].description
                )
            }
        }
    }

    data class Tender(
        val title: String, // MS.tender.title
        val description: String, // MS.tender.description
        val documents: List<Document>?, // CN.tender.documents
        val lots: List<Lot>?, // CN.tender.lots
        val tendererCriteria: List<TendererCriterion>?,
        val tenderCriteria: List<TenderCriterion>?,
        val procurementMethodModalities: String?, // CN.tender.procurementMethodModalities[0]
        val electronicAuctions: List<ElectronicAuction>?, // CN.tender.electronicAuctions?.details
        val procurementMethodDetails: String, // MS.tender.procurementMethodDetails
        val awardCriteria: String?, // CN.tender.awardCriteria
        val awardCriteriaDetails: String?, // CN.tender.awardCriteriaDetails
        val legalBasis: String,  // MS.tender.legalBasis
        val enquiryPeriod: String?, // CN.tender.enquiryPeriod?.endDate
        val tenderPeriod: String?, // CN.tender.tenderPeriod.endDate
        val budgetBreakdown: List<BudgetBreakdown>,
        val pmd: String, // MS.tender.procurementMethodDetails
        val mainProcurementCategory: String, // MS.tender.mainProcurementCategory
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
            val internalId: String?, // CN.tender.lots[i].internalId
            val title: String, // CN.tender.lots[i].title
            val description: String, // CN.tender.lots[i].description
            val value: Value,
            val performance: Performance,
            val lotCriteria: List<LotCriterion>?,
            val items: List<Item>?, // CN.tender.items
            val documents: List<Document>? // CN.tender.documents
        ) {
            data class Value(
                val amount: Double, // CN.tender.lots[i].value.amount
                val currency: String // CN.tender.lots[i].value.currency
            )

            data class Performance(
                val placeOfPerformance: PlaceOfPerformance?,  // CN.tender.lots[i].placeOfPerformance
                val deliveryPeriod: DeliveryPeriod
            ) {
                data class PlaceOfPerformance(
                    val address: Address?, // CN.tender.lots[i].placeOfPerformance.address
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

            data class LotCriterion(
                val id: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].id
                val title: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].title
                val description: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].description
                val relatesTo: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].relatesTo
                val relatedItem: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].relatedItem
                val requirementGroups: List<RequirementGroup>
            ) {
                data class RequirementGroup(
                    val id: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].id
                    val description: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].description
                    val requirements: List<Requirement>
                ) {
                    data class Requirement(
                        val id: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].id
                        val title: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].title
                        val description: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].description
                        val period: Period?,
                        val dataType: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].dataType
                        val expectedValue: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].expectedValue
                        val minValue: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].minValue
                        val maxValue: String?, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].maxValue
                        val conversions: List<Conversion>?
                    ) {
                        data class Period(
                            val startDate: String, // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].period.startDate
                            val endDate: String // CN.tender.criteria[relatesTo = 'lot' && relatedItem = 'lot.id'].requirementGroups[i].requirements[i].period.endDate
                        )

                        data class Conversion(
                            val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].id
                            val relatesTo: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatesTo
                            val relatedItem: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatedItem
                            val description: String?, // CN.tender.conversions[relatedItem = 'requirement.id'].description
                            val rationale: String, // CN.tender.conversions[relatedItem = 'requirement.id'].rationale
                            val coefficients: List<Coefficient>
                        ) {
                            data class Coefficient(
                                val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].id
                                val value: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].value
                                val coefficient: Double // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].coefficient
                            )
                        }
                    }
                }
            }

            data class Item(
                val id: String, // CN.tender.items[relatedLot = lot.id].id
                val relatedLot: String, // CN.tender.items[relatedLot = lot.id].relatedLot
                val internalId: String?, // CN.tender.items[relatedLot = lot.id].internalId
                val description: String?, // CN.tender.items[relatedLot = lot.id] description
                val quantity: Quantity,
                val classification: Classification,
                val additionalClassifications: List<AdditionalClassification>?,
                val itemCriteria: List<ItemCriterion>?
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

                data class ItemCriterion(
                    val id: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].id
                    val title: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].title
                    val description: String?, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].description
                    val relatesTo: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].relatesTo
                    val relatedItem: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].relatedItem
                    val requirementGroups: List<RequirementGroup>
                ) {
                    data class RequirementGroup(
                        val id: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].id
                        val description: String?, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].description
                        val requirements: List<Requirement>
                    ) {
                        data class Requirement(
                            val id: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].id
                            val title: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].title
                            val description: String?, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].description
                            val period: Period?,
                            val dataType: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].dataType
                            val expectedValue: String?/* | Double | Integer | Boolean*/, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].expectedValue
                            val minValue: String?, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].minValue
                            val maxValue: String?/* | Integer*/, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].maxValue
                            val conversions: List<Conversion>?
                        ) {
                            data class Period(
                                val startDate: String, // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].period.startDate
                                val endDate: String // CN.tender.criteria[relatesTo = 'item' && relatedItem = 'item.id'].requirementGroups[i].requirements[i].period.endDate
                            )

                            data class Conversion(
                                val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].id
                                val relatesTo: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatesTo
                                val relatedItem: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatedItem
                                val description: String?, // CN.tender.conversions[relatedItem = 'requirement.id'].description
                                val rationale: String, // CN.tender.conversions[relatedItem = 'requirement.id'].rationale
                                val coefficients: List<Coefficient>
                            ) {
                                data class Coefficient(
                                    val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].id
                                    val value: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].value
                                    val coefficient: Double // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].coefficient
                                )
                            }
                        }
                    }
                }
            }

            data class Document(
                val id: String, // CN.tender.documents[relatedLots[0] = lot.id].id
                val type: String, // CN.tender.documents[relatedLots[0] = lot.id].documentType
                val title: String?, // CN.tender.documents[relatedLots[0] = lot.id].title
                val description: String?, // CN.tender.documents[relatedLots[0] = lot.id].description
                val relatedLots: String // CN.tender.documents[relatedLots[0] = lot.id].relatedLots[0]
            )
        }

        data class TendererCriterion(
            val id: String, // CN.tender.criteria[relatesTo = 'tenderer'].id
            val title: String, // CN.tender.criteria[relatesTo = 'tenderer'].title
            val description: String?, // CN.tender.criteria[relatesTo = 'tenderer'].description
            val relatesTo: String, // CN.tender.criteria[relatesTo = 'tenderer'].relatesTo
            val requirementGroups: List<RequirementGroup>
        ) {
            data class RequirementGroup(
                val id: String, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].id
                val description: String?, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].description
                val requirements: List<Requirement>
            ) {
                data class Requirement(
                    val id: String, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].id
                    val title: String, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].title
                    val description: String?, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].description
                    val period: Period?,
                    val dataType: String, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].dataType
                    val expectedValue: String?/* | Double | Integer | Boolean*/, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].expectedValue
                    val minValue: String?, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].minValue
                    val maxValue: String?, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].maxValue
                    val conversions: List<Conversion>?
                ) {
                    data class Period(
                        val startDate: String, // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].period.startDate
                        val endDate: String // CN.tender.criteria[relatesTo = 'tenderer'].requirementGroups[i].requirements[i].period.endDate
                    )

                    data class Conversion(
                        val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].id
                        val relatesTo: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatesTo
                        val relatedItem: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatedItem
                        val description: String?, // CN.tender.conversions[relatedItem = 'requirement.id'].description
                        val rationale: String, // CN.tender.conversions[relatedItem = 'requirement.id'].rationale
                        val coefficients: List<Coefficient>
                    ) {
                        data class Coefficient(
                            val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].id
                            val value: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].value
                            val coefficient: Double // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].coefficient
                        )
                    }
                }
            }
        }

        data class TenderCriterion(
            val id: String, // CN.tender.criteria[hasn't field relatesTo].id
            val title: String, // CN.tender.criteria[hasn't field relatesTo].title
            val description: String?, // CN.tender.criteria[hasn't field relatesTo].description
            val requirementGroups: List<RequirementGroup>
        ) {
            data class RequirementGroup(
                val id: String, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].id
                val description: String?, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].description
                val requirements: List<Requirement>
            ) {
                data class Requirement(
                    val id: String, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].id
                    val title: String, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].title
                    val description: String?, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].description
                    val period: Period?,
                    val dataType: String, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].dataType
                    val expectedValue: String?/* | Double | Integer | Boolean*/, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].expectedValue
                    val minValue: String?, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].minValue
                    val maxValue: String?, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].maxValue
                    val conversions: List<Conversion>?
                ) {
                    data class Period(
                        val startDate: String, // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].period.startDate
                        val endDate: String // CN.tender.criteria[hasn't field relatesTo].requirementGroups[i].requirements[i].period.endDate
                    )

                    data class Conversion(
                        val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].id
                        val relatesTo: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatesTo
                        val relatedItem: String, // CN.tender.conversions[relatedItem = 'requirement.id'].relatedItem
                        val description: String?, // CN.tender.conversions[relatedItem = 'requirement.id'].description
                        val rationale: String, // CN.tender.conversions[relatedItem = 'requirement.id'].rationale
                        val coefficients: List<Coefficient>
                    ) {
                        data class Coefficient(
                            val id: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].id
                            val value: String, // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].value
                            val coefficient: Double // CN.tender.conversions[relatedItem = 'requirement.id'].coefficients[i].coefficient
                        )
                    }
                }
            }
        }

        data class ElectronicAuction(
            val id: String, // CN.tender.electronicAuctions?.details[i].id
            val relatedLot: String, // CN.tender.electronicAuctions?.details[i].relatedLot
            val value: Value
        ) {
            data class Value(
                val amount: Double, // CN.tender.electronicAuctions?.details[i].electronicAuctionModalities[0].eligibleMinimumDifference.amount
                val currency: String // CN.tender.electronicAuctions?.details[i].electronicAuctionModalities[0].eligibleMinimumDifference.currency
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
