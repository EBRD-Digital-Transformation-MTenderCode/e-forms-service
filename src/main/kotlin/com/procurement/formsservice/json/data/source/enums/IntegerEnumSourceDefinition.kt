package com.procurement.formsservice.json.data.source.enums

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.IntegerValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.enumerator.IntegerEnumElementDefinition
import com.procurement.formsservice.json.data.enumerator.IntegerEnumElementsDefinition
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

class IntegerEnumSourceDefinition private constructor(
    val enum: IntegerEnumElementsDefinition,
    val default: IntegerEnumElementDefinition?,
    val value: IntegerValueBuilder? = null,
    override val readOnly: Predicate
) : IntegerSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
        if (readOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = value?.invoke(context)

    class Builder : IntegerSourceDefinition.Builder<IntegerEnumSourceDefinition> {
        var default: IntegerEnumElementDefinition? = null
        var value: IntegerValueBuilder? = null
        var readOnly: Predicate = FALSE

        private val elements = mutableSetOf<IntegerEnumElementDefinition>()

        operator fun IntegerEnumElementDefinition.unaryPlus() {
            if (elements.contains(this)) throw EnumDefinitionException("Enum can not contain duplicate items.")
            elements.add(this)
        }

        override fun build(name: String) =
            IntegerEnumSourceDefinition(
                enum = IntegerEnumElementsDefinition(elements),
                default = default(name, default),
                value = value,
                readOnly = readOnly
            )

        private fun default(name: String, default: IntegerEnumElementDefinition?) =
            if (default != null && !elements.contains(default))
                throw EnumDefinitionException("The attribute '$name' has the default value '$default' which is not included in the list of elements of the enum '$elements'.")
            else
                default
    }
}
