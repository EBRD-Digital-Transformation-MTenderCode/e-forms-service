package com.procurement.formsservice.model.enquiry.create

import com.fasterxml.jackson.annotation.JsonProperty

data class EnquiryCreateData(
    @JsonProperty("uri") val uri: String,
    @JsonProperty("version") val version: String,
    @JsonProperty("extensions") val extensions: List<String>,
    @JsonProperty("publisher") val publisher: Publisher,
    @JsonProperty("license") val license: String,
    @JsonProperty("publicationPolicy") val publicationPolicy: String,
    @JsonProperty("publishedDate") val publishedDate: String,
    @JsonProperty("releases") val releases: List<Release>
) {

    data class Release(
        @JsonProperty("ocid") val ocid: String,
        @JsonProperty("id") val id: String,
        @JsonProperty("date") val date: String,
        @JsonProperty("tag") val tag: List<String>,
        @JsonProperty("initiationType") val initiationType: String,
        @JsonProperty("language") val language: String,
        @JsonProperty("tender") val tender: Tender,
        @JsonProperty("hasPreviousNotice") val hasPreviousNotice: Boolean,
        @JsonProperty("purposeOfNotice") val purposeOfNotice: PurposeOfNotice,
        @JsonProperty("relatedProcesses") val relatedProcesses: List<RelatedProcesse>
    ) {

        data class PurposeOfNotice(
            @JsonProperty("isACallForCompetition") val isACallForCompetition: Boolean
        )

        data class RelatedProcesse(
            @JsonProperty("id") val id: String,
            @JsonProperty("relationship") val relationship: List<String>,
            @JsonProperty("scheme") val scheme: String,
            @JsonProperty("identifier") val identifier: String,
            @JsonProperty("uri") val uri: String
        )

        data class Tender(
            @JsonProperty("id") val id: String,
            @JsonProperty("title") val title: String,
            @JsonProperty("description") val description: String,
            @JsonProperty("status") val status: String,
            @JsonProperty("statusDetails") val statusDetails: String,
            @JsonProperty("items") val items: List<Item>,
            @JsonProperty("lots") val lots: List<Lot>,
            @JsonProperty("lotGroups") val lotGroups: List<LotGroup>,
            @JsonProperty("tenderPeriod") val tenderPeriod: TenderPeriod,
            @JsonProperty("enquiryPeriod") val enquiryPeriod: EnquiryPeriod,
            @JsonProperty("hasEnquiries") val hasEnquiries: Boolean,
            @JsonProperty("awardCriteria") val awardCriteria: String,
            @JsonProperty("submissionMethod") val submissionMethod: List<String>,
            @JsonProperty("submissionMethodDetails") val submissionMethodDetails: String,
            @JsonProperty("submissionMethodRationale") val submissionMethodRationale: List<String>,
            @JsonProperty("requiresElectronicCatalogue") val requiresElectronicCatalogue: Boolean
        ) {

            data class EnquiryPeriod(
                @JsonProperty("startDate") val startDate: String,
                @JsonProperty("endDate") val endDate: String
            )

            data class Item(
                @JsonProperty("id") val id: String,
                @JsonProperty("description") val description: String,
                @JsonProperty("classification") val classification: Classification,
                @JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>,
                @JsonProperty("quantity") val quantity: Int,
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
                @JsonProperty("title") val title: String,
                @JsonProperty("description") val description: String,
                @JsonProperty("status") val status: String,
                @JsonProperty("statusDetails") val statusDetails: String,
                @JsonProperty("value") val value: Value,
                @JsonProperty("options") val options: List<Option>,
                @JsonProperty("recurrentProcurement") val recurrentProcurement: List<RecurrentProcurement>,
                @JsonProperty("variants") val variants: List<Variant>,
                @JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,
                @JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance
            ) {

                data class Option(
                    @JsonProperty("hasOptions") val hasOptions: Boolean
                )

                data class ContractPeriod(
                    @JsonProperty("startDate") val startDate: String,
                    @JsonProperty("endDate") val endDate: String
                )

                data class Variant(
                    @JsonProperty("hasVariants") val hasVariants: Boolean
                )

                data class PlaceOfPerformance(
                    @JsonProperty("address") val address: Address,
                    @JsonProperty("description") val description: String
                ) {

                    data class Address(
                        @JsonProperty("streetAddress") val streetAddress: String,
                        @JsonProperty("postalCode") val postalCode: String,
                        @JsonProperty("addressDetails") val addressDetails: AddressDetails
                    ) {

                        data class AddressDetails(
                            @JsonProperty("country") val country: Country,
                            @JsonProperty("region") val region: Region,
                            @JsonProperty("locality") val locality: Locality
                        ) {

                            data class Country(
                                @JsonProperty("id") val id: String
                            )

                            data class Locality(
                                @JsonProperty("scheme") val scheme: String,
                                @JsonProperty("id") val id: String,
                                @JsonProperty("description") val description: String
                            )

                            data class Region(
                                @JsonProperty("id") val id: String
                            )
                        }
                    }
                }

                data class RecurrentProcurement(
                    @JsonProperty("isRecurrent") val isRecurrent: Boolean
                )

                data class Value(
                    @JsonProperty("amount") val amount: Int,
                    @JsonProperty("currency") val currency: String
                )
            }

            data class LotGroup(
                @JsonProperty("optionToCombine") val optionToCombine: Boolean
            )

            data class TenderPeriod(
                @JsonProperty("startDate") val startDate: String,
                @JsonProperty("endDate") val endDate: String
            )
        }
    }

    data class Publisher(
        @JsonProperty("name") val name: String,
        @JsonProperty("scheme") val scheme: String,
        @JsonProperty("uid") val uid: String,
        @JsonProperty("uri") val uri: String
    )
}
