package com.procurement.formsservice.json.data.source.url

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.mdm.MDMKind
import com.procurement.formsservice.json.data.source.BooleanSourceDefinition
import com.procurement.formsservice.json.data.source.IntegerSourceDefinition
import com.procurement.formsservice.json.data.source.NumberSourceDefinition
import com.procurement.formsservice.json.data.source.StringSourceDefinition

fun mbmUrl(builder: UrlSourceDefinition.Builder.() -> Unit) =
    UrlSourceDefinition.Builder().apply(builder)

class UrlSourceDefinition(val value: String, override val readOnly: Predicate)
    : StringSourceDefinition,
      BooleanSourceDefinition,
      IntegerSourceDefinition,
      NumberSourceDefinition {

    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true
    }

    override suspend fun buildData(context: Context): Any? = value

    class Builder : StringSourceDefinition.Builder<UrlSourceDefinition>,
                    BooleanSourceDefinition.Builder<UrlSourceDefinition>,
                    IntegerSourceDefinition.Builder<UrlSourceDefinition>,
                    NumberSourceDefinition.Builder<UrlSourceDefinition> {

        lateinit var kind: MDMKind
        var readOnly: Predicate = FALSE

        override fun build(name: String) = UrlSourceDefinition(value = kind.url, readOnly = readOnly)
    }
}
