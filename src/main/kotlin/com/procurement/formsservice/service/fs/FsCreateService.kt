package com.procurement.formsservice.service.fs

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.query.QueryParameterStateException
import com.procurement.formsservice.model.fs.create.FsCreateContext
import com.procurement.formsservice.model.fs.create.FsCreateData
import com.procurement.formsservice.model.fs.create.FsCreateParameters
import com.procurement.formsservice.model.fs.create.FsFunder
import com.procurement.formsservice.model.fs.create.FsPayer
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface FsCreateService {
    fun create(queryParameters: FsCreateParameters): String
}

@Service
class FsCreateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService) :
    FsCreateService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.FS]

    override fun create(queryParameters: FsCreateParameters): String {
        validateParameters(queryParameters)

        val release: FsCreateData.Release =
            publicPointService.getFsCreateData(queryParameters.cpid.value).releases[0]
        val bayer = buyer(party = release.parties[0])
        val ei = ei(release = release)
        val budget = budget(release = release)
        val funder = Funder(queryParameters = queryParameters, buyer = bayer)
        val payer = Payer(queryParameters = queryParameters, buyer = bayer)
        val uris = uris(queryParameters = queryParameters, buyer = bayer)

        val data = FsCreateContext(
            parameters = FsCreateContext.Parameters(
                funder = queryParameters.funder.name,
                payer = queryParameters.payer.name,
                europeanUnionFunded = queryParameters.isEuropeanUnionFunded
            ),
            funder = funder,
            payer = payer,
            uris = uris,
            ei = ei,
            buyer = bayer,
            budget = budget
        )

        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun Funder(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Funder =
        FsCreateContext.Funder(uris = funderUris(queryParameters, buyer))

    private fun funderUris(queryParameters: FsCreateParameters,
                           buyer: FsCreateContext.Buyer): FsCreateContext.Funder.Uris {
        val countryId = buyer.address.country.id
        val regionId = buyer.address.region.id
        val lang = queryParameters.lang

        return if (funderIsBuyer(queryParameters)) {
            FsCreateContext.Funder.Uris(
                country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
            )
        } else {
            FsCreateContext.Funder.Uris(
                country = "${MDMKind.COUNTRY}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=\$country\$"
            )
        }
    }

    private fun funderIsBuyer(queryParameters: FsCreateParameters) = queryParameters.funder == FsFunder.BUYER

    private fun Payer(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Payer =
        FsCreateContext.Payer(uris = payerUris(queryParameters, buyer))

    private fun payerUris(queryParameters: FsCreateParameters,
                          buyer: FsCreateContext.Buyer): FsCreateContext.Payer.Uris {
        val countryId = buyer.address.country.id
        val regionId = buyer.address.region.id
        val lang = queryParameters.lang

        return if (payerIsBuyer(queryParameters)) {
            FsCreateContext.Payer.Uris(
                country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
            )
        } else {
            FsCreateContext.Payer.Uris(
                country = "${MDMKind.COUNTRY}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=\$country\$"
            )
        }
    }

    private fun payerIsBuyer(queryParameters: FsCreateParameters) = queryParameters.payer == FsPayer.BUYER

    private fun uris(queryParameters: FsCreateParameters, buyer: FsCreateContext.Buyer): FsCreateContext.Uris {
        val countryId = buyer.address.country.id
        val lang = queryParameters.lang
        val currency = "${MDMKind.CURRENCY}?lang=$lang&country=$countryId"
        return FsCreateContext.Uris(currency = currency)
    }

    private fun ei(release: FsCreateData.Release): FsCreateContext.EI {
        val tender = release.tender
        val period = release.planning.budget.period
        val classification = tender.classification

        return FsCreateContext.EI(
            ocid = release.ocid,
            title = tender.title,
            description = tender.description,
            budgetPeriod = FsCreateContext.EI.BudgetPeriod(
                startDate = period.startDate,
                endDate = period.endDate
            ),
            classification = FsCreateContext.EI.Classification(
                scheme = classification.scheme,
                id = classification.id,
                description = classification.description
            )
        )
    }

    private fun buyer(party: FsCreateData.Release.Party): FsCreateContext.Buyer =
        FsCreateContext.Buyer(
            name = party.name,
            address = address(party),
            identifier = identifier(party),
            additionalIdentifiers = additionalIdentifier(party),
            contactPoint = contactPoint(party)
        )

    private fun address(party: FsCreateData.Release.Party): FsCreateContext.Buyer.Address {
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality
        return FsCreateContext.Buyer.Address(
            country = FsCreateContext.Buyer.Address.Country(
                id = country.id,
                description = country.description
            ),
            region = FsCreateContext.Buyer.Address.Region(
                id = region.id,
                description = region.description
            ),
            locality = FsCreateContext.Buyer.Address.Locality(
                scheme = locality.scheme,
                id = locality.id,
                description = locality.description
            ),
            streetAddress = address.streetAddress,
            postalCode = address.postalCode
        )
    }

    private fun identifier(party: FsCreateData.Release.Party): FsCreateContext.Buyer.Identifier =
        party.identifier.let { identifier ->
            FsCreateContext.Buyer.Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        }

    private fun additionalIdentifier(party: FsCreateData.Release.Party): List<FsCreateContext.Buyer.AdditionalIdentifier>? =
        party.additionalIdentifiers?.map { additionalIdentifier ->
            FsCreateContext.Buyer.AdditionalIdentifier(
                scheme = additionalIdentifier.scheme,
                id = additionalIdentifier.id,
                legalName = additionalIdentifier.legalName,
                uri = additionalIdentifier.uri
            )
        }

    private fun contactPoint(party: FsCreateData.Release.Party): FsCreateContext.Buyer.ContactPoint =
        party.contactPoint.let { contactPoint ->
            FsCreateContext.Buyer.ContactPoint(
                name = contactPoint.name,
                url = contactPoint.url,
                telephone = contactPoint.telephone,
                email = contactPoint.email,
                faxNumber = contactPoint.faxNumber
            )
        }

    private fun budget(release: FsCreateData.Release): FsCreateContext.Budget =
        release.planning.budget.amount?.let { amount ->
            FsCreateContext.Budget(amount = FsCreateContext.Budget.Amount(currency = amount.currency))
        } ?: FsCreateContext.Budget(amount = null)

    private fun validateParameters(queryParameters: FsCreateParameters) {
        if (queryParameters.payer == FsPayer.FUNDER && queryParameters.funder != FsFunder.DONOR)
            throw QueryParameterStateException(
                name = "funder",
                msg = "Invalid value of the parameter 'funder' (${queryParameters.funder}) when the parameter 'payer' is 'FUNDER'"
            )
    }
}
