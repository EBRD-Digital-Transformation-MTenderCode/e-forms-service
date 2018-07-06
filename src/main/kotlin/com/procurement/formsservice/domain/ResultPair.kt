package com.procurement.formsservice.domain

import com.procurement.formsservice.domain.response.ErrorRS
import java.util.*

sealed class ResultPair<S> {
    abstract val isEmpty: Boolean

    class Success<S>(private val _value: S? = null) : ResultPair<S>() {
        val value: S
            get() = _value ?: throw NoSuchElementException("")

        override val isEmpty: Boolean
            get() = _value == null
    }

    class Fail<S>(val errors: List<ErrorRS.Error>) : ResultPair<S>() {
        override val isEmpty: Boolean
            get() = errors.isEmpty()
    }

    companion object {
        fun <S> success(value: S): ResultPair<S> = ResultPair.Success(value)
        fun <S> fail(errors: List<ErrorRS.Error>): ResultPair<S> = ResultPair.Fail(errors)
        fun <S> empty(): ResultPair<S> = ResultPair.Success()
    }
}
