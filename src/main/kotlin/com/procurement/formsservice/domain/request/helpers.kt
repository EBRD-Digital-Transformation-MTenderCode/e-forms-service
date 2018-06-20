package com.procurement.formsservice.domain.request

import com.procurement.formsservice.json.Parameters
import org.springframework.util.MultiValueMap

const val DEFAULT_LANGUAGE = "EN"

fun MultiValueMap<String, String>.getLanguage(): String {
    val items = this[Parameters.LANGUAGE] ?: return DEFAULT_LANGUAGE
    if (items.isEmpty()) return DEFAULT_LANGUAGE
    return items[0]
}

fun MultiValueMap<String, String>.getCountry(): String {
    val items = this[Parameters.COUNTRY] ?: return ""
    if (items.isEmpty()) return ""
    return items[0]
}

fun MultiValueMap<String, String>.getIdentifierSchema(): String {
    val items = this[Parameters.IDENTIFIER_SCHEMA] ?: return ""
    if (items.isEmpty()) return ""
    return items[0]
}
