package com.procurement.formsservice.model.ac.create

import com.fasterxml.jackson.annotation.JsonProperty

data class AwardContractCreateData(
    @JsonProperty("releases") val releases: List<Release>
) {

    data class Release(
        @JsonProperty("contracts") val contracts: List<Contract>
    ) {

        data class Contract(
            @JsonProperty("id") val id: String,
            @JsonProperty("statusDetails") val statusDetails: String
        )
    }
}