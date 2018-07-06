package com.procurement.formsservice.json.data.source.ocds

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.path.StringPathSource

data class StringOcdsValueDefinition(val path: StringPathSource, val usage: Predicate)

class StringOCDSSourceDefinition private constructor(private val valueDefinition: StringOcdsValueDefinition,
                                                     private val defaultDefinition: StringOcdsValueDefinition,
                                                     override val readOnly: Predicate) : StringSourceDefinition {
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

    class Builder : StringSourceDefinition.Builder<StringOCDSSourceDefinition> {
        var value: StringOcdsValueDefinition = empty()
        var default: StringOcdsValueDefinition = empty()
        var readOnly: Predicate = FALSE

        override fun build(name: String): StringOCDSSourceDefinition =
            StringOCDSSourceDefinition(valueDefinition = value, defaultDefinition = default, readOnly = readOnly)

        fun path(path: String, usage: Predicate = TRUE) =
            StringOcdsValueDefinition(path = StringPathSource(path), usage = usage)

        private fun empty() = StringOcdsValueDefinition(path = StringPathSource(""), usage = FALSE)
    }
}
