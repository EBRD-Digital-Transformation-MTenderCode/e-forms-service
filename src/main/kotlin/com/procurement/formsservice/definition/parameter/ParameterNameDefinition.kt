package com.procurement.formsservice.definition.parameter

sealed class ParameterNameDefinition(val value: String) {
    private val equalsValue: String = value.toUpperCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ParameterNameDefinition
        if (equalsValue != other.equalsValue) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class StringParameterNameDefinition(value: String)
    : ParameterNameDefinition(value = value)

class BooleanParameterNameDefinition(value: String)
    : ParameterNameDefinition(value = value)

class IntegerParameterNameDefinition(value: String)
    : ParameterNameDefinition(value = value)

class NumberParameterNameDefinition(value: String)
    : ParameterNameDefinition(value = value)
