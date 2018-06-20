package com.procurement.formsservice.json.data.mdm

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Parameters.Companion.COUNTRY
import com.procurement.formsservice.json.Parameters.Companion.LANGUAGE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

data class MdmCurrency(val code: String, val name: String)

fun mdmCurrency(builder: CurrencySourceDefinition.Builder.() -> Unit = {}) =
    CurrencySourceDefinition.Builder().apply(builder).build()

class CurrencySourceDefinition private constructor(override val readOnly: Predicate) : StringSourceDefinition() {
    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val lang = context.parameters.getAsString(LANGUAGE)
        val country = context.parameters.getAsString(COUNTRY)

        val listCPVS = context.repository.currency(lang, country)
        val codes = mutableListOf<String>()
        val names = mutableListOf<String>()
        for ((code, name) in listCPVS) {
            codes.add(code)
            names.add(name)
        }

        if (names.isNotEmpty() && names.size != codes.size) {
            throw EnumDefinitionException("The number of elements of the list of the names: [${names.size}] does not match the number of values: [${codes.size}].")
        }

        if (codes.isNotEmpty()) writer["enum"] = codes
        if (names.isNotEmpty()) writer["enumNames"] = names
    }

    override fun buildData(context: Context): Any? = null

    class Builder {
        var readOnly: Predicate = FALSE

        fun build() = CurrencySourceDefinition(readOnly = readOnly)
    }
}
