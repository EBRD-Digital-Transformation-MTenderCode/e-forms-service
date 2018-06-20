package com.procurement.formsservice.json.data.mdm

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Predicate
import com.procurement.formsservice.json.data.source.StringSourceDefinition
import com.procurement.formsservice.json.exception.EnumDefinitionException
import javax.naming.Context.LANGUAGE

data class MdmCPVS(val code: String, val name: String)

fun mdmCPVS(builder: CPVSSourceDefinition.Builder.() -> Unit = {}) =
    CPVSSourceDefinition.Builder().apply(builder).build()

class CPVSSourceDefinition private constructor(override val readOnly: Predicate) : StringSourceDefinition() {
    override fun buildForm(context: Context, writer: MutableMap<String, Any>) {
        if (isReadOnly(context)) writer["readOnly"] = true

        val listCPVS = context.repository.cpvs(context.parameters.getAsString(LANGUAGE))
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

        fun build() = CPVSSourceDefinition(readOnly = readOnly)
    }
}
