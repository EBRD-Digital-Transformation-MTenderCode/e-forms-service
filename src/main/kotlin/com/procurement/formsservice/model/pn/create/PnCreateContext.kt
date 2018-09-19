package com.procurement.formsservice.model.pn.create

class PnCreateContext(
    val parameters: Parameters,
    val procuringEntity: ProcuringEntity,
    val lot: Lot,
    val uris: Uris,
    val buyer: Buyer?,
    val budget: Budget
) {
    data class Parameters(
        val procuringEntity: String,
        val responsibleContactPerson: String,
        val pmd: String
    )

    data class ProcuringEntity(val uris: Uris) {
        data class Uris(
            val country: String, // if parameter procuringEntity == "buyer" "/country/(Buyer.Address.Country.id)?lang=langFromRequest else "/country?lang=langFromRequest
            val region: String, // if parameter procuringEntity == "buyer" "/region?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/region?lang=langFromRequest&country=$country$"
            val locality: String, // if parameter procuringEntity == "buyer"  "/locality?lang=langFromRequest&country=(Buyer.Address.Country.id)&region=(Buyer.Address.Region.id)" else "/locality?lang=langFromRequest&country=$country$&region=$region$"
            val registrationScheme: String // if parameter procuringEntity == "buyer" "/registration-scheme?lang=langFromRequest&country=(Buyer.Address.Country.id)" else "/registration-scheme?lang=langFromRequest&country=$country$
        )
    }

    data class Lot(val uris: Uris) {
        data class Uris(
            val country: String, // "/country?lang=langFromRequest
            val region: String, // "/region?lang=langFromRequest&country=$country$"
            val locality: String // "/locality?lang=langFromRequest&country=$country$&region=$region$
        )
    }

    data class Uris(
        val unitClass: String, // /unit-class?lang=langFromRequest
        val unit: String, // /unit?lang=langFromRequest&unitClass=$unitClass$
        val cpv: String, // cpv?lang=langFromRequest&code=(EI.tender.classification.id)
        val cpvs: String, // /cpvs?lang=langFromRequest
        val pmd: String // /pmd?lang=langFromRequest&country=(Buyer.Address.Country.id)
    )

    data class Buyer(
        val name: String,  // EI.parties[0].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val contactPoint: ContactPoint?
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
        val amount: Amount // EI.planning.budget.amount, if EI.planning.budget.amount == null -> ERROR 422 None of the sources of financing is associated with this expenditure Items
    ) {
        data class Amount(
            val currency: String // EI.planning.budget.amount.currency
        )
    }
}
