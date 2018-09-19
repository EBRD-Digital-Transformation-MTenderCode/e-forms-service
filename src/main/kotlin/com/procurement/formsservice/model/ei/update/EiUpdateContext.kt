package com.procurement.formsservice.model.ei.update

class EiUpdateContext(
    val parameters: Parameters,
    val uris: Uris,
    val subject: Subject,
    val buyer: Buyer
) {

    data class Parameters(
        val ocid: String
    )

    data class Uris(
        val cpv: String, // cpv?lang=langFromRequest
        val country: String, // /country/EI.parties[0].address.addressDetails.country.id?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=EI.parties[0].address.addressDetails.country.id
        val locality: String, // /locality?lang=langFromRequest&country=EI.parties[0].address.addressDetails.country.id&region=EI.parties[0].address.addressDetails.region.id
        val registrationScheme: String  // /registration-scheme?lang=langFromRequest&country=EI.parties[0].address.addressDetails.country.id
    )

    data class Subject (
        val title: String, // EI.tender.title
        val description: String?, // EI.tender.description
        val budgetPeriod: BudgetPeriod,
        val classification: Classification,
        val rationale: String?  // EI.planning.rationale
    )

    data class BudgetPeriod (
        val startDate: String, // EI.planning.budget.period.startDate
        val endDate: String // EI.planning.budget.period.endDate
    )

    data class Classification (
        val scheme: String,  // EI.tender.classification.scheme
        val id: String, // EI.tender.classification.id
        val description: String, // EI.tender.classification.description
        val title: String // "EI.tender.classification.id - EI.tender.classification.description"
    )

    data class Buyer(
        val name: String, // EI.parties[0].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val details: Details,
        val contactPoint: ContactPoint
    ) {
        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // EI.parties[0].address.streetAddress
            val postalCode: String? // EI.parties[0].address.postalCode
        ) {
            data class Country(
                val id: String, // EI.parties[0].address.addressDetails.country.id
                val description: String // EI.parties[0].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // EI.parties[0].address.addressDetails.region.id
                val description: String // EI.parties[0].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // EI.parties[0].address.addressDetails.locality.scheme
                val id: String, // EI.parties[0].address.addressDetails.locality.id
                val description: String // EI.parties[0].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // EI.parties[0].identifier.scheme
            val id: String, // EI.parties[0].identifier.id
            val legalName: String, // EI.parties[0].identifier.legalName
            val uri: String? // EI.parties[0].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // EI.parties[0].additionalIdentifier[i].scheme
            val id: String, // EI.parties[0].additionalIdentifier[i].id
            val legalName: String, // EI.parties[0].additionalIdentifier[i].legalName
            val uri: String? // EI.parties[0].additionalIdentifier[i].uri
        )

        data class Details(
            val typeOfBuyer: String, // EI.parties[0].details.typeOfBuyer
            val mainGeneralActivity: String, // EI.parties[0].details.mainGeneralActivity
            val mainSectoralActivity: String // EI.parties[0].details.mainSectoralActivity
        )

        data class ContactPoint(
            val name: String, // EI.parties[0].contactPoint.name
            val url: String, // EI.parties[0].contactPoint.url
            val telephone: String, // EI.parties[0].contactPoint.telephone
            val email: String, // EI.parties[0].contactPoint.email
            val faxNumber: String? // EI.parties[0].contactPoint.faxNumber
        )
    }
}
