package com.procurement.formsservice.service.fs

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.model.fs.Role
import com.procurement.formsservice.model.fs.update.FsUpdateContext
import com.procurement.formsservice.model.fs.update.FsUpdateEiData
import com.procurement.formsservice.model.fs.update.FsUpdateFsData
import com.procurement.formsservice.model.fs.update.FsUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface FsUpdateService {
    fun update(queryParameters: FsUpdateParameters): String
}

@Service
class FsUpdateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService) : FsUpdateService {

    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.FS]

    override fun update(queryParameters: FsUpdateParameters): String {
        val ocid = queryParameters.ocid.value
        val cpid = queryParameters.ocid.toCPID().value

        val eiRelease: FsUpdateEiData.Release = publicPointService.getFsUpdateEiData(cpid).releases[0]

        val fsRelease: FsUpdateFsData.Release = publicPointService.getFsUpdateFsData(cpid, ocid).releases[0]

        val data = FsUpdateContext(
            parameters = FsUpdateContext.Parameters(
                ocid = ocid
            ),
            ei = ei(release = eiRelease),
            funder = funder(queryParameters = queryParameters, release = fsRelease),
            payer = payer(queryParameters = queryParameters, release = fsRelease),
            budget = budget(fsRelease),
            uris = uris(queryParameters),
            statusFS = fsRelease.tender.status
        )

        return formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun ei(release: FsUpdateEiData.Release): FsUpdateContext.EI {
        val tender = release.tender
        val classification = tender.classification
        val period = release.planning.budget.period

        return FsUpdateContext.EI(
            ocid = release.ocid,
            title = tender.title,
            description = tender.description,
            budgetPeriod = FsUpdateContext.EI.BudgetPeriod(
                startDate = period.startDate,
                endDate = period.endDate
            ),
            classification = FsUpdateContext.EI.Classification(
                scheme = classification.scheme,
                id = classification.id,
                description = classification.description
            )
        )
    }

    private fun budget(release: FsUpdateFsData.Release): FsUpdateContext.Budget {
        val planning = release.planning
        val budget = planning.budget
        val amount = budget.amount
        val period = budget.period

        return FsUpdateContext.Budget(
            rationale = planning.rationale,
            budgetDetails = planning.budget.let {
                if (it.description != null || it.id != null)
                    FsUpdateContext.Budget.BudgetDetails(
                        description = it.description,
                        id = it.id
                    )
                else
                    null
            },
            budgetAmount = FsUpdateContext.Budget.BudgetAmount(
                amount = amount.amount,
                currency = amount.currency
            ),
            budgetPeriod = FsUpdateContext.Budget.BudgetPeriod(
                startDate = period.startDate,
                endDate = period.endDate
            ),
            budgetProject = FsUpdateContext.Budget.BudgetProject(
                project = budget.project,
                projectID = budget.projectID,
                uri = budget.uri,
                euFunded = euFunded(planning)
            )

        )
    }

    private fun euFunded(planning: FsUpdateFsData.Release.Planning): FsUpdateContext.Budget.BudgetProject.EUfunded {
        val budget = planning.budget
        return FsUpdateContext.Budget.BudgetProject.EUfunded(
            isEUfunded = budget.isEuropeanUnionFunded,
            projectIdentifier = budget.europeanUnionFunding?.projectIdentifier,
            projectName = budget.europeanUnionFunding?.projectName,
            uri = budget.europeanUnionFunding?.uri
        )
    }

    private fun uris(queryParameters: FsUpdateParameters): FsUpdateContext.Uris {
        val lang = queryParameters.lang
        return FsUpdateContext.Uris(
            currency = "${MDMKind.CURRENCY}?lang=$lang"
        )
    }

    private fun funder(queryParameters: FsUpdateParameters, release: FsUpdateFsData.Release): FsUpdateContext.Funder? =
        getPartyByRole(release, Role.FUNDER)?.let { funder(queryParameters, it) }

    private fun funder(queryParameters: FsUpdateParameters,
                       party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder {
        val lang = queryParameters.lang
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality

        return FsUpdateContext.Funder(
            name = party.name,
            address = FsUpdateContext.Funder.Address(
                country = FsUpdateContext.Funder.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = FsUpdateContext.Funder.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = FsUpdateContext.Funder.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                ),
                streetAddress = address.streetAddress,
                postalCode = address.postalCode
            ),
            identifier = party.identifier.let { identifier ->
                FsUpdateContext.Funder.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            },
            additionalIdentifiers = party.additionalIdentifiers?.map { additionalIdentifier ->
                FsUpdateContext.Funder.AdditionalIdentifier(
                    scheme = additionalIdentifier.scheme,
                    id = additionalIdentifier.id,
                    legalName = additionalIdentifier.legalName,
                    uri = additionalIdentifier.uri
                )
            },
            contactPoint = party.contactPoint.let { contactPoint ->
                FsUpdateContext.Funder.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            },
            uris = FsUpdateContext.Funder.Uris(
                country = "${MDMKind.COUNTRY}/${country.id}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=${country.id}",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=${country.id}&region=${region.id}",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=${country.id}"
            )
        )
    }

    private fun payer(queryParameters: FsUpdateParameters, release: FsUpdateFsData.Release): FsUpdateContext.Payer =
        getPartyByRole(release, Role.PAYER)?.let { payer(queryParameters, it) } ?: throw IllegalArgumentException()

    private fun payer(queryParameters: FsUpdateParameters,
                      party: FsUpdateFsData.Release.Party): FsUpdateContext.Payer {
        val lang = queryParameters.lang
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality

        return FsUpdateContext.Payer(
            name = party.name,
            address = FsUpdateContext.Payer.Address(
                country = FsUpdateContext.Payer.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = FsUpdateContext.Payer.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = FsUpdateContext.Payer.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                ),
                streetAddress = address.streetAddress,
                postalCode = address.postalCode
            ),
            identifier = party.identifier.let { identifier ->
                FsUpdateContext.Payer.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            },
            additionalIdentifiers = party.additionalIdentifiers?.map { additionalIdentifier ->
                FsUpdateContext.Payer.AdditionalIdentifier(
                    scheme = additionalIdentifier.scheme,
                    id = additionalIdentifier.id,
                    legalName = additionalIdentifier.legalName,
                    uri = additionalIdentifier.uri
                )
            },
            contactPoint = party.contactPoint.let { contactPoint ->
                FsUpdateContext.Payer.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            },
            uris = FsUpdateContext.Payer.Uris(
                country = "${MDMKind.COUNTRY}/${country.id}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=${country.id}",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=${country.id}&region=${region.id}",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=${country.id}"
            )
        )
    }
