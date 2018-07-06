package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.data.source.empty.EmptyStringSourceDefinition
import com.procurement.formsservice.json.data.source.enums.StringEnumSourceDefinition
import com.procurement.formsservice.json.data.source.manual.StringManualSourceDefinition
import com.procurement.formsservice.json.data.source.ocds.StringOCDSSourceDefinition
import com.procurement.formsservice.json.exception.DataSpecificException
import com.procurement.formsservice.json.exception.PathParseException
import com.procurement.formsservice.json.path.PathDestinationElementType
import com.procurement.formsservice.json.path.StringPathDestination

fun string(builder: StringAttributeDefinition.Builder.() -> Unit) =
    StringAttributeDefinition.Builder().apply(builder)

class StringAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: StringSourceDefinition,
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
    override suspend fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "string"
        if (minLength > -1) writer["minLength"] = minLength
        if (maxLength > -1) writer["maxLength"] = maxLength
        if (format != Format.NONE) writer["format"] = format.toString()
    }

    override suspend fun buildSource(context: Context, writer: MutableMap<String, Any>) =
        source.buildForm(context, writer)

    override suspend fun buildData(context: Context): Any? =
        when (source) {
            is StringOCDSSourceDefinition -> {
                val value = source.buildData(context)
                if (required.invoke(context) && source.isReadOnly(context) && value == null)
                    throw DataSpecificException("Data of OCDS for required attribute '$name' not specified.")
                value
            }
            else -> source.buildData(context)
        }

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

    class Builder : ElementDefinition.Builder<StringAttributeDefinition> {
        var title: String = ""
        var description: String = ""
        var destination: (name: String) -> StringPathDestination = emptyPathDestination()
        var required = FALSE
        var usage = TRUE
        var source: StringSourceDefinition.Builder<*> = empty { }
        var minLength: Int = -1
        var maxLength: Int = -1
        var format: StringAttributeDefinition.Format =
            StringAttributeDefinition.Format.NONE

        override fun build(name: String) =
            StringAttributeDefinition(
                name = name,
                title = title,
                description = description,
                destination = destinationPath(name, destination),
                required = required,
                usage = usage,
                source = source.build(name),
                minLength = minLength,
                maxLength = maxLength,
                format = format
            )

        //******************************************************************************
        // Path of destination
        //******************************************************************************
        fun destination(path: String): (name: String) -> StringPathDestination = { StringPathDestination(path) }

        private fun emptyPathDestination(): (name: String) -> StringPathDestination = { StringPathDestination("") }
        //******************************************************************************

        //******************************************************************************
        // Sources
        //******************************************************************************
        fun manual(builder: StringManualSourceDefinition.Builder.() -> Unit) =
            StringManualSourceDefinition.Builder().apply(builder)

        fun ocds(builder: StringOCDSSourceDefinition.Builder.() -> Unit) =
            StringOCDSSourceDefinition.Builder().apply(builder)

        fun enum(builder: StringEnumSourceDefinition.Builder.() -> Unit) =
            StringEnumSourceDefinition.Builder().apply(builder)

        fun empty(builder: EmptyStringSourceDefinition.Builder.() -> Unit) =
            EmptyStringSourceDefinition.Builder().apply(builder)
        //******************************************************************************

        private fun destinationPath(name: String, destinationBuilder: (name: String) -> StringPathDestination): String {
            val destination = destinationBuilder(name)
            if (destination.isNotEmpty()) {
                val actualAttributeType = destination.last().type
                val expectedAttributeType = PathDestinationElementType.STRING

                if (expectedAttributeType != actualAttributeType)
                    throw PathParseException("The attribute '$name' has an invalid type of the last element of the destination '$destination'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
            }
            return destination.toString()
        }
    }
}
