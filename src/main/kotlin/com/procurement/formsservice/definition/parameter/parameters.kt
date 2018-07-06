package com.procurement.formsservice.definition.parameter

import com.procurement.formsservice.domain.ResultPair
import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.exception.ValidationQueryParametersException
import com.procurement.formsservice.json.Parameters
import org.springframework.http.server.reactive.ServerHttpRequest

fun parametersDefinition(builder: ParametersDefinition.() -> Unit) = ParametersDefinition().apply(builder)

class ParametersDefinition {
    private val definitions = mutableSetOf<ParameterDefinition<*, *>>()

    fun getContextParameters(request: ServerHttpRequest): Parameters {
        val queryParameters = getQueryParameters(request)

        val parameters = Parameters.Builder()
        val errors = mutableListOf<ErrorRS.Error>()
        for (definition in definitions) {
            val name = definition.nameDefinition.value

            val values: List<String>? = queryParameters[name.toUpperCase()]
            if (values == null) {
                fillDefault(definition = definition, parameters = parameters, errors = errors)
            } else {
                if (values.size > 1) {
                    errors.add(
                        ErrorRS.Error(
                            code = "request.$name.countValues",
                            description = "The query parameter '$name' has an invalid number of values."
                        )
                    )
                } else {
                    fillContextParameters(
                        definition = definition,
                        queryValues = values,
                        parameters = parameters,
                        errors = errors
                    )
                }
            }
        }

        if (errors.isNotEmpty()) throw ValidationQueryParametersException(errors = errors)

        return parameters.build()
    }

    infix fun StringParameterNameDefinition.to(paramBuilder: StringParameterDefinition.Builder) =
        add(paramBuilder.build(this))

    infix fun BooleanParameterNameDefinition.to(paramBuilder: BooleanParameterDefinition.Builder) =
        add(paramBuilder.build(this))

    infix fun IntegerParameterNameDefinition.to(paramBuilder: IntegerParameterDefinition.Builder) =
        add(paramBuilder.build(this))

    infix fun NumberParameterNameDefinition.to(paramBuilder: NumberParameterDefinition.Builder) =
        add(paramBuilder.build(this))

    fun string(builder: StringParameterDefinition.Builder.() -> Unit = {}) =
        StringParameterDefinition.Builder().apply(builder)

    fun boolean(builder: BooleanParameterDefinition.Builder.() -> Unit = {}) =
        BooleanParameterDefinition.Builder().apply(builder)

    fun integer(builder: IntegerParameterDefinition.Builder.() -> Unit = {}) =
        IntegerParameterDefinition.Builder().apply(builder)

    fun number(builder: NumberParameterDefinition.Builder.() -> Unit = {}) =
        NumberParameterDefinition.Builder().apply(builder)

    private fun getQueryParameters(request: ServerHttpRequest): Map<String, List<String>> =
        mutableMapOf<String, List<String>>().apply {
            for ((name, values) in request.queryParams) {
                put(name.toUpperCase(), values)
            }
        }

    private fun add(param: ParameterDefinition<*, *>) {
        definitions.add(param)
    }

    private inline fun <reified T : ParameterDefinition<*, *>> fillContextParameters(definition: T,
                                                                                     queryValues: List<String>,
                                                                                     parameters: Parameters.Builder,
                                                                                     errors: MutableList<ErrorRS.Error>) {
        when (definition) {
            is StringParameterDefinition -> fillStringContextParameter(definition, queryValues, parameters, errors)
            is BooleanParameterDefinition -> fillBooleanContextParameter(definition, queryValues, parameters, errors)
            is IntegerParameterDefinition -> fillIntegerContextParameter(definition, queryValues, parameters, errors)
            is NumberParameterDefinition -> fillNumberContextParameter(definition, queryValues, parameters, errors)
        }
    }

    private inline fun <reified T : ParameterDefinition<*, *>> fillDefault(definition: T,
                                                                           parameters: Parameters.Builder,
                                                                           errors: MutableList<ErrorRS.Error>) {
        when (definition) {
            is StringParameterDefinition -> fillStringDefault(definition, parameters, errors)
            is BooleanParameterDefinition -> fillBooleanDefault(definition, parameters, errors)
            is IntegerParameterDefinition -> fillIntegerDefault(definition, parameters, errors)
            is NumberParameterDefinition -> fillNumberDefault(definition, parameters, errors)
        }
    }

