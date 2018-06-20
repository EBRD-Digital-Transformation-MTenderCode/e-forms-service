package com.procurement.formsservice.json

typealias Predicate = (context: Context) -> Boolean
typealias Condition = (context: Context) -> Boolean

val TRUE: (context: Context) -> Boolean
    get() = { true }

val FALSE: (context: Context) -> Boolean
    get() = { false }

typealias StringValueBuilder = (context: Context) -> String?
typealias BooleanValueBuilder = (context: Context) -> Boolean?
typealias IntegerValueBuilder = (context: Context) -> Long?
typealias NumberValueBuilder = (context: Context) -> Float?

interface ElementDefinition

interface PrintableElementDefinition : ElementDefinition {
    val name: String
    val required: Predicate
    val usage: Predicate

    fun buildForm(context: Context): Map<String, Any>

    fun buildData(context: Context): Any?
}
