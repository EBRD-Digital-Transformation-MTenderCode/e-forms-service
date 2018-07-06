package com.procurement.formsservice.json.data.source.enums

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.NumberValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.enumerator.NumberEnumElementDefinition
import com.procurement.formsservice.json.data.enumerator.NumberEnumElementsDefinition
import com.procurement.formsservice.json.data.source.NumberSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

class NumberEnumSourceDefinition private constructor(
    val enum: NumberEnumElementsDefinition,
    val default: NumberEnumElementDefinition?,
    val value: NumberValueBuilder? = null,
    override val readOnly: Predicate
) : NumberSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
        if (readOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = value?.invoke(context)

    class Builder : NumberSourceDefinition.Builder<NumberEnumSourceDefinition> {
        var default: NumberEnumElementDefinition? = null
        var value: NumberValueBuilder? = null
        var readOnly: Predicate = FALSE

        private val elements = mutableSetOf<NumberEnumElementDefinition>()

        operator fun NumberEnumElementDefinition.unaryPlus() {
            if (elements.contains(this)) throw EnumDefinitionException("Enum can not contain duplicate items.")
            elements.add(this)
        }

        override fun build(name: String) =
            NumberEnumSourceDefinition(
                enum = NumberEnumElementsDefinition(elements),
                default = default(name, default),
                value = value,
                readOnly = readOnly
            )

        private fun default(name: String, default: NumberEnumElementDefinition?) =
            if (default != null && !elements.contains(default))
                throw EnumDefinitionException("The attribute '$name' has the default value '$default' which is not included in the list of elements of the enum '$elements'.")
            else
                default
    }
}
