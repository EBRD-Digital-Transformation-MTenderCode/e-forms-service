package com.procurement.formsservice.model.bid.update

import com.fasterxml.jackson.annotation.JsonProperty

class BidUpdateData(
    @JsonProperty("records") val records: List<Record>) {

    data class Record(
        @JsonProperty("compiledRelease") val compiledRelease: CompiledRelease) {

        data class CompiledRelease(
            @JsonProperty("tender") val tender: Tender) {

            data class Tender(
                @JsonProperty("lots") val lots: List<Lot>?) {

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
}
