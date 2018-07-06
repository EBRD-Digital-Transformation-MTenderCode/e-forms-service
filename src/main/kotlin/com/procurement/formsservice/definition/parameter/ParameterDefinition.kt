package com.procurement.formsservice.definition.parameter

import com.procurement.formsservice.domain.ResultPair
import com.procurement.formsservice.domain.response.ErrorRS

sealed class ParameterDefinition<N : ParameterNameDefinition, V : ParameterValueDefinition>(val nameDefinition: N,
                                                                                            val allowableValues: Set<V>,
                                                                                            val required: Boolean,
                                                                                            val default: V?) {

    protected abstract fun convertValue(value: String): V?

    fun convertValues(values: List<String>): ResultPair<List<V>> {
        val errors = mutableListOf<ErrorRS.Error>()
        val result = mutableListOf<V>()

        for (value in values) {
            val convertedValue = convertValue(value)
            if (convertedValue == null) {
                errors.add(
                    ErrorRS.Error(
                        code = "request.${nameDefinition.value}.invalidValue",
                        description = "The query parameter '${nameDefinition.value}' has an invalid value."
                    )
                )
            } else {
                if (errors.isEmpty()) result.add(convertedValue)
            }
        }

        return if (errors.isEmpty()) ResultPair.success(result) else ResultPair.fail(errors)
    }

    fun validationValues(values: List<V>): List<ErrorRS.Error> {
        if (allowableValues.isEmpty()) return emptyList()

        val errors = mutableListOf<ErrorRS.Error>()
        for (value in values) {
            if (!allowableValues.contains(value)) {
                errors.add(
                    ErrorRS.Error(
                        code = "request.${nameDefinition.value}.unknownValue",
                        description = "The query parameter '${nameDefinition.value}' has an unknown value."
                    )
                )
            }
        }

        return errors
    }
}

class StringParameterDefinition private constructor(nameDefinition: StringParameterNameDefinition,
                                                    allowableValues: Set<StringParameterValueDefinition>,
                                                    required: Boolean,
                                                    default: StringParameterValueDefinition?) :
    ParameterDefinition<StringParameterNameDefinition, StringParameterValueDefinition>(
        nameDefinition = nameDefinition,
        allowableValues = allowableValues,
        required = required,
        default = default
    ) {

    override fun convertValue(value: String): StringParameterValueDefinition? = StringParameterValueDefinition(value)

    class Builder {
        var allowableValues: Set<StringParameterValueDefinition> = emptySet()
        var required: Boolean = true
        var default: StringParameterValueDefinition? = null

        fun build(nameDefinition: StringParameterNameDefinition) =
            StringParameterDefinition(
                nameDefinition = nameDefinition,
                allowableValues = allowableValues,
                required = required,
                default = default
            )
    }
}

class BooleanParameterDefinition private constructor(nameDefinition: BooleanParameterNameDefinition,
                                                     required: Boolean,
                                                     default: BooleanParameterValueDefinition?) :
    ParameterDefinition<BooleanParameterNameDefinition, BooleanParameterValueDefinition>(
        nameDefinition = nameDefinition,
        allowableValues = setOf(BooleanParameterValueDefinition(true), BooleanParameterValueDefinition(false)),
        required = required,
        default = default
    ) {

    override fun convertValue(value: String): BooleanParameterValueDefinition? =
        when (value.toUpperCase()) {
            "TRUE" -> BooleanParameterValueDefinition(true)
            "FALSE" -> BooleanParameterValueDefinition(false)
            else -> null
        }

    class Builder {
        var required: Boolean = true
        var default: BooleanParameterValueDefinition? = null

        fun build(nameDefinition: BooleanParameterNameDefinition) =
            BooleanParameterDefinition(
                nameDefinition = nameDefinition,
                required = required,
                default = default
            )
    }
}

class IntegerParameterDefinition private constructor(nameDefinition: IntegerParameterNameDefinition,
                                                     allowableValues: Set<IntegerParameterValueDefinition>,
                                                     required: Boolean,
                                                     default: IntegerParameterValueDefinition?) :
    ParameterDefinition<IntegerParameterNameDefinition, IntegerParameterValueDefinition>(
        nameDefinition = nameDefinition,
        allowableValues = allowableValues,
        required = required,
        default = default
    ) {

    override fun convertValue(value: String): IntegerParameterValueDefinition? =
        value.toLongOrNull()
            ?.let { IntegerParameterValueDefinition(it) }

    class Builder {
        var allowableValues: Set<IntegerParameterValueDefinition> = emptySet()
        var required: Boolean = true
        var default: IntegerParameterValueDefinition? = null

        fun build(nameDefinition: IntegerParameterNameDefinition) =
            IntegerParameterDefinition(
                nameDefinition = nameDefinition,
                allowableValues = allowableValues,
                required = required,
                default = default
            )
    }
}

class NumberParameterDefinition private constructor(nameDefinition: NumberParameterNameDefinition,
                                                    allowableValues: Set<NumberParameterValueDefinition>,
                                                    required: Boolean,
                                                    default: NumberParameterValueDefinition?) :
    ParameterDefinition<NumberParameterNameDefinition, NumberParameterValueDefinition>(
        nameDefinition = nameDefinition,
        allowableValues = allowableValues,
        required = required,
        default = default
    ) {

    override fun convertValue(value: String): NumberParameterValueDefinition? =
        value.toDoubleOrNull()?.let {
            NumberParameterValueDefinition(it)
        }

    class Builder {
        var allowableValues: Set<NumberParameterValueDefinition> = emptySet()
        var required: Boolean = true
        var default: NumberParameterValueDefinition? = null

        fun build(nameDefinition: NumberParameterNameDefinition) =
            NumberParameterDefinition(
                nameDefinition = nameDefinition,
                allowableValues = allowableValues,
                required = required,
                default = default
            )
    }
}
