package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.BooleanValueBuilder
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition

class BooleanManualSourceDefinition(val value: BooleanValueBuilder,
                                    val default: BooleanValueBuilder,
                                    override val readOnly: Predicate) :
    BooleanSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: BOOLEAN? = default(context)
        if (defaultValue != null) writer["default"] = defaultValue
    }

    override suspend fun buildData(context: Context): Any? = value(context)

    class Builder : BooleanSourceDefinition.Builder<BooleanManualSourceDefinition> {
        var value: BooleanValueBuilder = { null }
        var default: BooleanValueBuilder = { null }
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            BooleanManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}
