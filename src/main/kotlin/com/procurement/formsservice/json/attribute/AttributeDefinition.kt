package com.procurement.formsservice.json.attribute

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.PrintableElementDefinition

abstract class AttributeDefinition(
    override val name: String,
    val title: String,
    val description: String,
    val destination: String,
    override val required: Predicate,
    override val usage: Predicate
) : PrintableElementDefinition {
    override fun buildForm(context: Context): Map<String, Any> = if (usage(context))
        linkedMapOf<String, Any>()
            .apply {
                if (title.isNotBlank()) this["title"] = title
                if (description.isNotBlank()) this["description"] = description
                buildType(this)
                buildSource(context = context, writer = this)
                if (destination.isNotBlank()) this["ocds"] = destination
            }
    else
        emptyMap()

    protected abstract fun buildType(writer: MutableMap<String, Any>)
    protected abstract fun buildSource(context: Context, writer: MutableMap<String, Any>)
}
