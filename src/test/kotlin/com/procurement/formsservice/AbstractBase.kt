package com.procurement.formsservice

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

abstract class AbstractBase {
    companion object {
        val objectMapper = ObjectMapper().apply {
            this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
        val jsonJacksonMapper = JsonMapper(objectMapper)
        val RESOURCES = JsonResource()
    }
}
