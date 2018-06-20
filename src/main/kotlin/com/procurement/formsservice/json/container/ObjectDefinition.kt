package com.procurement.formsservice.json.container

import com.procurement.formsservice.json.*
import com.procurement.formsservice.json.attribute.BooleanAttributeDefinition
import com.procurement.formsservice.json.attribute.IntegerAttributeDefinition
import com.procurement.formsservice.json.attribute.NumberAttributeDefinition
import com.procurement.formsservice.json.attribute.StringAttributeDefinition

fun obj(builder: ObjectDefinition.Builder.() -> Unit) = ObjectDefinition.Builder().apply(builder)

class ObjectDefinition(
    override val name: String,
    val title: String,
    val description: String,
    private val children: List<ElementDefinition>,
    override val required: Predicate,
    override val usage: Predicate
) : PrintableElementDefinition {

    override fun buildForm(context: Context): Map<String, Any> =
        if (this.usage(context)) {
            val attributes = getAttributes(context)
            if (attributes.isNotEmpty())
                linkedMapOf<String, Any>()
                    .apply {
                        this["title"] = title
                        if (description.isNotBlank()) this["description"] = description
                        this["type"] = "object"

                        val requiredAttributes =
                            attributes.asSequence().filter { it.required(context) }.map { it.name }.toList()
                        if (requiredAttributes.isNotEmpty()) this["required"] = requiredAttributes

                        this["properties"] = getProperties(context, attributes)
                    }
            else emptyMap()
        } else emptyMap()

    private fun getAttributes(context: Context): List<PrintableElementDefinition> =
        if (children.isNotEmpty())
            mutableListOf<PrintableElementDefinition>().apply {
                for (attribute in children) {
                    when (attribute) {
                        is PrintableElementDefinition -> if (attribute.usage(context)) this.add(attribute)
                        is AlternativesDefinition -> this.addAll(attribute.getElements(context))
                    }
                }
            }
        else emptyList()

    private fun getProperties(context: Context, attributes: List<PrintableElementDefinition>): Map<String, Any> {
        val properties = mutableMapOf<String, Any>()
        for (attribute in attributes) {
            val property = attribute.buildForm(context)
            if (property.isNotEmpty()) properties[attribute.name] = property
        }
        return properties
    }

    override fun buildData(context: Context): Map<String, Any> =
        if (this.usage(context)) {
            val attributes = getAttributes(context)
            if (attributes.isNotEmpty())
                linkedMapOf<String, Any>()
                    .apply {
                        for (attribute in attributes) {
                            val attributeValue = attribute.buildData(context)
                            if (attributeValue != null) {
                                if (attributeValue is Map<*, *>) {
                                    if (attributeValue.isNotEmpty()) this[attribute.name] = attributeValue
                                } else
                                    this[attribute.name] = attributeValue
                            }
                        }
                    }
            else emptyMap()
        } else emptyMap()

    class Builder {
        private val children = mutableListOf<ElementDefinition>()

        var title: String = ""
        var description: String = ""
        var required = FALSE
        var usage = TRUE

        private fun add(attribute: ElementDefinition) {
            children.add(attribute)
        }

        fun build(name: String = "") =
            ObjectDefinition(
                name = name,
                title = title,
                description = description,
                children = children,
                required = required,
                usage = usage
            )

        fun alternatives(block: AlternativesDefinition.() -> Unit) =
            AlternativesDefinition().apply(block).also { add(it) }

        infix fun String.to(builder: BooleanAttributeDefinition.Builder) = add(builder.build(this))
        infix fun String.to(builder: StringAttributeDefinition.Builder) = add(builder.build(this))
        infix fun String.to(builder: IntegerAttributeDefinition.Builder) = add(builder.build(this))
        infix fun String.to(builder: NumberAttributeDefinition.Builder) = add(builder.build(this))
        infix fun String.to(builder: ObjectDefinition.Builder) = add(builder.build(this))
        infix fun String.to(builder: ArrayDefinition.Builder) = add(builder.build(this))
    }
}
