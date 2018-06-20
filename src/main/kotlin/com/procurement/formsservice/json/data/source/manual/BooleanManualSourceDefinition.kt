package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.BooleanValueBuilder
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition

fun manualBoolean(builder: BooleanManualSourceDefinition.Builder.() -> Unit) =
    BooleanManualSourceDefinition.Builder().apply(builder).build()

class BooleanManualSourceDefinition(val value: BooleanValueBuilder,
                                    val default: BooleanValueBuilder,
                                    override val readOnly: Predicate) :
    BooleanSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: Boolean? = default(context)
        if (defaultValue != null) writer["default"] = default
    }

    override fun buildData(context: Context): Any? = value(context)

    class Builder {
        var value: BooleanValueBuilder = { null }
        var default: BooleanValueBuilder = { null }
        var readOnly: Predicate = FALSE

        fun build() = BooleanManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}