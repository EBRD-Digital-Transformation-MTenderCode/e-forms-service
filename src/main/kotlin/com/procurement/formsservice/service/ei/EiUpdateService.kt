package com.procurement.formsservice.service.ei

import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.model.ei.update.EiUpdateContext
import com.procurement.formsservice.model.ei.update.EiUpdateData
import com.procurement.formsservice.model.ei.update.EiUpdateParameters
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface EiUpdateService {
    fun update(queryParameters: EiUpdateParameters): String
}

@Service
class EiUpdateServiceImpl(private val formTemplateService: FormTemplateService,
                          private val publicPointService: PublicPointService) : EiUpdateService {

    private val updateTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.EI]

    override fun update(queryParameters: EiUpdateParameters): String {
        val release: EiUpdateData.Release =
            publicPointService.getEiUpdateData(queryParameters.cpid.value).releases[0]

        val data = EiUpdateContext(
            parameters = EiUpdateContext.Parameters(
                ocid = queryParameters.cpid.value
            ),
            uris = uris(queryParameters = queryParameters, party = release.parties[0]),
            subject = subject(release),
            buyer = buyer(release)
        )

        return formTemplateService.evaluate(
            template = updateTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun uris(queryParameters: EiUpdateParameters, party: EiUpdateData.Release.Party): EiUpdateContext.Uris {
        return EiUpdateContext.Uris(
            cpv = "${MDMKind.CPV}?lang=${queryParameters.lang}",
            country = "${MDMKind.COUNTRY}/${party.address.addressDetails.country.id}?lang=${queryParameters.lang}",
            region = "${MDMKind.REGION}?lang=${queryParameters.lang}&country=${party.address.addressDetails.country.id}",
            locality = "${MDMKind.LOCALITY}?lang=${queryParameters.lang}&country=${party.address.addressDetails.country.id}&region=${party.address.addressDetails.region.id}",
            registrationScheme = "${MDMKind.REGISTRATION_SCHEME}?lang=${queryParameters.lang}&country=${party.address.addressDetails.country.id}"
        )
    }

    private fun subject(release: EiUpdateData.Release): EiUpdateContext.Subject {
        val tender = release.tender
        val classification = tender.classification
        val planning = release.planning
        val period = planning.budget.period

        return EiUpdateContext.Subject(
            title = tender.title,
            description = tender.description,
            budgetPeriod = EiUpdateContext.BudgetPeriod(
                startDate = period.startDate,
                endDate = period.endDate
            ),
            classification = EiUpdateContext.Classification(
                scheme = classification.scheme,
                id = classification.id,
                description = classification.description,
                title = "${classification.id}-${classification.description}"
            ),
            rationale = planning.rationale
        )
    }

//    private fun budgetPeriod(release: EiUpdateData.Release): EiUpdateContext.BudgetPeriod {
//        val period = release.planning.budget.period
//        return EiUpdateContext.BudgetPeriod(
//            startDate = period.startDate,
//            endDate = period.endDate
//        )
//    }

//    private fun classification(release: EiUpdateData.Release): EiUpdateContext.Classification {
//        val classification = release.tender.classification
//
//        return EiUpdateContext.Classification(
//            scheme = classification.scheme,
//            id = classification.id,
//            description = classification.description,
//            title = "${classification.id}-${classification.description}"
//        )
//    }

    private fun buyer(release: EiUpdateData.Release): EiUpdateContext.Buyer {
        val party = release.parties[0]

        val address = party.address
        val addressDetails = address.addressDetails
        val country = addressDetails.country
        val region = addressDetails.region
        val locality = addressDetails.locality
        val identifier = party.identifier
        val details = party.details
        val contactPoint = party.contactPoint

        return EiUpdateContext.Buyer(
            name = party.name,
            address = EiUpdateContext.Buyer.Address(
                country = EiUpdateContext.Buyer.Address.Country(
                    id = country.id,
                    description = country.description
                ),
                region = EiUpdateContext.Buyer.Address.Region(
                    id = region.id,
                    description = region.description
                ),
                locality = EiUpdateContext.Buyer.Address.Locality(
                    scheme = locality.scheme,
                    id = locality.id,
                    description = locality.description
                ),
                streetAddress = address.streetAddress,
                postalCode = address.postalCode
            ),
            identifier = EiUpdateContext.Buyer.Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            ),
            additionalIdentifiers = party.additionalIdentifiers?.map {
                EiUpdateContext.Buyer.AdditionalIdentifier(
                    scheme = it.scheme,
                    id = it.id,
                    legalName = it.legalName,
                    uri = it.uri
                )
            },
            details = EiUpdateContext.Buyer.Details(
                typeOfBuyer = details.typeOfBuyer,
                mainGeneralActivity = details.mainGeneralActivity,
                mainSectoralActivity = details.mainSectoralActivity
            ),
            contactPoint = EiUpdateContext.Buyer.ContactPoint(
                name = contactPoint.name,
                url = contactPoint.url,
                telephone = contactPoint.telephone,
                email = contactPoint.email,
                faxNumber = contactPoint.faxNumber
            )
        )
    }

//    private fun address(party: EiUpdateData.Release.Party): EiUpdateContext.Buyer.Address {
//        val address = party.address
//        val addressDetails = address.addressDetails
//        return EiUpdateContext.Buyer.Address(
//            country = country(addressDetails.country),
//            region = region(addressDetails.region),
//            locality = locality(addressDetails.locality),
//            streetAddress = address.streetAddress,
//            postalCode = address.postalCode
//        )
//    }

//    private fun country(country: EiUpdateData.Release.Party.Address.AddressDetails.Country): EiUpdateContext.Buyer.Address.Country {
//        return EiUpdateContext.Buyer.Address.Country(
//            id = country.id,
//            description = country.description
//        )
//    }

//    private fun region(region: EiUpdateData.Release.Party.Address.AddressDetails.Region): EiUpdateContext.Buyer.Address.Region {
//        return EiUpdateContext.Buyer.Address.Region(
//            id = region.id,
//            description = region.description
//        )
//    }

//    private fun locality(locality: EiUpdateData.Release.Party.Address.AddressDetails.Locality): EiUpdateContext.Buyer.Address.Locality {
//        return EiUpdateContext.Buyer.Address.Locality(
//            scheme = locality.scheme,
//            id = locality.id,
//            description = locality.description
//        )
//    }

//    private fun identifier(party: EiUpdateData.Release.Party): EiUpdateContext.Buyer.Identifier {
//        val identifier = party.identifier
//        return EiUpdateContext.Buyer.Identifier(
//            scheme = identifier.scheme,
//            id = identifier.id,
//            legalName = identifier.legalName,
//            uri = identifier.uri
//        )
//    }

//    private fun additionalIdentifiers(party: EiUpdateData.Release.Party): List<EiUpdateContext.Buyer.AdditionalIdentifier>? {
//        return party.additionalIdentifiers?.map {
//            EiUpdateContext.Buyer.AdditionalIdentifier(
//                scheme = it.scheme,
//                id = it.id,
//                legalName = it.legalName,
//                uri = it.uri
//            )
//        }
//    }
//
//    private fun details(party: EiUpdateData.Release.Party): EiUpdateContext.Buyer.Details {
//        val details = party.details
//        return EiUpdateContext.Buyer.Details(
//            typeOfBuyer = details.typeOfBuyer,
//            mainGeneralActivity = details.mainGeneralActivity,
//            mainSectoralActivity = details.mainSectoralActivity
//        )
//    }
//
//    private fun contactPoint(party: EiUpdateData.Release.Party): EiUpdateContext.Buyer.ContactPoint {
//        val contactPoint = party.contactPoint
//        return EiUpdateContext.Buyer.ContactPoint(
//            name = contactPoint.name,
//            url = contactPoint.url,
//            telephone = contactPoint.telephone,
//            email = contactPoint.email,
//            faxNumber = contactPoint.faxNumber
//        )
//    }
}
