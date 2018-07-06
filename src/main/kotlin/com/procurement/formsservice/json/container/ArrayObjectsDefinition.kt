package com.procurement.formsservice.json.container

import com.procurement.formsservice.json.Condition
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.attribute.BooleanAttributeDefinition
import com.procurement.formsservice.json.attribute.IntegerAttributeDefinition
import com.procurement.formsservice.json.attribute.NumberAttributeDefinition
import com.procurement.formsservice.json.attribute.StringAttributeDefinition
import com.procurement.formsservice.json.exception.DataSpecificException
import java.util.*

fun arrayObjects(builder: ArrayObjectsDefinition.Builder.() -> Unit) =
    ArrayObjectsDefinition.Builder().apply(builder)

class ArrayObjectsDefinition(
    override val name: String,
    val title: String,
    val description: String,
    private val children: List<ElementDefinition>,
    override val required: Predicate,
    override val usage: Predicate
) : ElementDefinition {

    override suspend fun buildForm(context: Context): Map<String, Any> =
        if (usage(context))
            writeForm(context)
        else
            emptyMap()

    private suspend fun writeForm(context: Context): Map<String, Any> {
        val listItems = asItems(context)
        return if (listItems.isNotEmpty()) {
            linkedMapOf<String, Any>().apply {
                if (title.isNotBlank()) this["title"] = title
                if (description.isNotBlank()) this["description"] = description
                this["type"] = "array"
                this["items"] = listItems
            }
        } else emptyMap()
    }

    private suspend fun asItems(context: Context): Map<String, Any> =
        if (children.isNotEmpty()) {
            val attributes = getAttributes(context)
            if (attributes.isNotEmpty()) {
                linkedMapOf<String, Any>().apply {
                    this["type"] = "object"

                    val requiredAttributes =
                        attributes.asSequence().filter { it.required(context) }.map { it.name }.toList()
                    if (requiredAttributes.isNotEmpty()) this["required"] = requiredAttributes

                    this["properties"] = getProperties(context, attributes)
                }
            } else emptyMap()
        } else emptyMap()

    private fun getAttributes(context: Context): List<ElementDefinition> =
        if (children.isNotEmpty())
            mutableListOf<ElementDefinition>().apply {
                for (attribute in children) {
                    when (attribute) {
                        is AlternativeDefinition -> this.addAll(attribute.getElements(context))
                        else -> if (attribute.usage(context)) this.add(attribute)
                    }
                }
            }
        else emptyList()

    private suspend fun getProperties(context: Context, attributes: List<ElementDefinition>): Map<String, Any> {
        val properties = mutableMapOf<String, Any>()
        for (attribute in attributes) {
            val property = attribute.buildForm(context)
            if (property.isNotEmpty()) properties[attribute.name] = property
        }
        return properties
    }

    override suspend fun buildData(context: Context): Map<String, Any> =
        if (children.isNotEmpty())
            writeData(context)
        else
            emptyMap()

    private suspend fun writeData(context: Context): Map<String, Any> {
        val attributes = getAttributes(context)
        return if (attributes.isNotEmpty()) {
            linkedMapOf<String, Any>().apply {
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
        } else emptyMap()
    }

    class Builder : ElementDefinition.Builder<ArrayObjectsDefinition> {
        private val children = mutableMapOf<String, ElementDefinition.Builder<*>>()

        var title: String = ""
        var description: String = ""
        var required = FALSE
        var usage = TRUE

        private fun add(name: String, builder: ElementDefinition.Builder<*>) {
            if (children.containsKey(name)) throw DataSpecificException("Element with name '$name' is already.")
            children[name] = builder
        }

        override fun build(name: String) =
            ArrayObjectsDefinition(
                name = name,
                title = title,
                description = description,
                children = children.map { (name, builder) -> builder.build(name) },
                required = required,
                usage = usage
            )

        fun alternative(condition: Condition, builder: AlternativeDefinition.Builder.() -> Unit) =
            AlternativeDefinition.Builder(condition).apply(builder).also { add(UUID.randomUUID().toString(), it) }

        infix fun String.to(builder: BooleanAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: StringAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: IntegerAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: NumberAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ObjectDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ArrayObjectsDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ArrayStringsDefinition.Builder) = add(this, builder)
    }
}
