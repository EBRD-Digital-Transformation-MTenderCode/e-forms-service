package com.procurement.formsservice.model.enquiry.create

class EnquiryCreateContext(
    val parameters: Parameters,
    val uris: Uris
) {
    data class Parameters(
        val lotId: String?
    )

    data class Uris(
        val country: String, // /country?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=$country$
        val locality: String, // /locality?lang=langFromRequest&country=$country$&region=$region$
        val registrationScheme: String // /registration-scheme?lang=langFromRequest&country=$country$
    )
}
