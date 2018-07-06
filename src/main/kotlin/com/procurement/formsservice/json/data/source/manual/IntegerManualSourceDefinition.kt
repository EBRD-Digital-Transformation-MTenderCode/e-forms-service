package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.IntegerValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition

class IntegerManualSourceDefinition(val value: IntegerValueBuilder,
                                    val default: IntegerValueBuilder,
                                    override val readOnly: Predicate) :
    IntegerSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: INTEGER? = default(context)
        if (defaultValue != null) writer["default"] = defaultValue
    }

    override suspend fun buildData(context: Context): Any? = value(context)

    class Builder : IntegerSourceDefinition.Builder<IntegerManualSourceDefinition> {
        var value: IntegerValueBuilder = { null }
        var default: IntegerValueBuilder = { null }
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            IntegerManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}
