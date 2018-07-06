package com.procurement.formsservice.json.data.source.empty

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition

class EmptyBooleanSourceDefinition(override val readOnly: Predicate) :
    BooleanSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = null

    class Builder : BooleanSourceDefinition.Builder<EmptyBooleanSourceDefinition> {
        var readOnly: Predicate = FALSE

        override fun build(name: String) = EmptyBooleanSourceDefinition(readOnly = readOnly)
    }
}
