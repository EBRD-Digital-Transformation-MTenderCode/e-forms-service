package com.procurement.formsservice.domain.query.v4

import java.util.*

private fun insensitive(queryParameters: Map<String, List<String>>): Map<String, List<String>> =
    TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER).apply {
        for ((name, values) in queryParameters) {
            put(name.toUpperCase(), values)
        }
    }

fun sensitiveQueryParameters(queryParameters: Map<String, List<String>>) =
    SensitiveQueryParameters(queryParameters)
fun inSensitiveQueryParameters(queryParameters: Map<String, List<String>>) =
    InSensitiveQueryParameters(queryParameters)

abstract class QueryParameters(val queryParameters: Map<String, List<String>>) {
    fun <T : Any> bind(binder: BinderWithoutInnerValidator<T>,
                       default: (() -> T)? = null,
                       validator: ((T) -> Boolean)? = null): T =
        binder.asSingle(queryParameters = queryParameters, default = default, validator = validator)

    fun <T : Any> bind(binder: BinderWithInnerValidator<T>,
                       default: (() -> T)? = null): T =
        binder.asSingle(queryParameters = queryParameters, default = default)

    fun <T : Any> bindSet(binder: BinderWithoutInnerValidator<T>,
                          default: (() -> Set<T>)? = null,
                          validator: ((T) -> Boolean)? = null): Set<T> =
        binder.asSet(queryParameters = queryParameters, default = default, validator = validator)

    fun <T : Any> bindSet(binder: BinderWithInnerValidator<T>,
                          default: (() -> Set<T>)? = null): Set<T> =
        binder.asSet(queryParameters = queryParameters, default = default)

    fun <T : Any> bindList(binder: BinderWithoutInnerValidator<T>,
                           default: (() -> List<T>)? = null,
                           validator: ((T) -> Boolean)? = null): List<T> =
        binder.asList(queryParameters = queryParameters, default = default, validator = validator)

    fun <T : Any> bindList(binder: BinderWithInnerValidator<T>,
                           default: (() -> List<T>)? = null): List<T> =
        binder.asList(queryParameters = queryParameters, default = default)
}

class SensitiveQueryParameters(queryParameters: Map<String, List<String>>) : QueryParameters(queryParameters)

class InSensitiveQueryParameters(queryParameters: Map<String, List<String>>) :
    QueryParameters(insensitive(
        queryParameters))
