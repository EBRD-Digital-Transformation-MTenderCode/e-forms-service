package com.procurement.formsservice.model.pn.create

import com.fasterxml.jackson.annotation.JsonProperty

data class PnCreateData(
    @JsonProperty("releases") val releases: List<Release>) {

    data class Release(
        @JsonProperty("parties") val parties: List<Party>,
        @JsonProperty("tender") val tender: Tender,
        @JsonProperty("planning") val planning: Planning) {

        data class Tender(
            @JsonProperty("classification") val classification: Classification) {

            data class Classification(@JsonProperty("id") val id: String)
        }

        data class Planning(
            @JsonProperty("budget") val budget: Budget) {

            data class Budget(
                @JsonProperty("amount") val amount: Amount?) {

                data class Amount(
                    @JsonProperty("currency") val currency: String)
            }
        }

        data class Party(
            @JsonProperty("name") val name: String,
            @JsonProperty("identifier") val identifier: Identifier,
            @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @JsonProperty("address") val address: Address,
            @JsonProperty("contactPoint") val contactPoint: ContactPoint) {

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
                @JsonProperty("url") val url: String
            )

            data class Identifier(
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("legalName") val legalName: String,
                @JsonProperty("uri") val uri: String?
            )
        }
    }
}
