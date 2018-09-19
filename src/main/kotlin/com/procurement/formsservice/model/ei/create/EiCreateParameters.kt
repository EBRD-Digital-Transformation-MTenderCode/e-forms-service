package com.procurement.formsservice.model.ei.create

import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.COUNTRY
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

//class QueryParametersService {
//    fun query(queryParameters: Map<String, List<String>>): Map<String, List<String>> =
//        mutableMapOf<String, List<String>>().apply {
//            for ((name, values) in queryParameters) {
//                put(name.toUpperCase(), values)
//            }
//        }
//
//    fun <V : Any> bind(binder: ParameterBinder<V>, default: V? = null): V =
//        if (default == null)
//            binder.toSingle(query)
//        else
//            binder.toSingleOrDefault(query, default)
//
//    fun <V : Any> bindSet(binder: ParameterBinder<V>, default: Set<V>? = null): Set<V> =
//        if (default == null)
//            binder.toSet(query)
//        else
//            binder.toSetOrDefault(query, default)
//
//    fun <V : Any> bindList(binder: ParameterBinder<V>, default: List<V>? = null): List<V> =
//        if (default == null)
//            binder.toList(query)
//        else
//            binder.toListOrDefault(query, default)
//}

//fun <R> binder(name:String, transformer: TypeTransformer<String, R>):Binder<R> {
//
//}

//class Binder<T>(val name: String,
//                val transformer: TypeTransformer<String, T>,
//                val validator: (T) -> Boolean = { true }) {
//
//    fun validation(predicate: (T) -> Boolean) {
//        if (!validator(queryValue))
//            throw QueryParameterValidationException(name, queryValue.toString())
//    }
//
//    private fun transform(queryValue: String): T {
//        return transformer(queryValue)
//            ?: throw QueryParameterTransformationException(name, queryValue, transformer.type.typeName)
//    }
//
//    fun toSingle(queryValues: Map<String, List<String>>, default: T? = null): T {
//        val values = queryValues[name]
//            ?: if (default != null) return default else throw QueryParameterMissingException(name)
//        if (values.size > 1) throw QueryParameterInvalidNumberException(name)
//        val value: T = transform(values[0])
//        validation(value)
//        return value
//    }
//}

//@Service
//class EiCreateParametersService(private val mdmRepository: MDMRepository) {
//    fun fff(lang: String, country: String): Set<String> {
//        return mdmRepository.schemeRegistration(lang = lang, country = country)
//    }
//
//    val IDENTIFIER_SCHEMA = binder(name = "identifierSchema")
//        .transform(StringToStringTransformer)
//        .validation { fff().contains(it) })
//
//    suspend fun create(queryParameters: Map<String, List<String>>): EiCreateParameters {
//        val query = mutableMapOf<String, List<String>>().apply {
//            for ((name, values) in queryParameters) {
//                put(name.toUpperCase(), values)
//            }
//        }
//
//        return EiCreateParameters(
//            lang = LANG.toSingleOrDefault(query, defaultLang),
//            country = COUNTRY.toSingle(query),
//            identifierSchema = IDENTIFIER_SCHEMA.toSingle(query)
//        )
//    }
//}

//data class EiCreateParameters(val lang: String, val country: String, val identifierSchema: String)

//class EiCreateParameters(query: Map<String, List<String>>) {
//    companion object {
//        val IDENTIFIER_SCHEMA = binder(name = "identifierSchema")
//    }
//
//    val lang: String = LANG, defaultLang)
//    val country: String = bind(COUNTRY)
//    val identifierSchema: String = bind(IDENTIFIER_SCHEMA)
//}

//class EiCreateParameters private constructor (queryParameters: Map<String, List<String>>) : QueryParameters(queryParameters) {
//    companion object {
//        val IDENTIFIER_SCHEMA = binder(name = "identifierSchema".toUpperCase())
//    }
//
////    val lang: String = bind(binder = LANG, default = { defaultLang })
////    val country: String = bind(COUNTRY)
////    val identifierSchema: String = bind(IDENTIFIER_SCHEMA)
//
//    val lang: String
//    val country: String
//    val identifierSchema: String
//
//    constructor(queryParameters: Map<String, List<String>>, identifierSchemas: Set<String>): this(queryParameters) {
//        lang = bind(binder = LANG, default = { defaultLang })
//        country = bind(COUNTRY)
//        identifierSchema = bind(binder = IDENTIFIER_SCHEMA, validator = {identifierSchemas.contains(it)})
//    }
//}

/*
class EiCreateParameters private constructor(val lang: String,
                                             val country: String,
                                             val identifierSchema: String) {
    companion object {
        private val IDENTIFIER_SCHEMA = binder<String>(name = "identifierSchema")

        fun create(queryParameters: QueryParameters,
                   mdmRepository: MDMRepository): EiCreateParameters = queryParameters.let {
            val lang: String = it.bind(binder = LANG, default = { defaultLang })
            val country: String = it.bind(COUNTRY)
            val identifierSchema: String = it.bind(binder = IDENTIFIER_SCHEMA,
                validator = { value ->
                    runBlocking {
                        mdmRepository.schemeRegistration(lang, country).contains(value)
                    }
                }
            )

            EiCreateParameters(lang, country, identifierSchema)
        }
    }
}*/

class EiCreateParameters(queryParameters: QueryParameters) {
    companion object {
        val IDENTIFIER_SCHEMA = binder<String>(name = "identifierSchema")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val country: String = queryParameters.bind(COUNTRY)
    val identifierSchema: String = queryParameters.bind(binder = IDENTIFIER_SCHEMA)
}