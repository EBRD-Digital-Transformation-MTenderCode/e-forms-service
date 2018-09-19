package com.procurement.formsservice.exception.query

sealed class QueryParameterException(val name: String, val msg: String) : RuntimeException(msg)

class QueryParameterMissingException(
    name: String,
    msg: String = "The query parameter '$name' is missing.")
    : QueryParameterException(name = name, msg = msg)

//class QueryParameterFiltrationException(
//    name: String,
//    msg: String = "Error filtration the values of the query parameter '$name'. No valid values.")
//    : QueryParameterException(name = name, msg = msg)

class QueryParameterTransformationException(
    name: String,
    value: String,
    type: String,
    msg: String = "Error transformation the query parameter '$name' with value '$value' to type '$type'.")
    : QueryParameterException(name = name, msg = msg)

class QueryParameterValidationException(
    name: String,
    value: String,
    msg: String = "Error validation the query parameter '$name'. The parameter contains an invalid value of '$value'.")
    : QueryParameterException(name = name, msg = msg)

class QueryParameterInvalidNumberException(name: String)
    : QueryParameterException(name = name, msg = "The query parameter '$name' has an invalid number of values.")

class QueryParameterStateException(name: String, msg: String)
    : QueryParameterException(name = name, msg = msg)
