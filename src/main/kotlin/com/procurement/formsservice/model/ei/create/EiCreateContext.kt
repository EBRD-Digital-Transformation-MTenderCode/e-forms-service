package com.procurement.formsservice.model.ei.create

class EiCreateContext(
    val parameters: Parameters,
    val uris: Uris
) {

    data class Parameters(
        val country: String,
        val registrationScheme: String
    )

    data class Uris(
        val cpv: String, // cpv?lang=langFromRequest
        val country: String, // /country/countryFromRequest?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=countryFromRequest
        val locality: String, // /locality?lang=langFromRequest&country=countryFromRequest&region=$region$
        val registrationScheme: String  // /registration-scheme?lang=langFromRequest&country=countryFromRequest
    )
}
