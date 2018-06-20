package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.StringSourceDefinition

fun string(builder: StringAttributeDefinition.Builder.() -> Unit) =
    StringAttributeDefinition.Builder().apply(builder)

class StringAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: StringSourceDefinition?,
    val minLength: Int,
    val maxLength: Int,
    val format: Format
) : AttributeDefinition(
    name = name,
    title = title,
    description = description,
    destination = destination,
    required = required,
    usage = usage
) {
    override fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "string"
        if (minLength > -1) writer["minLength"] = minLength
        if (maxLength > -1) writer["maxLength"] = maxLength
        if (format != Format.NONE) writer["format"] = format.toString()
    }

    override fun buildSource(context: Context, writer: MutableMap<String, Any>) {
        source?.buildForm(context, writer)
    }

    override fun buildData(context: Context): Any? = source?.buildData(context)

    enum class Format(val description: String) {
        NONE(""),
        DATE_TIME("date-time"),
        EMAIL("email"),
        HOSTNAME("hostname"),
        IPV4("ipv4"),
        IPV6("ipv6"),
        URI("uri");

        override fun toString(): String {
            return description
        }
    }

    class Builder {
        var title: String = ""
        var description: String = ""
        var destination: String = ""
        var required = FALSE
        var usage = TRUE
        var source: StringSourceDefinition? = null
        var minLength: Int = -1
        var maxLength: Int = -1
        var format: StringAttributeDefinition.Format =
            StringAttributeDefinition.Format.NONE

        fun build(name: String) =
            StringAttributeDefinition(
                name = name,
                title = title,
                description = description,
                destination = destination,
                required = required,
                usage = usage,
                source = source,
                minLength = minLength,
                maxLength = maxLength,
                format = format
            )
    }
}

