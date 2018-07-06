package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition
import com.procurement.formsservice.json.data.source.empty.EmptyIntegerSourceDefinition
import com.procurement.formsservice.json.data.source.enums.IntegerEnumSourceDefinition
import com.procurement.formsservice.json.data.source.manual.IntegerManualSourceDefinition
import com.procurement.formsservice.json.data.source.ocds.IntegerOCDSSourceDefinition
import com.procurement.formsservice.json.exception.DataSpecificException
import com.procurement.formsservice.json.exception.PathParseException
import com.procurement.formsservice.json.path.IntegerPathDestination
import com.procurement.formsservice.json.path.PathDestinationElementType

fun integer(builder: IntegerAttributeDefinition.Builder.() -> Unit) =
    IntegerAttributeDefinition.Builder().apply(builder)

class IntegerAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: IntegerSourceDefinition
) : AttributeDefinition(
    name = name,
    title = title,
    description = description,
    destination = destination,
    required = required,
    usage = usage
) {
    override suspend fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "integer"
    }

    override suspend fun buildSource(context: Context, writer: MutableMap<String, Any>) =
        source.buildForm(context, writer)

    override suspend fun buildData(context: Context): Any? =
        when (source) {
            is IntegerOCDSSourceDefinition -> {
                val value = source.buildData(context)
                if (required.invoke(context) && source.isReadOnly(context) && value == null)
                    throw DataSpecificException("Data of OCDS for required attribute '$name' not specified.")
                value
            }
            else -> source.buildData(context)
        }

    class Builder : ElementDefinition.Builder<IntegerAttributeDefinition> {
        var title: String = ""
        var description: String = ""
        var destination: (name: String) -> IntegerPathDestination = emptyPathDestination()
        var required = FALSE
        var usage = TRUE
        var source: IntegerSourceDefinition.Builder<*> = empty {}

        override fun build(name: String) =
            IntegerAttributeDefinition(
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
        fun destination(path: String): (name: String) -> IntegerPathDestination = { IntegerPathDestination(path) }

        private fun emptyPathDestination(): (name: String) -> IntegerPathDestination = { IntegerPathDestination("") }
        //******************************************************************************

        //******************************************************************************
        // Sources
        //******************************************************************************
        fun manual(builder: IntegerManualSourceDefinition.Builder.() -> Unit) =
            IntegerManualSourceDefinition.Builder().apply(builder)

        fun ocds(builder: IntegerOCDSSourceDefinition.Builder.() -> Unit) =
            IntegerOCDSSourceDefinition.Builder().apply(builder)

        fun enum(builder: IntegerEnumSourceDefinition.Builder.() -> Unit) =
            IntegerEnumSourceDefinition.Builder().apply(builder)

        fun empty(builder: EmptyIntegerSourceDefinition.Builder.() -> Unit) =
            EmptyIntegerSourceDefinition.Builder().apply(builder)
        //******************************************************************************

        private fun destinationPath(name: String,
                                    destinationBuilder: (name: String) -> IntegerPathDestination): String {
            val destination = destinationBuilder(name)
            if (destination.isNotEmpty()) {
                val actualAttributeType = destination.last().type
                val expectedAttributeType = PathDestinationElementType.INTEGER

                if (expectedAttributeType != actualAttributeType)
                    throw PathParseException("The attribute '$name' has an invalid type of the last element of the destination '$destination'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
            }
            return destination.toString()
        }
    }
}
