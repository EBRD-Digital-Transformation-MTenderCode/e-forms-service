package com.procurement.formsservice.domain.mdm

enum class MDMKind(val segment: String) {
    COUNTRY("country"),
    CPV("cpv"),
    CPVS("cpvs"),
    CURRENCY("currency"),
    LOCALITY("locality"),
    PMD("pmd"),
    REGION("region"),
    REGISTRATION_SCHEME("registration-scheme"),
    UNIT("unit"),
    UNIT_CLASS("unit-class");

    val url = "/$segment"
    override fun toString(): String = url
}