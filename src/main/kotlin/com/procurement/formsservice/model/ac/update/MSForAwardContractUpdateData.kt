package com.procurement.formsservice.model.ac.update

import com.fasterxml.jackson.annotation.JsonProperty

data class MSForAwardContractUpdateData(
    @JsonProperty("releases") val releases: List<Release>
) {
    data class Release(
        @JsonProperty("parties") val parties: List<Party>
    ) {
        data class Party(
            @JsonProperty("id") val id: String,
            @JsonProperty("name") val name: String,
            @JsonProperty("identifier") val identifier: Identifier,
            @JsonProperty("address") val address: Address,
            @JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @JsonProperty("roles") val roles: List<String>,
            @JsonProperty("details") val details: Details?
        ) {
            data class Identifier(
                @JsonProperty("id") val id: String,
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("legalName") val legalName: String,
                @JsonProperty("uri") val uri: String?
            )

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
                    data class Country(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class Region(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class Locality(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )
                }
            }

            data class ContactPoint(
                @JsonProperty("name") val name: String,
                @JsonProperty("email") val email: String,
                @JsonProperty("telephone") val telephone: String,
                @JsonProperty("faxNumber") val faxNumber: String?,
                @JsonProperty("url") val url: String?
            )

            data class AdditionalIdentifier(
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("legalName") val legalName: String,
                @JsonProperty("uri") val uri: String?
            )

            data class Details(
                @JsonProperty("typeOfBuyer") val typeOfBuyer: String?,
                @JsonProperty("mainGeneralActivity") val mainGeneralActivity: String?,
                @JsonProperty("mainSectoralActivity") val mainSectoralActivity: String?
            )
        }
    }
}