//    private fun funder(queryParameters: FsUpdateParameters,
//                       party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder {
//
//        return FsUpdateContext.Funder(
//            name = party.name,
//            address = funderAddress(party),
//            identifier = funderIdentifier(party),
//            additionalIdentifiers = funderAdditionalIdentifier(party),
//            contactPoint = contactPoint(party),
//            uris = funderUris(queryParameters, party)
//        )
//    }

//    private fun funderUris(queryParameters: FsUpdateParameters,
//                           party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder.Uris {
//        val countryId = party.address.addressDetails.country.id
//        val regionId = party.address.addressDetails.region.id
//        val lang = queryParameters.lang
//
//        return FsUpdateContext.Funder.Uris(
//            country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
//            region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
//            locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
//            registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
//        )
//    }

//    private fun funderAddress(party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder.Address {
//        val address = party.address
//        val addressDetails = address.addressDetails
//        val country = addressDetails.country
//        val region = addressDetails.region
//        val locality = addressDetails.locality
//        return FsUpdateContext.Funder.Address(
//            country = FsUpdateContext.Funder.Address.Country(
//                id = country.id,
//                description = country.description
//            ),
//            region = FsUpdateContext.Funder.Address.Region(
//                id = region.id,
//                description = region.description
//            ),
//            locality = FsUpdateContext.Funder.Address.Locality(
//                scheme = locality.scheme,
//                id = locality.id,
//                description = locality.description
//            ),
//            streetAddress = address.streetAddress,
//            postalCode = address.postalCode
//        )
//    }

//    private fun funderIdentifier(party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder.Identifier =
//        party.identifier.let { identifier ->
//            FsUpdateContext.Funder.Identifier(
//                scheme = identifier.scheme,
//                id = identifier.id,
//                legalName = identifier.legalName,
//                uri = identifier.uri
//            )
//        }

//    private fun funderAdditionalIdentifier(party: FsUpdateFsData.Release.Party): List<FsUpdateContext.Funder.AdditionalIdentifier>? =
//        party.additionalIdentifiers?.map { additionalIdentifier ->
//            FsUpdateContext.Funder.AdditionalIdentifier(
//                scheme = additionalIdentifier.scheme,
//                id = additionalIdentifier.id,
//                legalName = additionalIdentifier.legalName,
//                uri = additionalIdentifier.uri
//            )
//        }

