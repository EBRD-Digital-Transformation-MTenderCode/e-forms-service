package com.procurement.formsservice

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

class JsonMapper(val mapper: ObjectMapper = ObjectMapper()) {
    inline fun <reified T> toJson(obj: T): String = try {
        mapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }

    inline fun <reified T> toObject(json: String): T = try {
        mapper.readValue(json, T::class.java)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }

    inline fun <reified T> toObject(json: JsonNode): T = try {
        mapper.treeToValue(json, T::class.java)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}
