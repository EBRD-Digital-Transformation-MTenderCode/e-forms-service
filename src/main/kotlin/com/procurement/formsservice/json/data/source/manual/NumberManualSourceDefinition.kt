package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.NumberValueBuilder
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.NumberSourceDefinition

class NumberManualSourceDefinition(val value: NumberValueBuilder,
                                   val default: NumberValueBuilder,
                                   override val readOnly: Predicate) :
    NumberSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: NUMBER? = default(context)
        if (defaultValue != null) writer["default"] = defaultValue
    }

    override suspend fun buildData(context: Context): Any? = value(context)

    class Builder : NumberSourceDefinition.Builder<NumberManualSourceDefinition> {
        var value: NumberValueBuilder = { null }
        var default: NumberValueBuilder = { null }
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            NumberManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}
