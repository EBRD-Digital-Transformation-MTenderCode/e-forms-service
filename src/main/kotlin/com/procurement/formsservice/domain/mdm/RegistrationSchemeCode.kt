package com.procurement.formsservice.domain.mdm

import com.fasterxml.jackson.annotation.JsonProperty

data class RegistrationSchemeCode(@JsonProperty("data") val data: Data) {
    data class Data(@JsonProperty("items") val items: List<Item>) {
        data class Item(@JsonProperty("code") val code: String)
    }
}

