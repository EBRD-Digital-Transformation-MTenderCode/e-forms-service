package com.procurement.formsservice.json.data.source.ocds

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition
import com.procurement.formsservice.json.path.BooleanPathSource

data class BooleanOcdsValueDefinition(val path: BooleanPathSource, val usage: Predicate)

class BooleanOCDSSourceDefinition private constructor(private val valueDefinition: BooleanOcdsValueDefinition,
                                                      private val defaultDefinition: BooleanOcdsValueDefinition,
                                                      override val readOnly: Predicate) : BooleanSourceDefinition {
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

    class Builder : BooleanSourceDefinition.Builder<BooleanOCDSSourceDefinition> {
        var value: BooleanOcdsValueDefinition = empty()
        var default: BooleanOcdsValueDefinition = empty()
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            BooleanOCDSSourceDefinition(valueDefinition = value, defaultDefinition = default, readOnly = readOnly)

        fun path(path: String, usage: Predicate = TRUE) =
            BooleanOcdsValueDefinition(path = BooleanPathSource(path), usage = usage)

        private fun empty() = BooleanOcdsValueDefinition(path = BooleanPathSource(""), usage = FALSE)
    }
}
