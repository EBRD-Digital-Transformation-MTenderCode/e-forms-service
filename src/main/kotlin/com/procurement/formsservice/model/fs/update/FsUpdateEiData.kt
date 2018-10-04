package com.procurement.formsservice.model.fs.update

import com.fasterxml.jackson.annotation.JsonProperty

data class FsUpdateEiData(
    @JsonProperty("releases") val releases: List<Release>) {

    data class Release(
        @JsonProperty("ocid") val ocid: String,
        @JsonProperty("tender") val tender: Tender,
        @JsonProperty("planning") val planning: Planning) {

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
            @JsonProperty("budget") val budget: Budget) {

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