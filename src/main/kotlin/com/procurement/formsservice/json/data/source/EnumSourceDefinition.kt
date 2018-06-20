package com.procurement.formsservice.json.data.source

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.enumerator.*

class StringEnumSourceDefinition(
    val enum: StringEnumElementsDefinition,
    val default: StringEnumElementDefinition? = null,
    override val readOnly: Predicate = FALSE
) : StringSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
    }

    override fun buildData(context: Context): Any? = null
}

class BooleanEnumSourceDefinition(
    val enum: BooleanEnumElementsDefinition,
    val default: BooleanEnumElementDefinition? = null,
    override val readOnly: Predicate = FALSE
) : BooleanSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
    }

    override fun buildData(context: Context): Any? = null
}

class IntegerEnumSourceDefinition(
    val enum: IntegerEnumElementsDefinition,
    val default: IntegerEnumElementDefinition? = null,
    override val readOnly: Predicate = FALSE
) : IntegerSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
    }

    override fun buildData(context: Context): Any? = null
}

class NumberEnumSourceDefinition(
    val enum: NumberEnumElementsDefinition,
    val default: NumberEnumElementDefinition? = null,
    override val readOnly: Predicate = FALSE
) : NumberSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (default != null) writer["default"] = default.value
        if (enum.values.isNotEmpty()) writer["enum"] = enum.values
        if (enum.names.isNotEmpty()) writer["enumNames"] = enum.names
        if (enum.descriptions.isNotEmpty()) writer["enumDesc"] = enum.descriptions
    }

    override fun buildData(context: Context): Any? = null
}