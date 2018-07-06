package com.procurement.formsservice.json.data.source.manual

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.STRING
import com.procurement.formsservice.json.StringValueBuilder
import com.procurement.formsservice.json.data.source.StringSourceDefinition

class StringManualSourceDefinition(val value: StringValueBuilder,
                                   val default: StringValueBuilder,
                                   override val readOnly: Predicate) :
    StringSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val defaultValue: STRING? = default(context)
        if (defaultValue != null && defaultValue.isNotBlank()) writer["default"] = defaultValue
    }

    override suspend fun buildData(context: Context): Any? = value(context)

    class Builder : StringSourceDefinition.Builder<StringManualSourceDefinition> {
        var value: StringValueBuilder = { null }
        var default: StringValueBuilder = { null }
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            StringManualSourceDefinition(value = value, default = default, readOnly = readOnly)
    }
}
