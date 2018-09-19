package com.procurement.formsservice.model.cancellation.tender

import com.fasterxml.jackson.annotation.JsonProperty

class CancellationTenderData(@JsonProperty("records") val records: List<Record>) {

    data class Record(
        @JsonProperty("ocid") val ocid: String,
        @JsonProperty("compiledRelease") val compiledRelease: CompiledRelease) {

        data class CompiledRelease(@JsonProperty("tender") val tender: Tender) {

            data class Tender(
                @JsonProperty("title") val title: String,
                @JsonProperty("description") val description: String
            )
        }
    }
}
