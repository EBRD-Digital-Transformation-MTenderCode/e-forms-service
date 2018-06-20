package com.procurement.formsservice.json

import com.procurement.formsservice.json.exception.NoSuchParameter
import com.procurement.formsservice.json.exception.ParameterCastException

fun context(repository: MDMRepository,
            parameters: Parameters,
            builder: Context.Builder.() -> Unit = {}) =
    Context.Builder(repository = repository, parameters = parameters).apply(builder).build()

class Context private constructor(private val mode: LazyThreadSafetyMode,
                                  val parameters: Parameters,
                                  val repository: MDMRepository) {

    class Builder(val mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
                  val repository: MDMRepository,
                  val parameters: Parameters) {

        fun build(): Context = Context(
            mode = mode,
            parameters = parameters,
            repository = repository
        )
    }
}

fun parameters(builder: Parameters.Builder.() -> Unit) = Parameters.Builder().apply(builder).build()

class Parameters private constructor(private val parameters: Map<String, Any?>) {
    companion object {
        const val LANGUAGE = "lang"
        const val COUNTRY = "country"
        const val IDENTIFIER_SCHEMA = "identifierSchema"
    }

    fun getAsString(key: String): String =
        parameters[key]?.let { asString(key, it) } ?: throw NoSuchParameter("No such parameter '$key'.")

    fun getAsBoolean(key: String): Boolean =
        parameters[key]?.let { asBoolean(key, it) } ?: throw NoSuchParameter("No such parameter '$key'.")

    fun getAsInteger(key: String): Long =
        parameters[key]?.let { asInteger(key, it) } ?: throw NoSuchParameter("No such parameter '$key'.")

    fun getAsNumber(key: String): Float =
        parameters[key]?.let { asNumber(key, it) } ?: throw NoSuchParameter("No such parameter '$key'.")

    fun getOrDefault(key: String, default: String): String = parameters[key]?.let { asString(key, it) } ?: default
    fun getOrDefault(key: String, default: Boolean): Boolean = parameters[key]?.let { asBoolean(key, it) } ?: default
    fun getOrDefault(key: String, default: Long): Long = parameters[key]?.let { asInteger(key, it) } ?: default
    fun getOrDefault(key: String, default: Float): Float = parameters[key]?.let { asNumber(key, it) } ?: default

    private fun asString(key: String, value: Any?): String? =
        value as? String ?: throw ParameterCastException("The parameter '$key' cannot be to cast to type 'String'.")

    private fun asBoolean(key: String, value: Any?): Boolean? =
        value as? Boolean ?: throw ParameterCastException("The parameter '$key' cannot be to cast to type 'Boolean'.")

    private fun asInteger(key: String, value: Any?): Long? =
        value as? Long ?: throw ParameterCastException("The parameter '$key' cannot be to cast to type 'Long'.")

    private fun asNumber(key: String, value: Any?): Float? =
        value as? Float ?: throw ParameterCastException("The parameter '$key' cannot be to cast to type 'Float'.")

    class Builder {
        private val parameters = mutableMapOf<String, Any?>()

        operator fun get(key: String) = parameters[key]

        operator fun set(key: String, value: String) {
            parameters[key] = value
        }

        operator fun set(key: String, value: Boolean) {
            parameters[key] = value
        }

        operator fun set(key: String, value: Long) {
            parameters[key] = value
        }

        operator fun set(key: String, value: Float) {
            parameters[key] = value
        }

        fun build() = Parameters(parameters)
    }
}
