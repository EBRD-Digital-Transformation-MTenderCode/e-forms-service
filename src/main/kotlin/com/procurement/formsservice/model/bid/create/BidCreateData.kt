package com.procurement.formsservice.model.bid.create

import com.fasterxml.jackson.annotation.JsonProperty

data class BidCreateData(
    @JsonProperty("releases") val releases: List<Release>) {

    data class Release(
        @JsonProperty("tender") val tender: Tender) {

        data class Tender(
            @JsonProperty("lots") val lots: List<Lot>) {

            data class Lot(
                @JsonProperty("id") val id: String,
                @JsonProperty("value") val value: Value) {

                data class Value(
                    @JsonProperty("amount") val amount: Double,
                    @JsonProperty("currency") val currency: String
                )
            }
        }
    }
}