//    private fun contactPoint(party: FsUpdateFsData.Release.Party): FsUpdateContext.Funder.ContactPoint =
//        party.contactPoint.let { contactPoint ->
//            FsUpdateContext.Funder.ContactPoint(
//                name = contactPoint.name,
//                url = contactPoint.url,
//                telephone = contactPoint.telephone,
//                email = contactPoint.email,
//                faxNumber = contactPoint.faxNumber
//            )
//        }

    private fun getPartyByRole(release: FsUpdateFsData.Release, role: Role): FsUpdateFsData.Release.Party? =
        release.parties.firstOrNull {
            it.roles.contains(role)
        }

//
//    private fun Funder(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Funder =
//        FsCreateContext.Funder(uris = funderUris(queryParameters, buyer))
//

//
//    private fun funderIsBuyer(queryParameters: FsCreateParameters) = queryParameters.funder == FsFunder.BUYER
//
//    private fun Payer(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Payer =
//        FsCreateContext.Payer(uris = payerUris(queryParameters, buyer))
//
//    private fun payerUris(queryParameters: FsCreateParameters,
//                          buyer: FsCreateContext.Buyer): FsCreateContext.Payer.Uris {
//        val countryId = buyer.address.country.id
//        val regionId = buyer.address.region.id
//        val lang = queryParameters.lang
//
//        return if (payerIsBuyer(queryParameters)) {
//            FsCreateContext.Payer.Uris(
//                country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
//                region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
//                locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
//                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
//            )
//        } else {
//            FsCreateContext.Payer.Uris(
//                country = "${MDMKind.COUNTRY}?lang=$lang",
//                region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
//                locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
//                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=\$country\$"
//            )
//        }
//    }
//
//    private fun payerIsBuyer(queryParameters: FsCreateParameters) = queryParameters.payer == FsPayer.BUYER
//
//    private fun uris(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Uris {
//        val countryId = buyer.address.country.id
//        val lang = queryParameters.lang
//        val currency = "${MDMKind.CURRENCY}?lang=$lang&country=$countryId"
//        return FsCreateContext.Uris(currency = currency)
//    }
//
//    private fun ei(release: FsCreateData.Release): FsCreateContext.EI {
//        val tender = release.tender
//        val period = release.planning.budget.period
//        val classification = tender.classification
//
//        return FsCreateContext.EI(
//            ocid = release.ocid,
//            title = tender.title,
//            description = tender.description,
//            budgetPeriod = FsCreateContext.EI.BudgetPeriod(
//                startDate = period.startDate,
//                endDate = period.endDate
//            ),
//            classification = FsCreateContext.EI.Classification(
//                scheme = classification.scheme,
//                id = classification.id,
//                description = classification.description
//            )
//        )
//    }
//
//    private fun buyer(party: FsCreateData.Release.Party): FsCreateContext.Buyer =
//        FsCreateContext.Buyer(
//            name = party.name,
//            address = address(party),
//            identifier = identifier(party),
//            additionalIdentifiers = additionalIdentifier(party),
//            contactPoint = contactPoint(party)
//        )
//

//
//    private fun identifier(party: FsCreateData.Release.Party): FsCreateContext.Buyer.Identifier =
//        party.identifier.let { identifier ->
//            FsCreateContext.Buyer.Identifier(
//                scheme = identifier.scheme,
//                id = identifier.id,
//                legalName = identifier.legalName,
//                uri = identifier.uri
//            )
//        }
//
//    private fun additionalIdentifier(party: FsCreateData.Release.Party): List<FsCreateContext.Buyer.AdditionalIdentifier>? =
//        party.additionalIdentifiers?.map { additionalIdentifier ->
//            FsCreateContext.Buyer.AdditionalIdentifier(
//                scheme = additionalIdentifier.scheme,
//                id = additionalIdentifier.id,
//                legalName = additionalIdentifier.legalName,
//                uri = additionalIdentifier.uri
//            )
//        }
//
//    private fun contactPoint(party: FsCreateData.Release.Party): FsCreateContext.Buyer.ContactPoint =
//        party.contactPoint.let { contactPoint ->
//            FsCreateContext.Buyer.ContactPoint(
//                name = contactPoint.name,
//                url = contactPoint.url,
//                telephone = contactPoint.telephone,
//                email = contactPoint.email,
//                faxNumber = contactPoint.faxNumber
//            )
//        }
//
//    private fun budget(release: FsCreateData.Release): FsCreateContext.Budget =
//        release.planning.budget.amount?.let { amount ->
//            FsCreateContext.Budget(amount = FsCreateContext.Budget.Amount(currency = amount.currency))
//        } ?: FsCreateContext.Budget(amount = null)
}
