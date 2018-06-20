package com.procurement.formsservice.json.container

import com.procurement.formsservice.json.Condition
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.ElementDefinition
import com.procurement.formsservice.json.PrintableElementDefinition
import com.procurement.formsservice.json.attribute.BooleanAttributeDefinition
import com.procurement.formsservice.json.attribute.IntegerAttributeDefinition
import com.procurement.formsservice.json.attribute.NumberAttributeDefinition
import com.procurement.formsservice.json.attribute.StringAttributeDefinition

class AlternativesDefinition : ElementDefinition {
    private val alternatives = mutableListOf<AlternativeDefinition>()

    fun alternative(condition: Condition, block: AlternativeDefinition.() -> Unit) =
        AlternativeDefinition(condition).apply(block).also { alternatives.add(it) }

    fun getElements(context: Context): List<PrintableElementDefinition> {
        for (alternative in alternatives) {
            if (alternative.condition(context))
                return alternative.getElements(context)
        }
        return emptyList()
    }
}

class AlternativeDefinition(val condition: Condition) {
    private val children = mutableListOf<PrintableElementDefinition>()

    fun getElements(context: Context): List<PrintableElementDefinition> = children.filter { it.usage(context) }

    private fun add(attribute: PrintableElementDefinition) {
        children.add(attribute)
    }

    infix fun String.to(builder: BooleanAttributeDefinition.Builder) = add(builder.build(this))
    infix fun String.to(builder: StringAttributeDefinition.Builder) = add(builder.build(this))
    infix fun String.to(builder: IntegerAttributeDefinition.Builder) = add(builder.build(this))
    infix fun String.to(builder: NumberAttributeDefinition.Builder) = add(builder.build(this))
    infix fun String.to(builder: ObjectDefinition.Builder) = add(builder.build(this))
    infix fun String.to(builder: ArrayDefinition.Builder) = add(builder.build(this))
}