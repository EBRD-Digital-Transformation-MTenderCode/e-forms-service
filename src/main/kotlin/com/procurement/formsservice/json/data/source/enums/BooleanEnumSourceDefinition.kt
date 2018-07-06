package com.procurement.formsservice.json.data.source.enums

import com.procurement.formsservice.json.BooleanValueBuilder
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.enumerator.BooleanEnumElementDefinition
import com.procurement.formsservice.json.data.enumerator.BooleanEnumElementsDefinition
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

class BooleanEnumSourceDefinition private constructor(
    val enum: BooleanEnumElementsDefinition,
    val default: BooleanEnumElementDefinition?,
    val value: BooleanValueBuilder? = null,
    override val readOnly: Predicate
) : BooleanSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
        if (readOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = value?.invoke(context)

    class Builder : BooleanSourceDefinition.Builder<BooleanEnumSourceDefinition> {
        var default: BooleanEnumElementDefinition? = null
        var value: BooleanValueBuilder? = null
        var readOnly: Predicate = FALSE

        private val elements = mutableSetOf<BooleanEnumElementDefinition>()

        operator fun BooleanEnumElementDefinition.unaryPlus() {
            if (elements.contains(this)) throw EnumDefinitionException("Enum can not contain duplicate items.")
            elements.add(this)
        }

        override fun build(name: String) =
            BooleanEnumSourceDefinition(
                enum = BooleanEnumElementsDefinition(elements),
                default = default(name, default),
                value = value,
                readOnly = readOnly
            )

        private fun default(name: String, default: BooleanEnumElementDefinition?) =
            if (default != null && !elements.contains(default))
                throw EnumDefinitionException("The attribute '$name' has the default value '$default' which is not included in the list of elements of the enum '$elements'.")
            else
                default
    }
}
