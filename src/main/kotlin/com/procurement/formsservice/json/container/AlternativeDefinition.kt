package com.procurement.formsservice.json.container

import com.procurement.formsservice.json.Condition
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.attribute.BooleanAttributeDefinition
import com.procurement.formsservice.json.attribute.IntegerAttributeDefinition
import com.procurement.formsservice.json.attribute.NumberAttributeDefinition
import com.procurement.formsservice.json.attribute.StringAttributeDefinition

class AlternativeDefinition(
    override val name: String,
    val condition: Condition,
    private val attributes: List<ElementDefinition>
) : ElementDefinition {

    override val required: Predicate
        get() = throw NotImplementedError("An operation is not implemented.")
    override val usage: Predicate
        get() = throw NotImplementedError("An operation is not implemented.")

    override suspend fun buildForm(context: Context): Map<String, Any> = emptyMap()
    override suspend fun buildData(context: Context): Any? = null

    fun getElements(context: Context): List<ElementDefinition> =
        if (condition(context))
            attributes.filter { it.usage(context) }
        else
            emptyList()

    class Builder(private val condition: Condition) : ElementDefinition.Builder<AlternativeDefinition> {
        private val alternatives = mutableMapOf<String, ElementDefinition.Builder<*>>()

        private fun add(name: String, builder: ElementDefinition.Builder<*>) {
            alternatives[name] = builder
        }

        override fun build(name: String) = AlternativeDefinition(
            name = name,
            condition = condition,
            attributes = alternatives.map { (name, builder) -> builder.build(name) }
        )

        infix fun String.to(builder: BooleanAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: StringAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: IntegerAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: NumberAttributeDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ObjectDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ArrayObjectsDefinition.Builder) = add(this, builder)
        infix fun String.to(builder: ArrayStringsDefinition.Builder) = add(this, builder)
    }
}
