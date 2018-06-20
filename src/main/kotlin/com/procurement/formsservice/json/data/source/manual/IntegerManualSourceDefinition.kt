package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.IntegerValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition

fun manualInteger(builder: IntegerManualSourceDefinition.Builder.() -> Unit) =
    IntegerManualSourceDefinition.Builder().apply(builder).build()

class IntegerManualSourceDefinition(val value: IntegerValueBuilder,
                                    val default: IntegerValueBuilder,
                                    override val readOnly: Predicate) :
    IntegerSourceDefinition() {

    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: Long? = default(context)
        if (defaultValue != null) writer["default"] = default
    }

    override fun buildData(context: Context): Any? = value(context)

    class Builder {
        var value: IntegerValueBuilder = { null }
        var default: IntegerValueBuilder = { null }
        var readOnly: Predicate = FALSE

        fun build() = IntegerManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}