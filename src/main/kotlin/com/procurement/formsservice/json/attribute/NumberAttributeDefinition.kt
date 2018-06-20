package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.NumberSourceDefinition

fun number(builder: NumberAttributeDefinition.Builder.() -> Unit) =
    NumberAttributeDefinition.Builder().apply(builder)

class NumberAttributeDefinition private constructor(
    name: String,
    title: String,
    description: String,
    destination: String,
    required: Predicate,
    usage: Predicate,
    val source: NumberSourceDefinition?
) : AttributeDefinition(
    name = name,
    title = title,
    description = description,
    destination = destination,
    required = required,
    usage = usage
) {
    override fun buildType(writer: MutableMap<String, Any>) {
        writer["type"] = "number"
    }

    override fun buildSource(context: Context, writer: MutableMap<String, Any>) {
        source?.buildForm(context, writer)
    }

    override fun buildData(context: Context): Any? = source?.buildData(context)

    class Builder {
        var title: String = ""
        var description: String = ""
        var destination: String = ""
        var required = FALSE
        var usage = TRUE
        var source: NumberSourceDefinition? = null

        fun build(name: String) =
            NumberAttributeDefinition(
                name = name,
                title = title,
                description = description,
                destination = destination,
                required = required,
                usage = usage,
                source = source
            )
    }
}

