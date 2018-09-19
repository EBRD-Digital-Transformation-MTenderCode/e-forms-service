package com.procurement.formsservice.model.parameters

import com.procurement.formsservice.domain.query.v4.binder

object CommonQueryParametersBinder {
    private val allowableLanguages = setOf("EN", "RU", "RO")
    const val defaultLang = "RO"
    val LANG = binder<String>(name = "lang", validator = { allowableLanguages.contains(it.toUpperCase()) })

    val COUNTRY = binder<String>(name = "country")
}
