package com.procurement.formsservice.model.fs.update

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.formsservice.model.fs.Role

class FsUpdateFsData(
    @JsonProperty("releases") val releases: List<Release>) {

    data class Release(
        @JsonProperty("parties") val parties: List<Party>,
        @JsonProperty("planning") val planning: Planning) {

        data class Planning(
            @JsonProperty("budget") val budget: Budget,
            @JsonProperty("rationale") val rationale: String?) {

            data class Budget(
                @JsonProperty("id") val id: String?,
                @JsonProperty("description") val description: String?,
                @JsonProperty("period") val period: Period,
                @JsonProperty("amount") val amount: Amount,
                @JsonProperty("project") val project: String?,
                @JsonProperty("projectID") val projectID: String?,
                @JsonProperty("uri") val uri: String?,
                @JsonProperty("isEuropeanUnionFunded") val isEuropeanUnionFunded: Boolean,
                @JsonProperty("europeanUnionFunding") val europeanUnionFunding: EuropeanUnionFunding?) {

                data class EuropeanUnionFunding(
                    @JsonProperty("projectIdentifier") val projectIdentifier: String?,
                    @JsonProperty("projectName") val projectName: String?,
                    @JsonProperty("uri") val uri: String?
                )

                data class Period(
                    @JsonProperty("startDate") val startDate: String,
                    @JsonProperty("endDate") val endDate: String
                )

                data class Amount(
                    @JsonProperty("amount") val amount: Double,
                    @JsonProperty("currency") val currency: String
                )
            }
        }

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
                @JsonProperty("url") val url: String?,
                @JsonProperty("telephone") val telephone: String,
                @JsonProperty("email") val email: String,
                @JsonProperty("faxNumber") val faxNumber: String?
            )
        }
    }
}