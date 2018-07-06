package com.procurement.formsservice.json.data.source.enums

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.StringValueBuilder
import com.procurement.formsservice.json.data.enumerator.StringEnumElementDefinition
import com.procurement.formsservice.json.data.enumerator.StringEnumElementsDefinition
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

class StringEnumSourceDefinition private constructor(
    val enum: StringEnumElementsDefinition,
    val default: StringEnumElementDefinition?,
    val value: StringValueBuilder? = null,
    override val readOnly: Predicate
) : StringSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
        if (readOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = value?.invoke(context)

    class Builder : StringSourceDefinition.Builder<StringEnumSourceDefinition> {
        var default: StringEnumElementDefinition? = null
        var value: StringValueBuilder? = null
        var readOnly: Predicate = FALSE

        private val elements = mutableSetOf<StringEnumElementDefinition>()

        operator fun StringEnumElementDefinition.unaryPlus() {
            if (elements.contains(this)) throw EnumDefinitionException("Enum can not contain duplicate items.")
            elements.add(this)
        }

        override fun build(name: String) =
            StringEnumSourceDefinition(
                enum = StringEnumElementsDefinition(elements),
                value = value,
                default = default(name, default),
                readOnly = readOnly
            )

        private fun default(name: String, default: StringEnumElementDefinition?) =
            if (default != null && !elements.contains(default))
                throw EnumDefinitionException("The attribute '$name' has the default value '$default' which is not included in the list of elements of the enum '$elements'.")
            else
                default
    }
}
