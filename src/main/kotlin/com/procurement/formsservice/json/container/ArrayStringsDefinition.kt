package com.procurement.formsservice.json.container

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
import com.procurement.formsservice.json.exception.PathParseException
import com.procurement.formsservice.json.path.PathDestinationElementType
import com.procurement.formsservice.json.path.StringArrayPathDestination

fun arrayStrings(builder: ArrayStringsDefinition.Builder.() -> Unit) =
    ArrayStringsDefinition.Builder().apply(builder)

class ArrayStringsDefinition(
    override val name: String,
    val title: String,
    val description: String,
    val destination: String,
    val minItems: Int,
    val uniqueItems: Boolean,
    val source: StringSourceDefinition,
    override val required: Predicate,
    override val usage: Predicate
) : ElementDefinition {

    override suspend fun buildForm(context: Context): Map<String, Any> =
        if (usage(context))
            writeForm(context)
        else
            emptyMap()

    private suspend fun writeForm(context: Context): Map<String, Any> = linkedMapOf<String, Any>().apply {
        if (title.isNotBlank()) this["title"] = title
        if (description.isNotBlank()) this["description"] = description
        this["type"] = "array"
        if (minItems != -1) this["minItems"] = minItems
        if (uniqueItems) this["uniqueItems"] = true
        this["items"] = items(context)
    }

    private suspend fun items(context: Context): Map<String, Any> = linkedMapOf<String, Any>().apply {
        this["type"] = "string"
        source.buildForm(context = context, writer = this)
        this["ocds"] = destination
    }

    override suspend fun buildData(context: Context): Map<String, Any> = linkedMapOf<String, Any>().apply {
        val values = source.buildData(context)
        if (values != null) {
            if (values is List<*>) {
                if (values.isNotEmpty()) this[name] = values
            } else
                this[name] = values
        }
    }

    class Builder : ElementDefinition.Builder<ArrayStringsDefinition> {
        var title: String = ""
        var description: String = ""
        var minItems: Int = -1
        var uniqueItems: Boolean = false
        var source: StringSourceDefinition.Builder<*> = empty { }
        var destination: (name: String) -> StringArrayPathDestination = emptyPathDestination()
        var required = FALSE
        var usage = TRUE

        override fun build(name: String) =
            ArrayStringsDefinition(
                name = name,
                title = title,
                description = description,
                destination = destinationPath(name, destination),
                minItems = minItems,
                uniqueItems = uniqueItems,
                source = source.build(name),
                required = required,
                usage = usage
            )

        //******************************************************************************
        // Path of destination
        //******************************************************************************
        fun destination(path: String): (name: String) -> StringArrayPathDestination =
            { StringArrayPathDestination(path) }

        private fun emptyPathDestination(): (name: String) -> StringArrayPathDestination =
            { StringArrayPathDestination("") }
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

        private fun destinationPath(name: String,
                                    destinationBuilder: (name: String) -> StringArrayPathDestination): String {
            val destination = destinationBuilder(name)
            if (destination.isNotEmpty()) {
                val actualAttributeType = destination.last().type
                val expectedAttributeType = PathDestinationElementType.ARRAY_OF_STRING

                if (expectedAttributeType != actualAttributeType)
                    throw PathParseException("The attribute '$name' has an invalid type of the last element of the destination '$destination'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
            }
            return destination.toString()
        }
    }
}
