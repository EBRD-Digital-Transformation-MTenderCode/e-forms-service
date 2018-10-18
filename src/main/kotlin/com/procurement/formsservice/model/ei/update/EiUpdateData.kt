package com.procurement.formsservice.model.ei.update

import com.fasterxml.jackson.annotation.JsonProperty

data class EiUpdateData(
    @JsonProperty("releases") val releases: List<Release>) {

    data class Release(
        @JsonProperty("tender") val tender: Tender,
        @JsonProperty("parties") val parties: List<Party>,
        @JsonProperty("planning") val planning: Planning) {

        data class Party(
            @JsonProperty("name") val name: String,
            @JsonProperty("identifier") val identifier: Identifier,
            @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @JsonProperty("address") val address: Address,
            @JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @JsonProperty("details") val details: Details?) {

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
                        @JsonProperty("description") val description: String)

                    data class Locality(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String)

                    data class Country(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String)
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

            data class Details(
                @JsonProperty("typeOfBuyer") val typeOfBuyer: String?,
                @JsonProperty("mainGeneralActivity") val mainGeneralActivity: String?,
                @JsonProperty("mainSectoralActivity") val mainSectoralActivity: String?
            )
        }

        data class Tender(
            @JsonProperty("title") val title: String,
            @JsonProperty("description") val description: String?,
            @JsonProperty("classification") val classification: Classification) {

            data class Classification(
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("description") val description: String
            )
        }

        data class Planning(
            @JsonProperty("budget") val budget: Budget,
            @JsonProperty("rationale") val rationale: String?) {

            data class Budget(
                @JsonProperty("period") val period: Period) {

                data class Period(
                    @JsonProperty("startDate") val startDate: String,
                    @JsonProperty("endDate") val endDate: String
                )
            }
        }
    }
}
