package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.NumberSourceDefinition
import com.procurement.formsservice.json.data.source.empty.EmptyNumberSourceDefinition
import com.procurement.formsservice.json.data.source.enums.NumberEnumSourceDefinition
import com.procurement.formsservice.json.data.source.manual.NumberManualSourceDefinition
import com.procurement.formsservice.json.data.source.ocds.NumberOCDSSourceDefinition
import com.procurement.formsservice.json.exception.DataSpecificException
import com.procurement.formsservice.json.exception.PathParseException
import com.procurement.formsservice.json.path.NumberPathDestination
import com.procurement.formsservice.json.path.PathDestinationElementType

fun number(builder: NumberAttributeDefinition.Builder.() -> Unit) =
    NumberAttributeDefinition.Builder().apply(builder)

class NumberAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: NumberSourceDefinition
) : AttributeDefinition(
    name = name,
    title = title,
    description = description,
    destination = destination,
    required = required,
    usage = usage
) {
    override suspend fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "number"
    }

    override suspend fun buildSource(context: Context, writer: MutableMap<String, Any>) =
        source.buildForm(context, writer)

    override suspend fun buildData(context: Context): Any? =
        when (source) {
            is NumberOCDSSourceDefinition -> {
                val value = source.buildData(context)
                if (required.invoke(context) && source.isReadOnly(context) && value == null)
                    throw DataSpecificException("Data of OCDS for required attribute '$name' not specified.")
                value
            }
            else -> source.buildData(context)

        }

    class Builder : ElementDefinition.Builder<NumberAttributeDefinition> {
        var title: String = ""
        var description: String = ""
        var destination: (name: String) -> NumberPathDestination = emptyPathDestination()
        var required = FALSE
        var usage = TRUE
        var source: NumberSourceDefinition.Builder<*> = empty {}

        override fun build(name: String) =
            NumberAttributeDefinition(
                name = name,
                title = title,
                description = description,
                destination = destinationPath(name, destination),
                required = required,
                usage = usage,
                source = source.build(name)
            )

        //******************************************************************************
        // Path of destination
        //******************************************************************************
        fun destination(path: String): (name: String) -> NumberPathDestination = { NumberPathDestination(path) }

        private fun emptyPathDestination(): (name: String) -> NumberPathDestination = { NumberPathDestination("") }
        //******************************************************************************

        //******************************************************************************
        // Sources
        //******************************************************************************
        fun manual(builder: NumberManualSourceDefinition.Builder.() -> Unit) =
            NumberManualSourceDefinition.Builder().apply(builder)

        fun ocds(builder: NumberOCDSSourceDefinition.Builder.() -> Unit) =
            NumberOCDSSourceDefinition.Builder().apply(builder)

        fun enum(builder: NumberEnumSourceDefinition.Builder.() -> Unit) =
            NumberEnumSourceDefinition.Builder().apply(builder)

        fun empty(builder: EmptyNumberSourceDefinition.Builder.() -> Unit) =
            EmptyNumberSourceDefinition.Builder().apply(builder)
        //******************************************************************************

        private fun destinationPath(name: String, destinationBuilder: (name: String) -> NumberPathDestination): String {
            val destination = destinationBuilder(name)
            if (destination.isNotEmpty()) {
                val actualAttributeType = destination.last().type
                val expectedAttributeType = PathDestinationElementType.NUMBER

                if (expectedAttributeType != actualAttributeType)
                    throw PathParseException("The attribute '$name' has an invalid type of the last element of the destination '$destination'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
            }
            return destination.toString()
        }
    }
}
