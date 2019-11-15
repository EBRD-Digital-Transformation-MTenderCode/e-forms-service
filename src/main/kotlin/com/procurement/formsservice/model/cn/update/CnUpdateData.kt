package com.procurement.formsservice.model.cn.update

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.formsservice.model.cn.Role

//@Target(AnnotationTarget.VALUE_PARAMETER)
//@Retention(AnnotationRetention.SOURCE)
//@MustBeDocumented
//annotation class PS

data class CnUpdateData(
    @JsonProperty("records") val records: List<Record>
) {

    data class Record(
        @JsonProperty("ocid") val ocid: String,
        @JsonProperty("compiledRelease") val compiledRelease: JsonNode
    ) {

        data class MS(
            @JsonProperty("ocid") val ocid: String,
            @JsonProperty("planning") val planning: Planning,
            @JsonProperty("tender") val tender: Tender,
            @JsonProperty("parties") val parties: List<Party>
        ) {

            data class Party(
                @JsonProperty("id") val id: String,
                @JsonProperty("name") val name: String,
                @JsonProperty("identifier") val identifier: Identifier,
                @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
                @JsonProperty("address") val address: Address,
                @JsonProperty("contactPoint") val contactPoint: ContactPoint,
                @JsonProperty("roles") val roles: List<Role>,
                @JsonProperty("persones") val persons: List<Person>?
            ) {

                data class Address(
                    @JsonProperty("streetAddress") val streetAddress: String,
                    @JsonProperty("postalCode") val postalCode: String?,
                    @JsonProperty("addressDetails") val addressDetails: AddressDetails
                ) {

                    data class AddressDetails(
                        @JsonProperty("country") val country: Country,
                        @JsonProperty("region") val region: Region,
                        @JsonProperty("locality") val locality: Locality
                    ) {

                        data class Region(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )

                        data class Locality(
                            @JsonProperty("scheme") val scheme: String,
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )

                        data class Country(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )
                    }
                }

                data class Identifier(
                    @JsonProperty("scheme") val scheme: String,
                    @JsonProperty("id") val id: String,
                    @JsonProperty("legalName") val legalName: String,
                    @JsonProperty("uri") val uri: String?
                )

                data class AdditionalIdentifier(
                    @JsonProperty("scheme") val scheme: String,
                    @JsonProperty("id") val id: String,
                    @JsonProperty("legalName") val legalName: String,
                    @JsonProperty("uri") val uri: String?
                )

                data class ContactPoint(
                    @JsonProperty("name") val name: String,
                    @JsonProperty("email") val email: String,
                    @JsonProperty("telephone") val telephone: String,
                    @JsonProperty("faxNumber") val faxNumber: String?,
                    @JsonProperty("url") val url: String?
                )

                data class Person(
                    @JsonProperty("title") val title: String,
                    @JsonProperty("name") val name: String,
                    @JsonProperty("identifier") val identifier: Identifier,
                    @JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
                ) {

                    data class Identifier(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("uri") val uri: String?
                    )

                    data class BusinessFunction(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("type") val type: String,
                        @JsonProperty("jobTitle") val jobTitle: String,
                        @JsonProperty("period") val period: Period,
                        @JsonProperty("documents") val documents: List<Document>?
                    ) {
                        data class Period(
                            @JsonProperty("startDate") val startDate: String
                        )

                        data class Document(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("documentType") val documentType: String,
                            @JsonProperty("title") val title: String,
                            @JsonProperty("description") val description: String?
                        )
                    }
                }
            }

            data class Tender(
                @JsonProperty("title") val title: String,
                @JsonProperty("description") val description: String,
                @JsonProperty("classification") val classification: Classification,
                @JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,
                @JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
                @JsonProperty("legalBasis") val legalBasis: String,
                @JsonProperty("value") val value: Value
            ) {

                data class Classification(
                    @JsonProperty("id") val id: String
                )

                data class Value(
                    @JsonProperty("currency") val currency: String
                )
            }

            data class Planning(
                @JsonProperty("budget") val budget: Budget
            ) {

                data class Budget(
                    @JsonProperty("budgetBreakdown") val budgetBreakdown: List<BudgetBreakdown>
                ) {

                    data class BudgetBreakdown(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("amount") val amount: Amount
                    ) {

                        data class Amount(
                            @JsonProperty("amount") val amount: Double,
                            @JsonProperty("currency") val currency: String
                        )
                    }
                }
            }
        }

        data class CN(
            @JsonProperty("ocid") val ocid: String,
            @JsonProperty("tender") val tender: Tender
        ) {

            data class Tender(
                @JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod,
                @JsonProperty("documents") val documents: List<Document>?,
                @JsonProperty("enquiryPeriod") val enquiryPeriod: EnquiryPeriod?,
                @JsonProperty("items") val items: List<Item>?,
                @JsonProperty("lots") val lots: List<Lot>?,
                @JsonProperty("procurementMethodModalities") val procurementMethodModalities: List<String>?,
                @JsonProperty("awardCriteria") val awardCriteria: String?,
                @JsonProperty("awardCriteriaDetails") val awardCriteriaDetails: String?,
                @JsonProperty("electronicAuctions") val electronicAuctions: ElectronicAuctions?,
                @field:JsonProperty("criteria") @param:JsonProperty("criteria") val criteria: List<Criteria>?,
                @field:JsonProperty("conversions") @param:JsonProperty("conversions") val conversions: List<Conversion>?
            ) {

                data class EnquiryPeriod(
                    @JsonProperty("endDate") val endDate: String?
                )

                data class TenderPeriod(
                    @JsonProperty("endDate") val endDate: String?
                )

                data class Document(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("documentType") val documentType: String,
                    @JsonProperty("title") val title: String?,
                    @JsonProperty("description") val description: String?,
                    @JsonProperty("relatedLots") val relatedLots: List<String>?
                )

                data class Item(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("internalId") val internalId: String?,
                    @JsonProperty("description") val description: String?,
                    @JsonProperty("classification") val classification: Classification,
                    @JsonProperty("additionalClassification") val additionalClassifications: List<AdditionalClassification>?,
                    @JsonProperty("quantity") val quantity: Double,
                    @JsonProperty("unit") val unit: Unit,
                    @JsonProperty("relatedLot") val relatedLot: String
                ) {

                    data class Classification(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class AdditionalClassification(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class Unit(
                        @JsonProperty("name") val name: String,
                        @JsonProperty("id") val id: String
                    )
                }

                data class Lot(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("internalId") val internalId: String?,
                    @JsonProperty("title") val title: String,
                    @JsonProperty("description") val description: String,
                    @JsonProperty("value") val value: Value,
                    @JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,
                    @JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance?
                ) {

                    data class Value(
                        @JsonProperty("amount") val amount: Double,
                        @JsonProperty("currency") val currency: String
                    )

                    data class ContractPeriod(
                        @JsonProperty("startDate") val startDate: String,
                        @JsonProperty("endDate") val endDate: String
                    )

                    data class PlaceOfPerformance(
                        @JsonProperty("address") val address: Address?,
                        @JsonProperty("description") val description: String?
                    ) {

                        data class Address(
                            @JsonProperty("streetAddress") val streetAddress: String,
                            @JsonProperty("postalCode") val postalCode: String?,
                            @JsonProperty("addressDetails") val addressDetails: AddressDetails
                        ) {

                            data class AddressDetails(
                                @JsonProperty("country") val country: Country,
                                @JsonProperty("region") val region: Region,
                                @JsonProperty("locality") val locality: Locality
                            ) {

                                data class Region(
                                    @JsonProperty("id") val id: String,
                                    @JsonProperty("description") val description: String
                                )

                                data class Locality(
                                    @JsonProperty("scheme") val scheme: String,
                                    @JsonProperty("id") val id: String,
                                    @JsonProperty("description") val description: String
                                )

                                data class Country(
                                    @JsonProperty("id") val id: String,
                                    @JsonProperty("description") val description: String
                                )
                            }
                        }
                    }
                }

                data class ElectronicAuctions(
                    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<Detail>
                ) {
                    data class Detail(
                        @param:JsonProperty("id") val id: String,
                        @param:JsonProperty("relatedLot") val relatedLot: String,
                        @param:JsonProperty("electronicAuctionModalities") val electronicAuctionModalities: List<ElectronicAuctionModality>
                    ) {
                        data class ElectronicAuctionModality(
                            @param:JsonProperty("eligibleMinimumDifference") val eligibleMinimumDifference: EligibleMinimumDifference
                        ) {
                            data class EligibleMinimumDifference(
                                @param:JsonProperty("amount") val amount: Double,
                                @param:JsonProperty("currency") val currency: String
                            )
                        }
                    }
                }

                data class Criteria(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                    @field:JsonProperty("source") @param:JsonProperty("source") val source: String?,
                    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String?,
                    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String?,
                    @field:JsonProperty("requirementGroups") @param:JsonProperty("requirementGroups") val requirementGroups: List<RequirementGroup>
                ) {

                    data class RequirementGroup(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                        @field:JsonProperty("requirements") @param:JsonProperty("requirements") val requirements: List<Requirement>
                    ) {

                        data class Requirement(
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                            @field:JsonProperty("period") @param:JsonProperty("period") val period: Period?,
                            @field:JsonProperty("dataType") @param:JsonProperty("dataType") val dataType: String,
                            @field:JsonProperty("expectedValue") @param:JsonProperty("expectedValue") val expectedValue: String?,
                            @field:JsonProperty("minValue") @param:JsonProperty("minValue") val minValue: String?,
                            @field:JsonProperty("maxValue") @param:JsonProperty("maxValue") val maxValue: String?
                        ) {

                            data class Period(
                                @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String,
                                @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: String
                            )
                        }
                    }
                }

                data class Conversion(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String,
                    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String,
                    @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String,
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                    @field:JsonProperty("coefficients") @param:JsonProperty("coefficients") val coefficients: List<Coefficient>
                ) {
                    data class Coefficient(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("value") @param:JsonProperty("value") val value: String,
                        @field:JsonProperty("coefficient") @param:JsonProperty("coefficient") val coefficient: String
                    )
                }
            }

            data class Planning(
                @JsonProperty("budget") val budget: Budget
            ) {

                data class Budget(
                    @JsonProperty("budgetBreakdown") val budgetBreakdown: List<BudgetBreakdown>
                ) {

                    data class BudgetBreakdown(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("amount") val amount: Amount
                    ) {

                        data class Amount(
                            @JsonProperty("amount") val amount: Int,
                            @JsonProperty("currency") val currency: String
                        )
                    }
                }
            }
        }
    }
}
