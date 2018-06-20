package com.procurement.formsservice.json.data.source

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.Predicate

sealed class SourceDefinition {
    abstract val readOnly: Predicate

    fun isReadOnly(context: Context) = readOnly.invoke(context)

    abstract fun buildForm(context: Context, writer: MutableMap<String, Any>)

    abstract fun buildData(context: Context): Any?
}

abstract class StringSourceDefinition : SourceDefinition()

abstract class BooleanSourceDefinition : SourceDefinition()

abstract class IntegerSourceDefinition : SourceDefinition()

abstract class NumberSourceDefinition : SourceDefinition()