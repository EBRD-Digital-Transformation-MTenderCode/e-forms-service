package com.procurement.formsservice.model.pn

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = Role.RoleDeserializer::class)
enum class Role(val jsonName: String) {
    PAYER("payer"),
    FUNDER("funder"),
    PROCURING_ENTITY("procuringEntity"),
    BUYER("buyer");

    companion object {
        private val map: Map<String, Role> = mutableMapOf<String, Role>().apply {
            enumValues<Role>().forEach {
                this[it.jsonName.toUpperCase()] = it
            }
        }
    }

    class RoleDeserializer : StdDeserializer<Role>(Role::class.java) {
        override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Role {
            val name = parser.readValueAs(String::class.java)
            return map[name.toUpperCase()] ?: throw IllegalArgumentException("Invalid name of the role [$name].")
        }
    }
}