    private fun fillStringDefault(definition: StringParameterDefinition,
                                  parameters: Parameters.Builder,
                                  errors: MutableList<ErrorRS.Error>) {

        val default = definition.default
        if (default != null)
            parameters[definition.nameDefinition] = default
        else
            checkRequired(definition, errors)
    }

    private fun fillBooleanDefault(definition: BooleanParameterDefinition,
                                   parameters: Parameters.Builder,
                                   errors: MutableList<ErrorRS.Error>) {

        val default = definition.default
        if (default != null)
            parameters[definition.nameDefinition] = default
        else
            checkRequired(definition, errors)
    }

    private fun fillIntegerDefault(definition: IntegerParameterDefinition,
                                   parameters: Parameters.Builder,
                                   errors: MutableList<ErrorRS.Error>) {

        val default = definition.default
        if (default != null)
            parameters[definition.nameDefinition] = default
        else
            checkRequired(definition, errors)
    }

    private fun fillNumberDefault(definition: NumberParameterDefinition,
                                  parameters: Parameters.Builder,
                                  errors: MutableList<ErrorRS.Error>) {

        val default = definition.default
        if (default != null) {
            parameters[definition.nameDefinition] = default
        } else
            checkRequired(definition, errors)
    }

    private fun checkRequired(definition: ParameterDefinition<*, *>, errors: MutableList<ErrorRS.Error>) {
        if (definition.required) {
            val name = definition.nameDefinition.value
            errors.add(
                ErrorRS.Error(
                    code = "request.$name.missing",
                    description = "The query parameter '$name' is missing."
                )
            )
        }
    }

    private fun fillStringContextParameter(definition: StringParameterDefinition,
                                           queryValues: List<String>,
                                           parameters: Parameters.Builder,
                                           errors: MutableList<ErrorRS.Error>) {
        val convertValues = definition.convertValues(queryValues)
        when (convertValues) {
            is ResultPair.Fail -> errors.addAll(convertValues.errors)
            is ResultPair.Success -> {
                val validationErrors = definition.validationValues(convertValues.value)
                errors.addAll(validationErrors)
                if (errors.isEmpty()) parameters[definition.nameDefinition] = convertValues.value[0]
            }
        }
    }

    private fun fillBooleanContextParameter(definition: BooleanParameterDefinition,
                                            queryValues: List<String>,
                                            parameters: Parameters.Builder,
                                            errors: MutableList<ErrorRS.Error>) {
        val convertValues = definition.convertValues(queryValues)
        when (convertValues) {
            is ResultPair.Fail -> errors.addAll(convertValues.errors)
            is ResultPair.Success -> {
                val validationErrors = definition.validationValues(convertValues.value)
                errors.addAll(validationErrors)
                if (errors.isEmpty()) parameters[definition.nameDefinition] = convertValues.value[0]
            }
        }
    }

    private fun fillIntegerContextParameter(definition: IntegerParameterDefinition,
                                            queryValues: List<String>,
                                            parameters: Parameters.Builder,
                                            errors: MutableList<ErrorRS.Error>) {
        val convertValues = definition.convertValues(queryValues)
        when (convertValues) {
            is ResultPair.Fail -> errors.addAll(convertValues.errors)
            is ResultPair.Success -> {
                val validationErrors = definition.validationValues(convertValues.value)
                errors.addAll(validationErrors)
                if (errors.isEmpty()) parameters[definition.nameDefinition] = convertValues.value[0]
            }
        }
    }

    private fun fillNumberContextParameter(definition: NumberParameterDefinition,
                                           queryValues: List<String>,
                                           parameters: Parameters.Builder,
                                           errors: MutableList<ErrorRS.Error>) {
        val convertValues = definition.convertValues(queryValues)
        when (convertValues) {
            is ResultPair.Fail -> errors.addAll(convertValues.errors)
            is ResultPair.Success -> {
                val validationErrors = definition.validationValues(convertValues.value)
                errors.addAll(validationErrors)
                if (errors.isEmpty()) parameters[definition.nameDefinition] = convertValues.value[0]
            }
        }
    }
}
