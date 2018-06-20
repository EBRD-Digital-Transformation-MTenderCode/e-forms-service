package com.procurement.formsservice.json.data.enumerator

sealed class EnumElementDefinition<T> {
    abstract val value: T
    abstract val name: String
    abstract val description: String
}

class StringEnumElementDefinition(override val value: String,
                                  override val name: String = "",
                                  override val description: String = "") : EnumElementDefinition<String>() {
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

class BooleanEnumElementDefinition(override val value: Boolean,
                                   override val name: String = "",
                                   override val description: String = "") : EnumElementDefinition<Boolean>() {
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

class IntegerEnumElementDefinition(override val value: Long,
                                   override val name: String = "",
                                   override val description: String = "") : EnumElementDefinition<Long>() {
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

class NumberEnumElementDefinition(override val value: Float,
                                  override val name: String = "",
                                  override val description: String = "") : EnumElementDefinition<Float>() {
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
