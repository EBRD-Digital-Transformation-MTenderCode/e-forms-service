package com.procurement.formsservice.definition.parameter

import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.STRING

sealed class ParameterValueDefinition
class StringParameterValueDefinition(val value: STRING) : ParameterValueDefinition() {
    private val equalsValue: STRING = value.toUpperCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StringParameterValueDefinition
        if (equalsValue != other.equalsValue) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class BooleanParameterValueDefinition(val value: BOOLEAN) : ParameterValueDefinition() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BooleanParameterValueDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class IntegerParameterValueDefinition(val value: INTEGER) : ParameterValueDefinition() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IntegerParameterValueDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class NumberParameterValueDefinition(val value: NUMBER) : ParameterValueDefinition() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NumberParameterValueDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
