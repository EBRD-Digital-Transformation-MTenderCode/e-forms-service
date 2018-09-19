package com.procurement.formsservice.domain.query.v4

import com.procurement.formsservice.exception.query.QueryParameterInvalidNumberException
import com.procurement.formsservice.exception.query.QueryParameterMissingException
import com.procurement.formsservice.exception.query.QueryParameterTransformationException
import com.procurement.formsservice.exception.query.QueryParameterValidationException

inline fun <reified T : Any> binder(name: String) =
    BinderWithoutInnerValidator<T>(name, TypeTransformers[name, T::class.java])

fun <T : Any> binder(name: String, transformer: TypeTransformer<T>) =
    BinderWithoutInnerValidator<T>(name, transformer)

inline fun <reified T : Any> binder(name: String, noinline validator: ((T) -> Boolean)?) =
    BinderWithInnerValidator<T>(name, TypeTransformers[name, T::class.java], validator)

fun <T : Any> binder(name: String, transformer: TypeTransformer<T>, validator: ((T) -> Boolean)?) =
    BinderWithInnerValidator<T>(name, transformer, validator)

inline fun <reified R : Enum<R>> transformer(elements: Array<R>): TypeTransformer<R> =
    object : StringToEnumTransformer<R>(elements) {
        override val type: Class<R> = R::class.java
    }

inline fun <reified T> transformer(noinline function: (String) -> T?): TypeTransformer<T> =
    object : TypeTransformer<T> {
        override val type: Class<T> = T::class.java
        override fun invoke(value: String): T? = function(value)
    }

sealed class AbstractBinder<T : Any>(val name: String, private val transformer: TypeTransformer<T>) {
    protected fun getSingleValue(queryParameters: Map<String, List<String>>,
                                 default: (() -> T)? = null,
                                 validator: ((T) -> Boolean)? = null): T {
        val values = queryParameters[name]
        return if (values == null || values.isEmpty())
            getSingleDefault(default, validator)
        else
            buildSingle(values, validator)
    }

    protected fun getValuesAsSet(queryParameters: Map<String, List<String>>,
                                 default: (() -> Set<T>)? = null,
                                 validator: ((T) -> Boolean)? = null): Set<T> {
        val values = queryParameters[name]
        return if (values == null || values.isEmpty())
            getCollectionDefault(default, validator)
        else
            buildSet(values, validator)
    }

    protected fun getValuesAsList(queryParameters: Map<String, List<String>>,
                                  default: (() -> List<T>)? = null,
                                  validator: ((T) -> Boolean)? = null): List<T> {
        val values = queryParameters[name]
        return if (values == null || values.isEmpty())
            getCollectionDefault(default, validator)
        else
            buildList(values, validator)
    }

    private fun getSingleDefault(default: (() -> T)? = null,
                                 validator: ((T) -> Boolean)?): T {
        if (default == null) throw QueryParameterMissingException(name)
        return default().apply { validate(value = this, validator = validator) }
    }

    private fun <C : Collection<T>> getCollectionDefault(default: (() -> C)? = null,
                                                         validator: ((T) -> Boolean)?): C {
        if (default == null) throw QueryParameterMissingException(name)
        return default().apply {
            forEach {
                validate(value = it, validator = validator)
            }
        }
    }

    private fun buildSingle(values: List<String>,
                            validator: ((T) -> Boolean)?): T {
        if (values.size > 1) throw QueryParameterInvalidNumberException(name)
        return values[0].transform().apply { validate(value = this, validator = validator) }
    }

    private fun buildSet(values: List<String>,
                         validator: ((T) -> Boolean)?): Set<T> {
        return mutableSetOf<T>().apply {
            values.forEach {
                val value = it.transform()
                validate(value = value, validator = validator)
                this.add(value)
            }
        }
    }

    private fun buildList(values: List<String>,
                          validator: ((T) -> Boolean)?): List<T> {
        return mutableListOf<T>().apply {
            values.forEach {
                val value = it.transform()
                validate(value = value, validator = validator)
                this.add(value)
            }
        }
    }

    private fun String.transform(): T =
        transformer(this) ?: throw QueryParameterTransformationException(name, this, transformer.type.typeName)

    private fun validate(value: T,
                         validator: ((T) -> Boolean)?) {
        if (validator != null && !validator.invoke(value))
            throw QueryParameterValidationException(name, value.toString())
    }
}

class BinderWithoutInnerValidator<T : Any>(name: String,
                                           transformer: TypeTransformer<T>) :
    AbstractBinder<T>(name, transformer) {

    fun asSingle(queryParameters: Map<String, List<String>>,
                 default: (() -> T)? = null,
                 validator: ((T) -> Boolean)? = null): T =
        getSingleValue(queryParameters, default, validator)

    fun asSet(queryParameters: Map<String, List<String>>,
              default: (() -> Set<T>)? = null,
              validator: ((T) -> Boolean)? = null): Set<T> =
        getValuesAsSet(queryParameters, default, validator)

    fun asList(queryParameters: Map<String, List<String>>,
               default: (() -> List<T>)? = null,
               validator: ((T) -> Boolean)? = null): List<T> =
        getValuesAsList(queryParameters, default, validator)
}

class BinderWithInnerValidator<T : Any>(name: String,
                                        transformer: TypeTransformer<T>,
                                        private val validator: ((T) -> Boolean)? = null) :
    AbstractBinder<T>(name, transformer) {

    fun asSingle(queryParameters: Map<String, List<String>>,
                 default: (() -> T)? = null): T =
        getSingleValue(queryParameters, default, validator)

    fun asSet(queryParameters: Map<String, List<String>>,
              default: (() -> Set<T>)? = null): Set<T> =
        getValuesAsSet(queryParameters, default, validator)

    fun asList(queryParameters: Map<String, List<String>>,
               default: (() -> List<T>)? = null): List<T> =
        getValuesAsList(queryParameters, default, validator)
}
