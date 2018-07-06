package com.procurement.formsservice.json.data.source

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.Predicate

interface SourceDefinition {
    val readOnly: Predicate

    fun isReadOnly(context: Context) = readOnly.invoke(context)

    suspend fun buildForm(context: Context, writer: MutableMap<String, Any>)

    suspend fun buildData(context: Context): Any?

    interface Builder<T : SourceDefinition> {
        fun build(name: String): T
    }
}

interface StringSourceDefinition : SourceDefinition {
    interface Builder<T : StringSourceDefinition> {
        fun build(name: String): T
    }
}

interface BooleanSourceDefinition : SourceDefinition {
    interface Builder<T : BooleanSourceDefinition> {
        fun build(name: String): T
    }
}

interface IntegerSourceDefinition : SourceDefinition {
    interface Builder<T : IntegerSourceDefinition> {
        fun build(name: String): T
    }
}

interface NumberSourceDefinition : SourceDefinition {
    interface Builder<T : NumberSourceDefinition> {
        fun build(name: String): T
    }
}
