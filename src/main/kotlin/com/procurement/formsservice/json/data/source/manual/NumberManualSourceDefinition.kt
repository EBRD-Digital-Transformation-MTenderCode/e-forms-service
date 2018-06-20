package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.NumberValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.NumberSourceDefinition

fun manualNumber(builder: NumberManualSourceDefinition.Builder.() -> Unit) =
    NumberManualSourceDefinition.Builder().apply(builder).build()

class NumberManualSourceDefinition(val value: NumberValueBuilder,
                                   val default: NumberValueBuilder,
                                   override val readOnly: Predicate) :
    NumberSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: Float? = default(context)
        if (defaultValue != null) writer["default"] = default
    }

    override fun buildData(context: Context): Any? = value(context)

    class Builder {
        var value: NumberValueBuilder = { null }
        var default: NumberValueBuilder = { null }
        var readOnly: Predicate = FALSE

        fun build() = NumberManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}