package com.procurement.formsservice.json.data.enumerator

import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.STRING

sealed class EnumElementDefinition<T> {
    abstract val value: T
    abstract val name: String
    abstract val description: String
}

class StringEnumElementDefinition(override val value: STRING,
                                  override val name: String = "",
                                  override val description: String = "") : EnumElementDefinition<STRING>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StringEnumElementDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class BooleanEnumElementDefinition(override val value: BOOLEAN,
                                   override val name: String = "",
                                   override val description: String = "") : EnumElementDefinition<BOOLEAN>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BooleanEnumElementDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class IntegerEnumElementDefinition(override val value: INTEGER,
                                   override val name: String = "",
                                   override val description: String = "") : EnumElementDefinition<INTEGER>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as IntegerEnumElementDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class NumberEnumElementDefinition(override val value: NUMBER,
                                  override val name: String = "",
                                  override val description: String = "") : EnumElementDefinition<NUMBER>() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NumberEnumElementDefinition
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
