package com.procurement.formsservice.json.data.mdm

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.formsservice.definition.CommonNamesParameters
import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException

data class MdmCurrency(@JsonProperty("data") val data: MdmCurrencyData)

data class MdmCurrencyData(@JsonProperty("items") val items: List<MdmCurrencyItem>)

data class MdmCurrencyItem(@JsonProperty("code") val code: String,
                           @JsonProperty("name") val name: String)

fun mdmCurrency(builder: CurrencySourceDefinition.Builder.() -> Unit = {}) =
    CurrencySourceDefinition.Builder().apply(builder)

class CurrencySourceDefinition private constructor(override val readOnly: Predicate) : StringSourceDefinition {
    override suspend fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val currencyData = context.repository.currency(
            lang = context.parameters[CommonNamesParameters.LANGUAGE],
            country = context.parameters[CommonNamesParameters.COUNTRY]
        )
        val codes = mutableListOf<String>()
        val names = mutableListOf<String>()
        for ((code, name) in currencyData.data.items) {
            codes.add(code)
            names.add(name)
        }

        if (names.isNotEmpty() && names.size != codes.size) {
            throw EnumDefinitionException("The number of elements of the list of the names: [${names.size}] does not match the number of values: [${codes.size}].")
        }

        if (codes.isNotEmpty()) writer["enum"] = codes
        if (names.isNotEmpty()) writer["enumNames"] = names
    }

    override suspend fun buildData(context: Context): Any? = null

    class Builder : StringSourceDefinition.Builder<CurrencySourceDefinition> {
        var readOnly: Predicate = FALSE

        override fun build(name: String) = CurrencySourceDefinition(readOnly = readOnly)
    }
}
