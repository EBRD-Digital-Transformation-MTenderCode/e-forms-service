package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.StringValueBuilder
import com.procurement.formsservice.json.data.source.StringSourceDefinition

fun manualString(builder: StringManualSourceDefinition.Builder.() -> Unit) =
    StringManualSourceDefinition.Builder().apply(builder).build()

class StringManualSourceDefinition(val value: StringValueBuilder,
                                   val default: StringValueBuilder,
                                   override val readOnly: Predicate) :
    StringSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: String? = default(context)
        if (defaultValue != null && defaultValue.isNotBlank()) writer["default"] = default
    }

    override fun buildData(context: Context): Any? = value(context)

    class Builder {
        var value: StringValueBuilder = { null }
        var default: StringValueBuilder = { null }
        var readOnly: Predicate = FALSE

        fun build() = StringManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}