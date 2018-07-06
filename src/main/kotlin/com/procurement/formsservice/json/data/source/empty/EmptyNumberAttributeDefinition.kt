package com.procurement.formsservice.json.data.source.empty

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.NumberSourceDefinition

class EmptyNumberSourceDefinition(override val readOnly: Predicate) :
    NumberSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = null

    class Builder : NumberSourceDefinition.Builder<EmptyNumberSourceDefinition> {
        var readOnly: Predicate = FALSE

        override fun build(name: String) = EmptyNumberSourceDefinition(readOnly = readOnly)
    }
}
