package com.procurement.formsservice.model.fs.update

class FsUpdateContext(
    val ei: EI,
    val parameters: Parameters,
    val funder: Funder?,
    val payer: Payer,
    val budget: Budget,
    val uris: Uris
) {
    data class Parameters(
        val ocid: String
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

    data class Funder(
        val name: String, // FS.parties[role="funder"].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val contactPoint: ContactPoint,
        val uris: Uris
    ) {
        data class Uris(
            val country: String, // /country/FS.parties[role="funder"].address.addressDetails.country.id?lang=langFromRequest
            val region: String, // /region?lang=langFromRequest&country=FS.parties[role="funder"].address.addressDetails.country.id
            val locality: String, // /locality?lang=langFromRequest&country=FS.parties[role="funder"].address.addressDetails.country.id&region=FS.parties[role="funder"].address.addressDetails.region.id
            val registrationScheme: String // /registration-scheme?lang=langFromRequest&country=FS.parties[role="funder"].address.addressDetails.country.id?
        )

        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // FS.parties[role="funder"].address.streetAddress
            val postalCode: String? // FS.parties[role="funder"].address.postalCode
        ) {
            data class Country(
                val id: String, // FS.parties[role="funder"].address.addressDetails.country.id
                val description: String // FS.parties[role="funder"].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // FS.parties[role="funder"].address.addressDetails.region.id
                val description: String // FS.parties[role="funder"].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // FS.parties[role="funder"].address.addressDetails.locality.scheme
                val id: String, // FS.parties[role="funder"].address.addressDetails.locality.id
                val description: String // FS.parties[role="funder"].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // FS.parties[role="funder"].identifier.scheme
            val id: String, // FS.parties[role="funder"].identifier.id
            val legalName: String, // FS.parties[role="funder"].identifier.legalName
            val uri: String? // FS.parties[role="funder"].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // FS.parties[role="funder"].additionalIdentifier[i].scheme
            val id: String, // FS.parties[role="funder"].additionalIdentifier[i].id
            val legalName: String, // FS.parties[role="funder"].additionalIdentifier[i].legalName
            val uri: String? // FS.parties[role="funder"].additionalIdentifier[i].uri
        )

        data class ContactPoint(
            val name: String, // FS.parties[role="funder"].contactPoint.name
            val url: String, // FS.parties[role="funder"].contactPoint.url
            val telephone: String, // FS.parties[role="funder"].contactPoint.telephone
            val email: String, // FS.parties[role="funder"].contactPoint.email
            val faxNumber: String? // FS.parties[role="funder"].contactPoint.faxNumber
        )
    }

    data class Payer(
        val name: String, // FS.parties[role="payer"].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>?,
        val contactPoint: ContactPoint,
        val uris: Uris
    ) {
        data class Uris(
            val country: String, // /country/FS.parties[role="payer"].address.addressDetails.country.id?lang=langFromRequest
            val region: String, // /region?lang=langFromRequest&country=FS.parties[role="payer"].address.addressDetails.country.id
            val locality: String, // /locality?lang=langFromRequest&country=FS.parties[role="payer"].address.addressDetails.country.id&region=FS.parties[role="payer"].address.addressDetails.region.id
            val registrationScheme: String // /registration-scheme?lang=langFromRequest&country=FS.parties[role="funder"].address.addressDetails.country.id?
        )

        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // FS.parties[role="payer"].address.streetAddress
            val postalCode: String? // FS.parties[role="payer"].address.postalCode
        ) {
            data class Country(
                val id: String, // FS.parties[role="payer"].address.addressDetails.country.id
                val description: String // FS.parties[role="payer"].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // FS.parties[role="payer"].address.addressDetails.region.id
                val description: String // FS.parties[role="payer"].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // FS.parties[role="payer"].address.addressDetails.locality.scheme
                val id: String, // FS.parties[role="payer"].address.addressDetails.locality.id
                val description: String // FS.parties[role="payer"].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // FS.parties[role="payer"].identifier.scheme
            val id: String, // FS.parties[role="payer"].identifier.id
            val legalName: String, // FS.parties[role="payer"].identifier.legalName
            val uri: String? // FS.parties[role="payer"].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // FS.parties[role="payer"].additionalIdentifier[i].scheme
            val id: String, // FS.parties[role="payer"].additionalIdentifier[i].id
            val legalName: String, // FS.parties[role="payer"].additionalIdentifier[i].legalName
            val uri: String? // FS.parties[role="payer"].additionalIdentifier[i].uri
        )

        data class ContactPoint(
            val name: String, // FS.parties[role="payer"].contactPoint.name
            val url: String, // FS.parties[role="payer"].contactPoint.url
            val telephone: String, // FS.parties[role="payer"].contactPoint.telephone
            val email: String, // FS.parties[role="payer"].contactPoint.email
            val faxNumber: String? // FS.parties[role="payer"].contactPoint.faxNumber
        )
    }

    data class Uris(
        val currency: String  // /currency?lang=langFromRequest&country=FS.planning.budget.amount.currency
    )

    data class Budget(
        val rationale: String?, // FS.planning.rationale
        val budgetDetails: BudgetDetails?,
        val budgetAmount: BudgetAmount,
        val budgetPeriod: BudgetPeriod,
        val budgetProject: BudgetProject
    ) {
        data class BudgetDetails(
            val description: String?, // FS.planning.budget.description
            val id: String? // FS.planning.budget.id
        )

        data class BudgetAmount(
            val amount: Double, // FS.planning.budget.amount.amount
            val currency: String // FS.planning.budget.amount.currency
        )

        data class BudgetPeriod(
            val startDate: String, // FS.planning.budget.period.startDate
            val endDate: String // FS.planning.budget.period.endDate
        )

        data class BudgetProject(
            val project: String?, // FS.planning.budget.project
            val projectID: String?, // FS.planning.budget.projectID
            val uri: String?, // FS.planning.budget.uri
            val euFunded: EUfunded
        ) {
            data class EUfunded(
                val isEUfunded: Boolean, // FS.planning.budget.isEuropeanUnionFunded
                val projectIdentifier: String?, // FS.planning.budget.europeanUnionFunding.projectIdentifier
                val projectName: String?, // FS.planning.budget.europeanUnionFunding.projectName
                val uri: String? // FS.planning.budget.europeanUnionFunding.uri
            )
        }
    }
}
