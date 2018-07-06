package com.procurement.formsservice.json.data

import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.STRING
import java.util.*

sealed class FieldWrapper<T> {
    protected var value: T? = null

    val isNull: Boolean
        get() = value == null

    val isNotNull: Boolean
        get() = value != null

    fun get(): T = value ?: throw NoSuchElementException("No value present")

    fun set(value: T?) {
        this.value = value
    }

    abstract val isString: Boolean
    abstract val isBoolean: Boolean
    abstract val isInteger: Boolean
    abstract val isNumber: Boolean
}

class StringFieldWrapper : FieldWrapper<STRING>() {
    val isEmpty: Boolean
        get() = value?.isEmpty() ?: true

    val isNotEmpty
        get() = value?.isNotEmpty() ?: false

    val isBlank
        get() = value?.isBlank() ?: true

    val isNotBlank
        get() = value?.isNotBlank() ?: false

    override val isString: Boolean = true
    override val isBoolean: Boolean = false
    override val isInteger: Boolean = false
    override val isNumber: Boolean = false
}

class BooleanFieldWrapper : FieldWrapper<BOOLEAN>() {
    override val isString: Boolean = false
    override val isBoolean: Boolean = true
    override val isInteger: Boolean = false
    override val isNumber: Boolean = false
}

class IntegerFieldWrapper : FieldWrapper<INTEGER>() {
    override val isString: Boolean = false
    override val isBoolean: Boolean = false
    override val isInteger: Boolean = true
    override val isNumber: Boolean = false
}

class NumberFieldWrapper : FieldWrapper<NUMBER>() {
    override val isString: Boolean = false
    override val isBoolean: Boolean = false
    override val isInteger: Boolean = false
    override val isNumber: Boolean = true
}
