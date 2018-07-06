package com.procurement.formsservice.json.data.source.ocds

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition
import com.procurement.formsservice.json.path.IntegerPathSource

data class IntegerOcdsValueDefinition(val path: IntegerPathSource, val usage: Predicate)

class IntegerOCDSSourceDefinition private constructor(private val valueDefinition: IntegerOcdsValueDefinition,
                                                      private val defaultDefinition: IntegerOcdsValueDefinition,
                                                      override val readOnly: Predicate) : IntegerSourceDefinition {
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

    class Builder : IntegerSourceDefinition.Builder<IntegerOCDSSourceDefinition> {
        var value: IntegerOcdsValueDefinition = empty()
        var default: IntegerOcdsValueDefinition = empty()
        var readOnly: Predicate = FALSE

        override fun build(name: String) =
            IntegerOCDSSourceDefinition(valueDefinition = value, defaultDefinition = default, readOnly = readOnly)

        fun path(path: String, usage: Predicate = TRUE) =
            IntegerOcdsValueDefinition(path = IntegerPathSource(path), usage = usage)

        private fun empty() = IntegerOcdsValueDefinition(path = IntegerPathSource(""), usage = FALSE)
    }
}
