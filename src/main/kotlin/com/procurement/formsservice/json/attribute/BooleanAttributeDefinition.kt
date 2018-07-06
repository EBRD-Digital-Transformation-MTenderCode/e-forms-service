package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition
import com.procurement.formsservice.json.data.source.empty.EmptyBooleanSourceDefinition
import com.procurement.formsservice.json.data.source.enums.BooleanEnumSourceDefinition
import com.procurement.formsservice.json.data.source.manual.BooleanManualSourceDefinition
import com.procurement.formsservice.json.data.source.ocds.BooleanOCDSSourceDefinition
import com.procurement.formsservice.json.exception.DataSpecificException
import com.procurement.formsservice.json.exception.PathParseException
import com.procurement.formsservice.json.path.BooleanPathDestination
import com.procurement.formsservice.json.path.PathDestinationElementType

fun boolean(builder: BooleanAttributeDefinition.Builder.() -> Unit) =
    BooleanAttributeDefinition.Builder().apply(builder)

class BooleanAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: BooleanSourceDefinition
) : AttributeDefinition(
    name = name,
    title = title,
    description = description,
    destination = destination,
    required = required,
    usage = usage
) {
    override suspend fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "boolean"
    }

    override suspend fun buildSource(context: Context, writer: MutableMap<String, Any>) =
        source.buildForm(context, writer)

    override suspend fun buildData(context: Context): Any? =
        when (source) {
            is BooleanOCDSSourceDefinition -> {
                val value = source.buildData(context)
                if (required.invoke(context) && source.isReadOnly(context) && value == null)
                    throw DataSpecificException("Data of OCDS for required attribute '$name' not specified.")
                value
            }
            else -> source.buildData(context)
        }

    class Builder : ElementDefinition.Builder<BooleanAttributeDefinition> {
        var title: String = ""
        var description: String = ""
        var destination: (name: String) -> BooleanPathDestination = emptyPathDestination()
        var required = FALSE
        var usage = TRUE
        var source: BooleanSourceDefinition.Builder<*> = empty {}

        override fun build(name: String) =
            BooleanAttributeDefinition(
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
        fun destination(path: String): (name: String) -> BooleanPathDestination = { BooleanPathDestination(path) }

        private fun emptyPathDestination(): (name: String) -> BooleanPathDestination = { BooleanPathDestination("") }

        //******************************************************************************
        // Sources
        //******************************************************************************
        fun manual(builder: BooleanManualSourceDefinition.Builder.() -> Unit) =
            BooleanManualSourceDefinition.Builder().apply(builder)

        fun ocds(builder: BooleanOCDSSourceDefinition.Builder.() -> Unit) =
            BooleanOCDSSourceDefinition.Builder().apply(builder)

        fun enum(builder: BooleanEnumSourceDefinition.Builder.() -> Unit) =
            BooleanEnumSourceDefinition.Builder().apply(builder)

        fun empty(builder: EmptyBooleanSourceDefinition.Builder.() -> Unit) =
            EmptyBooleanSourceDefinition.Builder().apply(builder)
        //******************************************************************************

        private fun destinationPath(name: String,
                                    destinationBuilder: (name: String) -> BooleanPathDestination): String {
            val destination = destinationBuilder(name)
            if (destination.isNotEmpty()) {
                val actualAttributeType = destination.last().type
                val expectedAttributeType = PathDestinationElementType.BOOLEAN

                if (expectedAttributeType != actualAttributeType)
                    throw PathParseException("The attribute '$name' has an invalid type of the last element of the destination '$destination'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
            }
            return destination.toString()
        }
    }
}
