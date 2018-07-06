package com.procurement.formsservice.json.data.source.ocds

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.NumberSourceDefinition
import com.procurement.formsservice.json.path.NumberPathSource

data class NumberOcdsValueDefinition(val path: NumberPathSource, val usage: Predicate)

class NumberOCDSSourceDefinition private constructor(private val valueDefinition: NumberOcdsValueDefinition,
                                                     private val defaultDefinition: NumberOcdsValueDefinition,
                                                     override val readOnly: Predicate) : NumberSourceDefinition {
    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true
        if (defaultDefinition.path.isNotEmpty() && defaultDefinition.usage(context)) {
            val defaultValue = context.publicData[defaultDefinition.path]
            if (defaultValue != null) writer["default"] = defaultValue
        }
    }

    override suspend fun buildData(context: Context): Any? =
        if (valueDefinition.path.isNotEmpty() && valueDefinition.usage(context))
            context.publicData[valueDefinition.path]
        else
            null

    class Builder : NumberSourceDefinition.Builder<NumberOCDSSourceDefinition> {
        var value: NumberOcdsValueDefinition = empty()
        var default: NumberOcdsValueDefinition = empty()
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            NumberOCDSSourceDefinition(valueDefinition = value, defaultDefinition = default, readOnly = readOnly)

        fun path(path: String, usage: Predicate = TRUE) =
            NumberOcdsValueDefinition(path = NumberPathSource(path), usage = usage)

        private fun empty() = NumberOcdsValueDefinition(path = NumberPathSource(""), usage = FALSE)
    }
}
