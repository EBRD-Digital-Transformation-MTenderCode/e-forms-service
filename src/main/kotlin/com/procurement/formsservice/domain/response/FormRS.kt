package com.procurement.formsservice.domain.response

import com.fasterxml.jackson.annotation.JsonProperty

class FormRS(
    @field:JsonProperty("schema")
    @param:JsonProperty("schema")
    val schema: Map<String, Any>,

    @field:JsonProperty("data")
    @param:JsonProperty("data")
    val data: Map<String, Any>) : BaseRS()