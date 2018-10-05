package com.procurement.formsservice.service.pn

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.ei.EiNotContainFsException
import com.procurement.formsservice.exception.query.QueryParameterValidationException
import com.procurement.formsservice.model.pn.create.PnCreateContext
import com.procurement.formsservice.model.pn.create.PnCreateData
import com.procurement.formsservice.model.pn.create.PnCreateParameters
import com.procurement.formsservice.model.pn.create.PnProcuringEntity
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface PnCreateService {
    fun create(queryParameters: PnCreateParameters): String
}

@Service
class PnCreateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService,
                          private val mdmRepository: MDMRepository) : PnCreateService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.PN]

    override fun create(queryParameters: PnCreateParameters): String {

        val release: PnCreateData.Release = publicPointService.getPnCreateData(queryParameters.cpid.value).releases[0]

        validation(queryParameters, release)

        val party: PnCreateData.Release.Party = release.parties[0]
        val buyer = buyer(queryParameters = queryParameters, party = party)

        val procuringEntity = procuringEntity(queryParameters = queryParameters, buyer = buyer)
        val cpvId = release.tender.classification.id
        val uris = uris(queryParameters = queryParameters, buyer = buyer, cpvId = cpvId)

        val data = PnCreateContext(
            parameters = PnCreateContext.Parameters(
                procuringEntity = queryParameters.procuringEntity.name,
                responsibleContactPerson = queryParameters.responsibleContactPerson.name,
                pmd = queryParameters.pmd.name
            ),
            procuringEntity = procuringEntity,
            lot = lot(queryParameters),
            uris = uris,
            buyer = buyer,
            budget = budget(release)
        )

        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun validation(queryParameters: PnCreateParameters, release: PnCreateData.Release) {
        val lang = queryParameters.lang
        val country = release.parties[0].address.addressDetails.country.id

        val pmds = mdmRepository.pmd(lang, country)
        if (!pmds.contains(queryParameters.pmd.toString()))
            throw QueryParameterValidationException(
                name = PnCreateParameters.PMD.name,
                value = queryParameters.pmd.toString()
            )
    }

    private fun procuringEntity(queryParameters: PnCreateParameters,
                                buyer: PnCreateContext.Buyer?): PnCreateContext.ProcuringEntity {
        return PnCreateContext.ProcuringEntity(
            uris = procuringEntityUris(queryParameters, buyer)
        )
    }

    private fun procuringEntityUris(queryParameters: PnCreateParameters,
                                    buyer: PnCreateContext.Buyer?): PnCreateContext.ProcuringEntity.Uris {
        val lang = queryParameters.lang

        return if (procuringEntityIsBuyer(queryParameters)) {
            val countryId = buyer!!.address.country.id
            val regionId = buyer.address.region.id

            PnCreateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
            )
        } else {
            PnCreateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=\$country\$"
            )
        }
    }

    private fun procuringEntityIsBuyer(queryParameters: PnCreateParameters) =
        queryParameters.procuringEntity == PnProcuringEntity.BUYER

    private fun lot(queryParameters: PnCreateParameters): PnCreateContext.Lot {
        return PnCreateContext.Lot(
            uris = lotUris(queryParameters)
        )
    }

    private fun lotUris(queryParameters: PnCreateParameters): PnCreateContext.Lot.Uris {
        val lang = queryParameters.lang
        return PnCreateContext.Lot.Uris(
            country = "${MDMKind.COUNTRY}?lang=$lang",
            region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
            locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$"
        )
    }

    fun uris(queryParameters: PnCreateParameters, buyer: PnCreateContext.Buyer?, cpvId: String): PnCreateContext.Uris {
        val countryId = buyer?.address?.country?.id ?: ""
        val lang = queryParameters.lang

        val unitClass = "${MDMKind.UNIT_CLASS}?lang=$lang"
        val unit = "${MDMKind.UNIT}?lang=$lang&unitClass=\$unitClass\$"
        val cpv = "${MDMKind.CPV}?lang=$lang&code=$cpvId"
        val cpvs = "${MDMKind.CPVS}?lang=$lang"
        val pmd = "${MDMKind.PMD}?lang=$lang&country=$countryId"

        return PnCreateContext.Uris(
            unitClass = unitClass,
            unit = unit,
            cpv = cpv,
            cpvs = cpvs,
            pmd = pmd
        )
    }

    fun buyer(queryParameters: PnCreateParameters, party: PnCreateData.Release.Party): PnCreateContext.Buyer? {
        return PnCreateContext.Buyer(
            name = party.name,
            address = address(party),
            identifier = identifier(party),
            additionalIdentifiers = additionalIdentifier(party),
            contactPoint = contactPoint(party)
        )
    }

    fun address(party: PnCreateData.Release.Party): PnCreateContext.Buyer.Address {
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality
        return PnCreateContext.Buyer.Address(
            country = PnCreateContext.Buyer.Address.Country(
                id = country.id,
                description = country.description
            ),
            region = PnCreateContext.Buyer.Address.Region(
                id = region.id,
                description = region.description
            ),
            locality = PnCreateContext.Buyer.Address.Locality(
                scheme = locality.scheme,
                id = locality.id,
                description = locality.description
            ),
            streetAddress = address.streetAddress,
            postalCode = address.postalCode
        )
    }

    fun identifier(party: PnCreateData.Release.Party): PnCreateContext.Buyer.Identifier =
        party.identifier.let { identifier ->
            PnCreateContext.Buyer.Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri

            )
        }

    fun additionalIdentifier(party: PnCreateData.Release.Party): List<PnCreateContext.Buyer.AdditionalIdentifier>? =
        party.additionalIdentifiers?.map { additionalIdentifier ->
            PnCreateContext.Buyer.AdditionalIdentifier(
                scheme = additionalIdentifier.scheme,
                id = additionalIdentifier.id,
                legalName = additionalIdentifier.legalName,
                uri = additionalIdentifier.uri
            )
        }

    fun contactPoint(party: PnCreateData.Release.Party): PnCreateContext.Buyer.ContactPoint =
        party.contactPoint.let { contactPoint ->
            PnCreateContext.Buyer.ContactPoint(
                name = contactPoint.name,
                url = contactPoint.url,
                telephone = contactPoint.telephone,
                email = contactPoint.email,
                faxNumber = contactPoint.faxNumber
            )
        }

    fun budget(release: PnCreateData.Release): PnCreateContext.Budget =
        release.planning.budget.amount.let { amount ->
            PnCreateContext.Budget(amount = PnCreateContext.Budget.Amount(
                currency = amount?.currency ?: throw EiNotContainFsException()))
        }
}
