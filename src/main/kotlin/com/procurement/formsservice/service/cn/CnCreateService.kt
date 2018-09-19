package com.procurement.formsservice.service.cn

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.exception.ei.EiNotContainFsException
import com.procurement.formsservice.exception.query.QueryParameterValidationException
import com.procurement.formsservice.model.cn.create.CnCreateContext
import com.procurement.formsservice.model.cn.create.CnCreateData
import com.procurement.formsservice.model.cn.create.CnCreateParameters
import com.procurement.formsservice.model.cn.create.CnProcuringEntity
import com.procurement.formsservice.repository.MDMRepository
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

interface CnCreateService {
    fun create(queryParameters: CnCreateParameters): Mono<String>
}

@Service
class CnCreateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService,
                          private val mdmRepository: MDMRepository) :
    CnCreateService {
    private val createTemplate = formTemplateService[KindTemplate.CREATE, KindEntity.CN]

    override fun create(queryParameters: CnCreateParameters): Mono<String> = mono {
        val release: CnCreateData.Release =
            publicPointService.getCnCreateData(queryParameters.cpid.value).releases[0]

        validation(queryParameters, release)

        val party: CnCreateData.Release.Party = release.parties[0]
        val buyer = buyer(party = party)

        val procuringEntityUris = procuringEntityUris(queryParameters, buyer)
        val lotUris = lotUris(queryParameters)
        val uris = uris(queryParameters = queryParameters, buyer = buyer, cpvId = release.tender.classification.id)

        val data = CnCreateContext(
            parameters = CnCreateContext.Parameters(
                procuringEntity = queryParameters.procuringEntity.name,
                responsibleContactPerson = queryParameters.responsibleContactPerson.name,
                pmd = queryParameters.pmd.name
            ),
            procuringEntity = CnCreateContext.ProcuringEntity(procuringEntityUris),
            lot = CnCreateContext.Lot(lotUris),
            uris = uris,
            buyer = buyer,
            budget = budget(release)
        )

        formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private suspend fun validation(queryParameters: CnCreateParameters, release: CnCreateData.Release) {
        val lang = queryParameters.lang
        val country = release.parties[0].address.addressDetails.country.id

        val pmds = mdmRepository.pmd(lang, country)
        if (!pmds.contains(queryParameters.pmd.toString()))
            throw QueryParameterValidationException(
                name = CnCreateParameters.PMD.name,
                value = queryParameters.pmd.toString()
            )
    }

    private fun procuringEntityUris(queryParameters: CnCreateParameters,
                                    buyer: CnCreateContext.Buyer?): CnCreateContext.ProcuringEntity.Uris {
        val lang = queryParameters.lang
        val countryId = buyer?.address?.country?.id ?: ""
        val regionId = buyer?.address?.region?.id ?: ""

        return if (procuringEntityIsBuyer(queryParameters)) {
            CnCreateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}/$countryId?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=$countryId",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=$countryId&region=$regionId",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=$countryId"
            )
        } else {
            CnCreateContext.ProcuringEntity.Uris(
                country = "${MDMKind.COUNTRY}?lang=$lang",
                region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
                locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$",
                registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=$lang&country=\$country\$"
            )
        }
    }

    private fun procuringEntityIsBuyer(queryParameters: CnCreateParameters) =
        queryParameters.procuringEntity == CnProcuringEntity.BUYER

    private fun lotUris(queryParameters: CnCreateParameters): CnCreateContext.Lot.Uris {
        val lang = queryParameters.lang
        return CnCreateContext.Lot.Uris(
            country = "${MDMKind.COUNTRY}?lang=$lang",
            region = "${MDMKind.REGION}?lang=$lang&country=\$country\$",
            locality = "${MDMKind.LOCALITY}?lang=$lang&country=\$country\$&region=\$region\$"
        )
    }

    private fun uris(queryParameters: CnCreateParameters,
                     buyer: CnCreateContext.Buyer?,
                     cpvId: String): CnCreateContext.Uris {
        val lang = queryParameters.lang
        val countryId = buyer?.address?.country?.id ?: ""

        return CnCreateContext.Uris(
            unitClass = "${MDMKind.UNIT_CLASS}?lang=$lang",
            unit = "${MDMKind.UNIT}?lang=$lang&unitClass=\$unitClass\$",
            cpv = "${MDMKind.CPV}?lang=$lang&code=$cpvId",
            cpvs = "${MDMKind.CPVS}?lang=$lang",
            pmd = "${MDMKind.PMD}?lang=$lang&country=$countryId"
        )
    }

    private fun buyer(party: CnCreateData.Release.Party): CnCreateContext.Buyer? {
        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality

        return CnCreateContext.Buyer(
            name = party.name,
            address = CnCreateContext.Buyer.Address(
                country = CnCreateContext.Buyer.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = CnCreateContext.Buyer.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = CnCreateContext.Buyer.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                ),
                streetAddress = address.streetAddress,
                postalCode = address.postalCode
            ),
            identifier = party.identifier.let { identifier ->
                CnCreateContext.Buyer.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri

                )
            },
            additionalIdentifiers = party.additionalIdentifiers?.map { additionalIdentifier ->
                CnCreateContext.Buyer.AdditionalIdentifier(
                    scheme = additionalIdentifier.scheme,
                    id = additionalIdentifier.id,
                    legalName = additionalIdentifier.legalName,
                    uri = additionalIdentifier.uri
                )
            },
            contactPoint = party.contactPoint.let { contactPoint ->
                CnCreateContext.Buyer.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            }
        )
    }

    private fun budget(release: CnCreateData.Release): CnCreateContext.Budget =
        release.planning.budget.amount.let { amount ->
            CnCreateContext.Budget(
                amount = CnCreateContext.Budget.Amount(
                currency = amount?.currency ?: throw EiNotContainFsException())
            )
        }
}
