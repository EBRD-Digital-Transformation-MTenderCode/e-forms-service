package com.procurement.formsservice.model.pn.update

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.formsservice.model.pn.Role

data class PnUpdateData(
    @JsonProperty("records") val records: List<Record>) {

    data class Record(
        @JsonProperty("ocid") val ocid: String,
        @JsonProperty("compiledRelease") val compiledRelease: JsonNode) {

        data class MS(
            @JsonProperty("ocid") val ocid: String,
            @JsonProperty("planning") val planning: Planning,
            @JsonProperty("tender") val tender: Tender,
            @JsonProperty("parties") val parties: List<Party>) {

            data class Party(
                @JsonProperty("name") val name: String,
                @JsonProperty("identifier") val identifier: Identifier,
                @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
                @JsonProperty("address") val address: Address,
                @JsonProperty("contactPoint") val contactPoint: ContactPoint,
                @JsonProperty("roles") val roles: List<Role>) {

                data class Address(
                    @JsonProperty("streetAddress") val streetAddress: String,
                    @JsonProperty("postalCode") val postalCode: String?,
                    @JsonProperty("addressDetails") val addressDetails: AddressDetails) {

                    data class AddressDetails(
                        @JsonProperty("country") val country: Country,
                        @JsonProperty("region") val region: Region,
                        @JsonProperty("locality") val locality: Locality) {

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
            }

            data class Tender(
                @JsonProperty("title") val title: String,
                @JsonProperty("description") val description: String,
                @JsonProperty("classification") val classification: Classification,
                @JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,
                @JsonProperty("procurementMethodAdditionalInfo") val procurementMethodAdditionalInfo: String?,
                @JsonProperty("legalBasis") val legalBasis: String,
                @JsonProperty("value") val value: Value) {

                data class Classification(
                    @JsonProperty("id") val id: String
                )

                data class Value(
                    @JsonProperty("currency") val currency: String
                )
            }

            data class Planning(
                @JsonProperty("budget") val budget: Budget) {

                data class Budget(
                    @JsonProperty("budgetBreakdown") val budgetBreakdown: List<BudgetBreakdown>) {

                    data class BudgetBreakdown(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("amount") val amount: Amount) {

                        data class Amount(
                            @JsonProperty("amount") val amount: Double,
                            @JsonProperty("currency") val currency: String
                        )
                    }
                }
            }
        }

        data class PN(
            @JsonProperty("ocid") val ocid: String,
            @JsonProperty("tender") val tender: Tender) {

            data class Tender(
                @JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod,
                @JsonProperty("documents") val documents: List<Document>?,
                @JsonProperty("items") val items: List<Item>?,
                @JsonProperty("lots") val lots: List<Lot>?) {

                data class TenderPeriod(
                    @JsonProperty("startDate") val startDate: String
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
                    @JsonProperty("relatedLot") val relatedLot: String) {

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
                    @JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance?) {

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
                        @JsonProperty("description") val description: String?) {

                        data class Address(
                            @JsonProperty("streetAddress") val streetAddress: String,
                            @JsonProperty("postalCode") val postalCode: String?,
                            @JsonProperty("addressDetails") val addressDetails: AddressDetails) {

                            data class AddressDetails(
                                @JsonProperty("country") val country: Country,
                                @JsonProperty("region") val region: Region,
                                @JsonProperty("locality") val locality: Locality) {

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
            }

            data class Planning(
                @JsonProperty("budget") val budget: Budget) {

                data class Budget(
                    @JsonProperty("budgetBreakdown") val budgetBreakdown: List<BudgetBreakdown>) {

                    data class BudgetBreakdown(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("amount") val amount: Amount) {

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