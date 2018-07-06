package com.procurement.formsservice.json

typealias STRING = String
typealias BOOLEAN = Boolean
typealias INTEGER = Long
typealias NUMBER = Double

typealias Predicate = (context: Context) -> Boolean
typealias Condition = (context: Context) -> Boolean

val TRUE: (context: Context) -> Boolean
    get() = { true }

val FALSE: (context: Context) -> Boolean
    get() = { false }

typealias StringValueBuilder = (context: Context) -> STRING?
typealias BooleanValueBuilder = (context: Context) -> BOOLEAN?
typealias IntegerValueBuilder = (context: Context) -> INTEGER?
typealias NumberValueBuilder = (context: Context) -> NUMBER?

interface ElementDefinition {
    val name: String
    val required: Predicate
    val usage: Predicate

    suspend fun buildForm(context: Context): Map<String, Any>
    suspend fun buildData(context: Context): Any?

    interface Builder<T : ElementDefinition> {
        fun build(name: String): T
    }
}
