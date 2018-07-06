package com.procurement.formsservice.json

import com.procurement.formsservice.definition.parameter.BooleanParameterNameDefinition
import com.procurement.formsservice.definition.parameter.BooleanParameterValueDefinition
import com.procurement.formsservice.definition.parameter.IntegerParameterNameDefinition
import com.procurement.formsservice.definition.parameter.IntegerParameterValueDefinition
import com.procurement.formsservice.definition.parameter.NumberParameterNameDefinition
import com.procurement.formsservice.definition.parameter.NumberParameterValueDefinition
import com.procurement.formsservice.definition.parameter.StringParameterNameDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.domain.response.ErrorRS
import com.procurement.formsservice.json.exception.NoSuchParameter
import com.procurement.formsservice.json.exception.ParameterCastException
import com.procurement.formsservice.json.path.EmptyPublicData
import com.procurement.formsservice.json.path.PublicData

fun context(repository: MDMRepository,
            parameters: Parameters,
            builder: Context.Builder.() -> Unit = {}) =
    Context.Builder(repository = repository, parameters = parameters).apply(builder).build()

class Context private constructor(val parameters: Parameters,
                                  val repository: MDMRepository,
                                  val publicData: PublicData) {

    class Builder(val repository: MDMRepository,
                  val parameters: Parameters) {

        var publicData: PublicData = EmptyPublicData

        fun build(): Context = Context(
            parameters = parameters,
            repository = repository,
            publicData = publicData
        )
    }
}

fun parameters(builder: Parameters.Builder.() -> Unit) = Parameters.Builder().apply(builder).build()

class Parameters private constructor(private val parameters: Map<String, Any?>) {
    operator fun get(nameDefinition: StringParameterNameDefinition): StringParameterValueDefinition =
        parameters[nameDefinition.value]
            ?.let { asString(nameDefinition.value, it) }
            ?: throw noSuchParameter(nameDefinition.value)

    operator fun get(nameDefinition: BooleanParameterNameDefinition): BooleanParameterValueDefinition =
        parameters[nameDefinition.value]
            ?.let { asBoolean(nameDefinition.value, it) }
            ?: throw noSuchParameter(nameDefinition.value)

    operator fun get(nameDefinition: IntegerParameterNameDefinition): IntegerParameterValueDefinition =
        parameters[nameDefinition.value]
            ?.let { asInteger(nameDefinition.value, it) }
            ?: throw noSuchParameter(nameDefinition.value)

    operator fun get(nameDefinition: NumberParameterNameDefinition): NumberParameterValueDefinition =
        parameters[nameDefinition.value]
            ?.let { asNumber(nameDefinition.value, it) }
            ?: throw noSuchParameter(nameDefinition.value)

    private fun noSuchParameter(name: String) =
        NoSuchParameter(ErrorRS.Error(code = "parameter.$name.noSuch", description = "No such parameter '$name'."))

    fun getOrDefault(nameDefinition: StringParameterNameDefinition,
                     default: StringParameterValueDefinition): StringParameterValueDefinition =
        parameters[nameDefinition.value]?.let { asString(nameDefinition.value, it) } ?: default

    fun getOrDefault(nameDefinition: BooleanParameterNameDefinition,
                     default: BooleanParameterValueDefinition): BooleanParameterValueDefinition =
        parameters[nameDefinition.value]?.let { asBoolean(nameDefinition.value, it) } ?: default

    fun getOrDefault(nameDefinition: IntegerParameterNameDefinition,
                     default: IntegerParameterValueDefinition): IntegerParameterValueDefinition =
        parameters[nameDefinition.value]?.let { asInteger(nameDefinition.value, it) } ?: default

    fun getOrDefault(nameDefinition: NumberParameterNameDefinition,
                     default: NumberParameterValueDefinition): NumberParameterValueDefinition =
        parameters[nameDefinition.value]?.let { asNumber(nameDefinition.value, it) } ?: default

    private fun asString(name: String, value: Any?): StringParameterValueDefinition? =
        value as? StringParameterValueDefinition
            ?: throw ParameterCastException("The parameter '$name' cannot be to cast to type 'STRING'.")

    private fun asBoolean(name: String, value: Any?): BooleanParameterValueDefinition? =
        value as? BooleanParameterValueDefinition
            ?: throw ParameterCastException("The parameter '$name' cannot be to cast to type 'BOOLEAN'.")

    private fun asInteger(name: String, value: Any?): IntegerParameterValueDefinition? =
        value as? IntegerParameterValueDefinition
            ?: throw ParameterCastException("The parameter '$name' cannot be to cast to type 'INTEGER'.")

    private fun asNumber(name: String, value: Any?): NumberParameterValueDefinition? =
        value as? NumberParameterValueDefinition
            ?: throw ParameterCastException("The parameter '$name' cannot be to cast to type 'NUMBER'.")

    class Builder {
        private val parameters = mutableMapOf<String, Any?>()

        operator fun set(nameDefinition: StringParameterNameDefinition, value: StringParameterValueDefinition) {
            parameters[nameDefinition.value] = value
        }

        operator fun set(nameDefinition: BooleanParameterNameDefinition, value: BooleanParameterValueDefinition) {
            parameters[nameDefinition.value] = value
        }

        operator fun set(nameDefinition: IntegerParameterNameDefinition, value: IntegerParameterValueDefinition) {
            parameters[nameDefinition.value] = value
        }

        operator fun set(nameDefinition: NumberParameterNameDefinition, value: NumberParameterValueDefinition) {
            parameters[nameDefinition.value] = value
        }

        fun build() = Parameters(parameters)
    }
}
