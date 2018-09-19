package com.procurement.formsservice.model.fs.create

class FsCreateContext(
    val parameters: Parameters,
    val funder: Funder,
    val payer: Payer,
    val uris: Uris,
    val ei: EI,
    val buyer: Buyer?,
    val budget: Budget
) {
    data class Parameters(
        val funder: String,
        val payer: String,
        val europeanUnionFunded: Boolean
    )

    data class Funder(val uris: Uris) {
        data class Uris(
            val country: String, // if parameter funder == "buyer" "/country/(Buyer.Address.Country.id)?lang=langFromRequest else "/country?lang=langFromRequest
            val region: String, // if parameter funder == "buyer" "/region?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/region?lang=langFromRequest&country=$country$"
            val locality: String, // if parameter funder == "buyer" "/locality?lang=langFromRequest&country=(Buyer.Address.Country.id)&region=(Buyer.Address.Region.id)" else "/locality?lang=langFromRequest&country=$country$&region=$region$"
            val registrationScheme: String // if parameter funder == "buyer" "/registration-scheme?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/registration-scheme?lang=langFromRequest&country=$country$"
        )
    }
    data class Payer(val uris: Uris) {
        data class Uris(
            val country: String, // if parameter  payer == "buyer" "/country/(Buyer.Address.Country.id)?lang=langFromRequest else "/country?lang=langFromRequest
            val region: String, // if parameter payer == "buyer" "/region?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/region?lang=langFromRequest&country=$country$"
            val locality: String, // if parameter payer == "buyer" "/locality?lang=langFromRequest&country=(Buyer.Address.Country.id)&region=(Buyer.Address.Region.id)" else "/locality?lang=langFromRequest&country=$country$&region=$region$"
            val registrationScheme: String // if parameter payer == "buyer" "/registration-scheme?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/registration-scheme?lang=langFromRequest&country=$country$"
        )
    }

    data class Uris(
        val currency: String  // /currency?lang=langFromRequest&country=(Buyer.Address.Country.id)
    )

    data class EI(
        val ocid: String, // EI.ocid,
        val title: String, // EI.tender.title
        val description: String?, // EI.tender.description
        val budgetPeriod: BudgetPeriod,
        val classification: Classification
    ) {
        data class BudgetPeriod(
            val startDate: String, // EI.planning.budget.period.startDate
            val endDate: String // EI.planning.budget.period.endDate
        )

        data class Classification(
            val scheme: String, // EI.tender.classification.scheme
            val id: String, // EI.tender.classification.id
            val description: String // EI.tender.classification.description
        )
    }

    data class Buyer(
        val name: String, // EI.parties[0].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
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

        data class ContactPoint(
            val name: String, // EI.parties[0].contactPoint.name
            val url: String, // EI.parties[0].contactPoint.url
            val telephone: String, // EI.parties[0].contactPoint.telephone
            val email: String, // EI.parties[0].contactPoint.email
            val faxNumber: String? // EI.parties[0].contactPoint.faxNumber
        )
    }

    data class Budget(
        val amount: Amount? // EI.planning.budget.amount
    ) {
        data class Amount(
            val currency: String // EI.planning.budget.amount.currency
        )
    }
}
