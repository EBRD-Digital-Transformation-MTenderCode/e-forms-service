package com.procurement.formsservice.domain.query.v4

import com.procurement.formsservice.domain.StringToCPIDTransformer
import com.procurement.formsservice.domain.StringToOCIDTransformer
import com.procurement.formsservice.exception.query.BinderQueryParameterException

object TypeTransformers {
    private val items = mutableMapOf<Class<*>, TypeTransformer<*>>()

    private fun <T> registration(transformer: TypeTransformer<T>) {
        items[transformer.type] = transformer
    }

    init {
        registration(StringToStringTransformer)
        registration(StringToBooleanTransformer)
        registration(StringToIntegerTransformer)
        registration(StringToLongTransformer)
        registration(StringToFloatTransformer)
        registration(StringToDoubleTransformer)
        registration(StringToOCIDTransformer)
        registration(StringToCPIDTransformer)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(name: String, type: Class<T>) =
        items[type] as? TypeTransformer<T>
            ?: throw BinderQueryParameterException(name, type)
}

interface TypeTransformer<T> : (String) -> T? {
    val type: Class<T>
}

object StringToStringTransformer : TypeTransformer<String> {
    override val type: Class<String> = String::class.java
    override operator fun invoke(value: String): String? = value
}

object StringToBooleanTransformer : TypeTransformer<Boolean> {
    override val type: Class<Boolean> = Boolean::class.java
    override operator fun invoke(value: String): Boolean? = when (value.toUpperCase()) {
        "TRUE" -> true
        "FALSE" -> false
        else -> null
    }
}

object StringToIntegerTransformer : TypeTransformer<Int> {
    override val type: Class<Int> = Int::class.java
    override operator fun invoke(value: String): Int? = value.toIntOrNull()
}

object StringToLongTransformer : TypeTransformer<Long> {
    override val type: Class<Long> = Long::class.java
    override operator fun invoke(value: String): Long? = value.toLongOrNull()
}

object StringToFloatTransformer : TypeTransformer<Float> {
    override val type: Class<Float> = Float::class.java
    override operator fun invoke(value: String): Float? = value.toFloatOrNull()
}

object StringToDoubleTransformer : TypeTransformer<Double> {
    override val type: Class<Double> = Double::class.java
    override operator fun invoke(value: String): Double? = value.toDoubleOrNull()
}

abstract class StringToEnumTransformer<T : Enum<T>>(elements: Array<T>) :
    TypeTransformer<T> {
    private val elements = mutableMapOf<String, T>().apply {
        elements.forEach {
            this[it.name] = it
        }
    }

    override operator fun invoke(value: String): T? = elements[value.toUpperCase()]
}

//interface TypeTransformer<T, R> : (T) -> R? {
//    val type: Class<R>
//}
//
//object StringToStringTransformer : TypeTransformer<String, String> {
//    override val type: Class<String> = String::class.java
//    override operator fun invoke(value: String): String? = value
//}
//
//object StringToBooleanTransformer : TypeTransformer<String, Boolean> {
//    override val type: Class<Boolean> = Boolean::class.java
//    override operator fun invoke(value: String): Boolean? = when (value.toUpperCase()) {
//        "TRUE" -> true
//        "FALSE" -> false
//        else -> null
//    }
//}
//
//object StringToIntegerTransformer : TypeTransformer<String, Int> {
//    override val type: Class<Int> = Int::class.java
//    override operator fun invoke(value: String): Int? = value.toIntOrNull()
//}
//
//object StringToLongTransformer : TypeTransformer<String, Long> {
//    override val type: Class<Long> = Long::class.java
//    override operator fun invoke(value: String): Long? = value.toLongOrNull()
//}
//
//object StringToFloatTransformer : TypeTransformer<String, Float> {
//    override val type: Class<Float> = Float::class.java
//    override operator fun invoke(value: String): Float? = value.toFloatOrNull()
//}
//
//object StringToDoubleTransformer : TypeTransformer<String, Double> {
//    override val type: Class<Double> = Double::class.java
//    override operator fun invoke(value: String): Double? = value.toDoubleOrNull()
//}
//
//abstract class StringToEnumTransformer<T : Enum<T>>(elements: Array<T>) : TypeTransformer<String, T> {
//    private val elements = mutableMapOf<String, T>().apply {
//        elements.forEach {
//            this[it.name] = it
//        }
//    }
//
//    override operator fun invoke(value: String): T? = elements[value.toUpperCase()]
//}
