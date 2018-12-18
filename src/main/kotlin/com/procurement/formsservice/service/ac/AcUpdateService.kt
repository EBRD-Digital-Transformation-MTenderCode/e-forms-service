package com.procurement.formsservice.service.ac

import com.procurement.formsservice.model.ac.update.AwardContractUpdateContext
import com.procurement.formsservice.model.ac.update.AwardContractUpdateData
import com.procurement.formsservice.model.ac.update.AwardContractUpdateParameters
import com.procurement.formsservice.model.ac.update.MSForAwardContractUpdateData
import com.procurement.formsservice.service.FormTemplateService
import com.procurement.formsservice.service.KindEntity
import com.procurement.formsservice.service.KindTemplate
import com.procurement.formsservice.service.PublicPointService
import org.springframework.stereotype.Service
import java.util.*

interface AcUpdateService {
    fun update(queryParameters: AwardContractUpdateParameters): String
}

@Service
class AcUpdateServiceImpl(
    private val formTemplateService: FormTemplateService,
    private val publicPointService: PublicPointService) : AcUpdateService {

    private val createTemplate = formTemplateService[KindTemplate.UPDATE, KindEntity.AWARD_CONTRACT]

    override fun update(queryParameters: AwardContractUpdateParameters): String {

        val cpid = queryParameters.ocid.toCPID().value
        val ocid = queryParameters.ocid.value
        val lang = queryParameters.lang

        val acRelease = publicPointService.getAwardContractUpdateData(cpid = cpid, ocid = ocid).releases[0]
        val msRelease = publicPointService.getMSForAwardContractUpdateData(cpid = cpid).releases[0]

        val data = getDate(ocid = ocid, lang = lang, acRelease = acRelease, msRelease = msRelease)

        return formTemplateService.evaluate(
            template = createTemplate,
            context = mapOf("context" to data),
            locale = Locale(queryParameters.lang)
        )
    }

    private fun getDate(ocid: String,
                        lang: String,
                        acRelease: AwardContractUpdateData.Release,
                        msRelease: MSForAwardContractUpdateData.Release): AwardContractUpdateContext {
        val contract = acRelease.contracts[0]
        val planning = acRelease.planning
        val award = acRelease.awards[0]

        val acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party? =
            partyByRole(role = "buyer", parties = acRelease.parties)
        val msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party =
            partyByRole(role = "buyer", parties = msRelease.parties)!!

        val acPartiesWithRoleSupplier: List<AwardContractUpdateData.Release.Party> =
            partiesByRole(role = "supplier", parties = acRelease.parties)

        return AwardContractUpdateContext(
            parameters = AwardContractUpdateContext.Parameters(
                ocid = ocid
            ),
            lotId = award.relatedLots[0],
            contract = getContract(contract),
            award = getAward(award),
            terms = getTerms(contract),
            buyer = getBuyer(acPartyWithRoleBuyer = acPartyWithRoleBuyer, msPartyWithRoleBuyer = msPartyWithRoleBuyer),
            buyerUris = getBuyerUris(acPartyWithRoleBuyer = acPartyWithRoleBuyer,
                                     msPartyWithRoleBuyer = msPartyWithRoleBuyer,
                                     lang = lang),
            suppliers = getSuppliers(acPartiesWithRoleSupplier = acPartiesWithRoleSupplier),
            supplierUris = getSupplierUris(acPartiesWithRoleSupplier = acPartiesWithRoleSupplier, lang = lang),
            milestones = getMilestones(contract),
            transactions = getTransactions(planning),
            budget = getBudget(planning),
            itemUris = getItemUris(acPartyWithRoleBuyer = acPartyWithRoleBuyer,
                                   msPartyWithRoleBuyer = msPartyWithRoleBuyer,
                                   lang = lang),
            currency = award.value.currency
        )
    }

    private fun getContract(contract: AwardContractUpdateData.Release.Contract): AwardContractUpdateContext.Contract {
        return AwardContractUpdateContext.Contract(
            title = contract.title,
            description = contract.description,
            period = contract.period?.let { period ->
                AwardContractUpdateContext.Contract.Period(
                    startDate = period.startDate,
                    endDate = period.endDate
                )
            },
            documents = contract.documents?.map { document ->
                AwardContractUpdateContext.Contract.Document(
                    id = document.id,
                    type = document.documentType,
                    title = document.title,
                    description = document.description,
                    relatedLot = document.relatedLots[0],
                    confirmationRequest = getConfirmationRequest(document.id, contract.confirmationRequests)
                )
            } ?: emptyList()

        )
    }

    private fun getConfirmationRequest(documentId: String,
                                       confirmationRequests: List<AwardContractUpdateData.Release.Contract.ConfirmationRequest>?): AwardContractUpdateContext.Contract.Document.ConfirmationRequest? {
        return confirmationRequests
            ?.firstOrNull { it.relatedItem == documentId }
            ?.let { confirmationRequest ->
                AwardContractUpdateContext.Contract.Document.ConfirmationRequest(
                    id = confirmationRequest.id,
                    relatedItem = confirmationRequest.relatedItem,
                    source = confirmationRequest.source
                )
            }
    }

    private fun getAward(award: AwardContractUpdateData.Release.Award): AwardContractUpdateContext.Award {
        return AwardContractUpdateContext.Award(
            id = award.id,
            items = award.items.map { item ->
                AwardContractUpdateContext.Award.Item(
                    id = item.id,
                    quantity = item.quantity.toDouble(),
                    value = item.unit.value?.let { value ->
                        AwardContractUpdateContext.Award.Item.Value(
                            amount = value.amount.toDouble(),
                            amountNet = value.amountNet.toDouble(),
                            valueAddedTaxIncluded = value.valueAddedTaxIncluded
                        )
                    },
                    deliveryAddress = item.deliveryAddress?.let { deliveryAddress ->
                        AwardContractUpdateContext.Award.Item.DeliveryAddress(
                            streetAddress = deliveryAddress.streetAddress,
                            postalCode = deliveryAddress.postalCode,
                            country = deliveryAddress.addressDetails.country.let { country ->
                                AwardContractUpdateContext.Award.Item.DeliveryAddress.Country(
                                    id = country.id,
                                    description = country.description
                                )
                            },
                            region = deliveryAddress.addressDetails.region.let { region ->
                                AwardContractUpdateContext.Award.Item.DeliveryAddress.Region(
                                    id = region.id,
                                    description = region.description
                                )
                            },
                            locality = deliveryAddress.addressDetails.locality.let { locality ->
                                AwardContractUpdateContext.Award.Item.DeliveryAddress.Locality(
                                    scheme = locality.scheme,
                                    id = locality.id,
                                    description = locality.description
                                )
                            }

                        )
                    }
                )
            },
            value = award.value.let { value ->
                AwardContractUpdateContext.Award.Value(
                    amount = value.amount.toDouble(),
                    amountNet = value.amountNet?.toDouble(),
                    valueAddedTaxIncluded = value.valueAddedTaxIncluded
                )
            },
            documents = award.documents?.map { document ->
                AwardContractUpdateContext.Award.Document(
                    id = document.id,
                    type = document.documentType,
                    title = document.title,
                    description = document.description,
                    relatedLot = document.relatedLots?.get(0)
                )
            } ?: emptyList()
        )
    }

    private fun getTerms(contract: AwardContractUpdateData.Release.Contract): List<AwardContractUpdateContext.Term> {
        return contract.agreedMetrics.map { agreedMetric ->
            AwardContractUpdateContext.Term(
                id = agreedMetric.id,
                title = agreedMetric.title,
                description = agreedMetric.description,
                observations = agreedMetric.observations.map { observation ->
                    AwardContractUpdateContext.Term.Observation(
                        id = observation.id,
                        notes = observation.notes,
                        measure = observation.measure
                    )
                }
            )
        }
    }

    private fun getBuyer(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                         msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): AwardContractUpdateContext.Buyer {
        return AwardContractUpdateContext.Buyer(
            id = acPartyWithRoleBuyer?.id ?: msPartyWithRoleBuyer.id,
            name = acPartyWithRoleBuyer?.name ?: msPartyWithRoleBuyer.name,
            address = getBuyerAddress(acPartyWithRoleBuyer, msPartyWithRoleBuyer),
            identifier = getBuyerIdentifier(acPartyWithRoleBuyer, msPartyWithRoleBuyer),
            additionalIdentifiers = getBuyerAdditionalIdentifiers(acPartyWithRoleBuyer, msPartyWithRoleBuyer),
            contactPoint = getBuyerContactPoint(acPartyWithRoleBuyer, msPartyWithRoleBuyer),
            details = getBuyerDetails(acPartyWithRoleBuyer, msPartyWithRoleBuyer),
            persones = getBuyerPersones(acPartyWithRoleBuyer)
        )
    }

    private fun getBuyerAddress(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                                msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): AwardContractUpdateContext.Buyer.Address {
        return acPartyWithRoleBuyer
            ?.let {
                it.address.let { address ->
                    AwardContractUpdateContext.Buyer.Address(
                        streetAddress = address.streetAddress,
                        postalCode = address.postalCode,
                        country = address.addressDetails.country.let { country ->
                            AwardContractUpdateContext.Buyer.Address.Country(
                                id = country.id,
                                description = country.description
                            )
                        },
                        region = address.addressDetails.region.let { region ->
                            AwardContractUpdateContext.Buyer.Address.Region(
                                id = region.id,
                                description = region.description
                            )
                        },
                        locality = address.addressDetails.locality.let { locality ->
                            AwardContractUpdateContext.Buyer.Address.Locality(
                                scheme = locality.scheme,
                                id = locality.id,
                                description = locality.description
                            )
                        }
                    )

                }
            }
            ?: msPartyWithRoleBuyer.address.let { address ->
                AwardContractUpdateContext.Buyer.Address(
                    streetAddress = address.streetAddress,
                    postalCode = address.postalCode,
                    country = address.addressDetails.country.let { country ->
                        AwardContractUpdateContext.Buyer.Address.Country(
                            id = country.id,
                            description = country.description
                        )
                    },
                    region = address.addressDetails.region.let { region ->
                        AwardContractUpdateContext.Buyer.Address.Region(
                            id = region.id,
                            description = region.description
                        )
                    },
                    locality = address.addressDetails.locality.let { locality ->
                        AwardContractUpdateContext.Buyer.Address.Locality(
                            scheme = locality.scheme,
                            id = locality.id,
                            description = locality.description
                        )
                    }
                )
            }
    }

    private fun getBuyerIdentifier(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                                   msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): AwardContractUpdateContext.Buyer.Identifier {
        return acPartyWithRoleBuyer?.let {
            it.identifier.let { identifier ->
                AwardContractUpdateContext.Buyer.Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            }
        } ?: msPartyWithRoleBuyer.identifier.let { identifier ->
            AwardContractUpdateContext.Buyer.Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        }
    }

    private fun getBuyerAdditionalIdentifiers(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                                              msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): List<AwardContractUpdateContext.Buyer.AdditionalIdentifier> {
        return acPartyWithRoleBuyer?.additionalIdentifiers
            ?.let {
                it.map { identifier ->
                    AwardContractUpdateContext.Buyer.AdditionalIdentifier(
                        scheme = identifier.scheme,
                        id = identifier.id,
                        legalName = identifier.legalName,
                        uri = identifier.uri
                    )
                }
            }
            ?: msPartyWithRoleBuyer.additionalIdentifiers?.map { identifier ->
                AwardContractUpdateContext.Buyer.AdditionalIdentifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            }
            ?: emptyList()
    }

    private fun getBuyerContactPoint(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                                     msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): AwardContractUpdateContext.Buyer.ContactPoint {
        return acPartyWithRoleBuyer
            ?.let {
                it.contactPoint.let { contactPoint ->
                    AwardContractUpdateContext.Buyer.ContactPoint(
                        name = contactPoint.name,
                        url = contactPoint.url,
                        telephone = contactPoint.telephone,
                        email = contactPoint.email,
                        faxNumber = contactPoint.faxNumber
                    )
                }
            }
            ?: msPartyWithRoleBuyer.contactPoint.let { contactPoint ->
                AwardContractUpdateContext.Buyer.ContactPoint(
                    name = contactPoint.name,
                    url = contactPoint.url,
                    telephone = contactPoint.telephone,
                    email = contactPoint.email,
                    faxNumber = contactPoint.faxNumber
                )
            }
    }

    private fun getBuyerDetails(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                                msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party): AwardContractUpdateContext.Buyer.Details? {
        return acPartyWithRoleBuyer
            ?.let { party ->
                val details = party.details!!

                AwardContractUpdateContext.Buyer.Details(
                    typeOfBuyer = details.typeOfBuyer,
                    mainGeneralActivity = details.mainGeneralActivity,
                    mainSectoralActivity = details.mainSectoralActivity,
                    permits = details.permits?.map { permit ->
                        AwardContractUpdateContext.Buyer.Details.Permit(
                            scheme = permit.scheme,
                            id = permit.id,
                            url = permit.url,
                            issuedBy = permit.permitDetails.issuedBy.let { issuedBy ->
                                AwardContractUpdateContext.Buyer.Details.Permit.IssuedBy(
                                    id = issuedBy.id,
                                    name = issuedBy.name
                                )
                            },
                            issuedThought = permit.permitDetails.issuedThought.let { issuedThought ->
                                AwardContractUpdateContext.Buyer.Details.Permit.IssuedThought(
                                    id = issuedThought.id,
                                    name = issuedThought.name
                                )
                            },
                            validityPeriod = permit.permitDetails.validityPeriod.let { validityPeriod ->
                                AwardContractUpdateContext.Buyer.Details.Permit.ValidityPeriod(
                                    startDate = validityPeriod.startDate,
                                    endDate = validityPeriod.endDate
                                )
                            }
                        )
                    } ?: emptyList(),
                    bankAccounts = details.bankAccounts
                        ?.map { bankAccount ->
                            AwardContractUpdateContext.Buyer.Details.BankAccount(
                                bankName = bankAccount.bankName,
                                description = bankAccount.description,
                                address = bankAccount.address.let { address ->
                                    AwardContractUpdateContext.Buyer.Details.BankAccount.Address(
                                        streetAddress = address.streetAddress,
                                        postalCode = address.postalCode,
                                        country = address.addressDetails.country.let { country ->
                                            AwardContractUpdateContext.Buyer.Details.BankAccount.Address.Country(
                                                id = country.id,
                                                description = country.description,
                                                uri = country.uri
                                            )
                                        },
                                        region = address.addressDetails.region.let { region ->
                                            AwardContractUpdateContext.Buyer.Details.BankAccount.Address.Region(
                                                id = region.id,
                                                description = region.description,
                                                uri = region.uri
                                            )
                                        },
                                        locality = address.addressDetails.locality.let { locality ->
                                            AwardContractUpdateContext.Buyer.Details.BankAccount.Address.Locality(
                                                scheme = locality.scheme,
                                                id = locality.id,
                                                description = locality.description
                                            )
                                        }
                                    )
                                },
                                identifier = bankAccount.identifier.let { identifier ->
                                    AwardContractUpdateContext.Buyer.Details.BankAccount.Identifier(
                                        scheme = identifier.scheme,
                                        id = identifier.id
                                    )
                                },
                                accountIdentification = bankAccount.accountIdentification.let { accountIdentification ->
                                    AwardContractUpdateContext.Buyer.Details.BankAccount.AccountIdentification(
                                        scheme = accountIdentification.scheme,
                                        id = accountIdentification.id
                                    )
                                },
                                additionalAccountIdentifiers = bankAccount.additionalAccountIdentifiers.map { additionalAccountIdentifiers ->
                                    AwardContractUpdateContext.Buyer.Details.BankAccount.AdditionalAccountIdentifier(
                                        scheme = additionalAccountIdentifiers.scheme,
                                        id = additionalAccountIdentifiers.id
                                    )
                                }
                            )
                        } ?: emptyList(),
                    legalForm = details.legalForm
                        ?.let { legalForm ->
                            AwardContractUpdateContext.Buyer.Details.LegalForm(
                                scheme = legalForm.scheme,
                                id = legalForm.id,
                                description = legalForm.description,
                                uri = legalForm.uri
                            )
                        }
                )
            }
            ?: msPartyWithRoleBuyer.details?.let { details ->
                AwardContractUpdateContext.Buyer.Details(
                    typeOfBuyer = details.typeOfBuyer,
                    mainGeneralActivity = details.mainGeneralActivity,
                    mainSectoralActivity = details.mainSectoralActivity,
                    permits = emptyList(),
                    bankAccounts = emptyList(),
                    legalForm = null
                )
            }
    }

    private fun getBuyerPersones(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?): List<AwardContractUpdateContext.Buyer.Persone> {
        return acPartyWithRoleBuyer?.let { party ->
            party.persones
                ?.map { persone ->
                    AwardContractUpdateContext.Buyer.Persone(
                        title = persone.title,
                        name = persone.name,
                        identifier = persone.identifier.let { identifier ->
                            AwardContractUpdateContext.Buyer.Persone.Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri
                            )
                        },
                        businessFunctions = persone.businessFunctions.map { businessFunction ->
                            AwardContractUpdateContext.Buyer.Persone.BusinessFunction(
                                id = businessFunction.id,
                                type = businessFunction.type,
                                jobTitle = businessFunction.jobTitle,
                                startDate = businessFunction.period.startDate,
                                documents = businessFunction.documents.map { document ->
                                    AwardContractUpdateContext.Buyer.Persone.BusinessFunction.Document(
                                        id = document.id,
                                        type = document.documentType,
                                        title = document.title,
                                        description = document.description
                                    )
                                }
                            )
                        }

                    )
                } ?: emptyList()

        } ?: emptyList()
    }

    private fun getBuyerUris(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                             msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party,
                             lang: String): AwardContractUpdateContext.BuyerUris {
        return acPartyWithRoleBuyer?.let { party ->
            AwardContractUpdateContext.BuyerUris(
                country = "/country/${party.address.addressDetails.country.id}?lang=$lang",
                region = "/region?lang=$lang&country=${party.address.addressDetails.country.id}",
                locality = "/locality?lang=$lang&country=${party.address.addressDetails.country.id}&region=${party.address.addressDetails.region.id}",
                registrationScheme = "/registration-scheme?lang=$lang&country=${party.address.addressDetails.country.id}"
            )
        } ?: msPartyWithRoleBuyer.let { party ->
            AwardContractUpdateContext.BuyerUris(
                country = "/country/${party.address.addressDetails.country.id}?lang=$lang",
                region = "/region?lang=$lang&country=${party.address.addressDetails.country.id}",
                locality = "/locality?lang=$lang&country=${party.address.addressDetails.country.id}&region=${party.address.addressDetails.region.id}",
                registrationScheme = "/registration-scheme?lang=$lang&country=${party.address.addressDetails.country.id}"
            )
        }
    }

    private fun getSuppliers(acPartiesWithRoleSupplier: List<AwardContractUpdateData.Release.Party>): List<AwardContractUpdateContext.Supplier> {
        return acPartiesWithRoleSupplier.map { party ->
            AwardContractUpdateContext.Supplier(
                id = party.id,
                name = party.name,
                address = getSupplierAddress(party),
                identifier = getSupplierIdentifier(party),
                additionalIdentifiers = getSupplierAdditionalIdentifiers(party),
                contactPoint = getSupplierContactPoint(party),
                details = getSupplierDetails(party),
                persones = getSupplierPersones(party)
            )
        }
    }

    private fun getSupplierAddress(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): AwardContractUpdateContext.Supplier.Address {
        return acPartyWithRoleSupplier.address.let { address ->
            AwardContractUpdateContext.Supplier.Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                country = address.addressDetails.country.let { country ->
                    AwardContractUpdateContext.Supplier.Address.Country(
                        id = country.id,
                        description = country.description
                    )
                },
                region = address.addressDetails.region.let { region ->
                    AwardContractUpdateContext.Supplier.Address.Region(
                        id = region.id,
                        description = region.description
                    )
                },
                locality = address.addressDetails.locality.let { locality ->
                    AwardContractUpdateContext.Supplier.Address.Locality(
                        scheme = locality.scheme,
                        id = locality.id,
                        description = locality.description
                    )
                }
            )

        }
    }

    private fun getSupplierIdentifier(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): AwardContractUpdateContext.Supplier.Identifier {
        return acPartyWithRoleSupplier.identifier.let { identifier ->
            AwardContractUpdateContext.Supplier.Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        }
    }

    private fun getSupplierAdditionalIdentifiers(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): List<AwardContractUpdateContext.Supplier.AdditionalIdentifier> {
        return acPartyWithRoleSupplier.additionalIdentifiers?.map { identifier ->
            AwardContractUpdateContext.Supplier.AdditionalIdentifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        } ?: emptyList()
    }

    private fun getSupplierContactPoint(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): AwardContractUpdateContext.Supplier.ContactPoint {
        return acPartyWithRoleSupplier.contactPoint.let { contactPoint ->
            AwardContractUpdateContext.Supplier.ContactPoint(
                name = contactPoint.name,
                url = contactPoint.url,
                telephone = contactPoint.telephone,
                email = contactPoint.email,
                faxNumber = contactPoint.faxNumber
            )
        }
    }

    private fun getSupplierDetails(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): AwardContractUpdateContext.Supplier.Details {
        return acPartyWithRoleSupplier.let { party ->
            val details = party.details!!

            AwardContractUpdateContext.Supplier.Details(
                typeOfSupplier = details.typeOfBuyer,
                mainEconomicActivities = details.mainEconomicActivities ?: emptyList(),
                scale = details.scale!!,
                permits = details.permits?.map { permit ->
                    AwardContractUpdateContext.Supplier.Details.Permit(
                        scheme = permit.scheme,
                        id = permit.id,
                        url = permit.url,
                        issuedBy = permit.permitDetails.issuedBy.let { issuedBy ->
                            AwardContractUpdateContext.Supplier.Details.Permit.IssuedBy(
                                id = issuedBy.id,
                                name = issuedBy.name
                            )
                        },
                        issuedThought = permit.permitDetails.issuedThought.let { issuedThought ->
                            AwardContractUpdateContext.Supplier.Details.Permit.IssuedThought(
                                id = issuedThought.id,
                                name = issuedThought.name
                            )
                        },
                        validityPeriod = permit.permitDetails.validityPeriod.let { validityPeriod ->
                            AwardContractUpdateContext.Supplier.Details.Permit.ValidityPeriod(
                                startDate = validityPeriod.startDate,
                                endDate = validityPeriod.endDate
                            )
                        }
                    )
                } ?: emptyList(),
                bankAccounts = details.bankAccounts
                    ?.map { bankAccount ->
                        AwardContractUpdateContext.Supplier.Details.BankAccount(
                            bankName = bankAccount.bankName,
                            description = bankAccount.description,
                            address = bankAccount.address.let { address ->
                                AwardContractUpdateContext.Supplier.Details.BankAccount.Address(
                                    streetAddress = address.streetAddress,
                                    postalCode = address.postalCode,
                                    country = address.addressDetails.country.let { country ->
                                        AwardContractUpdateContext.Supplier.Details.BankAccount.Address.Country(
                                            id = country.id,
                                            description = country.description,
                                            uri = country.uri
                                        )
                                    },
                                    region = address.addressDetails.region.let { region ->
                                        AwardContractUpdateContext.Supplier.Details.BankAccount.Address.Region(
                                            id = region.id,
                                            description = region.description,
                                            uri = region.uri
                                        )
                                    },
                                    locality = address.addressDetails.locality.let { locality ->
                                        AwardContractUpdateContext.Supplier.Details.BankAccount.Address.Locality(
                                            scheme = locality.scheme,
                                            id = locality.id,
                                            description = locality.description
                                        )
                                    }
                                )
                            },
                            identifier = bankAccount.identifier.let { identifier ->
                                AwardContractUpdateContext.Supplier.Details.BankAccount.Identifier(
                                    scheme = identifier.scheme,
                                    id = identifier.id
                                )
                            },
                            accountIdentification = bankAccount.accountIdentification.let { accountIdentification ->
                                AwardContractUpdateContext.Supplier.Details.BankAccount.AccountIdentification(
                                    scheme = accountIdentification.scheme,
                                    id = accountIdentification.id
                                )
                            },
                            additionalAccountIdentifiers = bankAccount.additionalAccountIdentifiers.map { additionalAccountIdentifiers ->
                                AwardContractUpdateContext.Supplier.Details.BankAccount.AdditionalAccountIdentifier(
                                    scheme = additionalAccountIdentifiers.scheme,
                                    id = additionalAccountIdentifiers.id
                                )
                            }
                        )
                    } ?: emptyList(),
                legalForm = details.legalForm
                    ?.let { legalForm ->
                        AwardContractUpdateContext.Supplier.Details.LegalForm(
                            scheme = legalForm.scheme,
                            id = legalForm.id,
                            description = legalForm.description,
                            uri = legalForm.uri
                        )
                    }
            )
        }
    }

    private fun getSupplierPersones(acPartyWithRoleSupplier: AwardContractUpdateData.Release.Party): List<AwardContractUpdateContext.Supplier.Persone> {
        return acPartyWithRoleSupplier.let { party ->
            party.persones
                ?.map { persone ->
                    AwardContractUpdateContext.Supplier.Persone(
                        title = persone.title,
                        name = persone.name,
                        identifier = persone.identifier.let { identifier ->
                            AwardContractUpdateContext.Supplier.Persone.Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri
                            )
                        },
                        businessFunctions = persone.businessFunctions.map { businessFunction ->
                            AwardContractUpdateContext.Supplier.Persone.BusinessFunction(
                                id = businessFunction.id,
                                type = businessFunction.type,
                                jobTitle = businessFunction.jobTitle,
                                startDate = businessFunction.period.startDate,
                                documents = businessFunction.documents.map { document ->
                                    AwardContractUpdateContext.Supplier.Persone.BusinessFunction.Document(
                                        id = document.id,
                                        type = document.documentType,
                                        title = document.title,
                                        description = document.description
                                    )
                                }
                            )
                        }

                    )
                } ?: emptyList()
        }
    }

    private fun getSupplierUris(acPartiesWithRoleSupplier: List<AwardContractUpdateData.Release.Party>,
                                lang: String): AwardContractUpdateContext.SupplierUris {
        return acPartiesWithRoleSupplier[0].let { party ->
            AwardContractUpdateContext.SupplierUris(
                country = "/country/${party.address.addressDetails.country.id}?lang=$lang",
                region = "/region?lang=$lang&country=${party.address.addressDetails.country.id}",
                locality = "/locality?lang=$lang&country=${party.address.addressDetails.country.id}&region=${party.address.addressDetails.region.id}",
                registrationScheme = "/registration-scheme?lang=$lang&country=${party.address.addressDetails.country.id}"
            )
        }
    }

    private fun getMilestones(contract: AwardContractUpdateData.Release.Contract): List<AwardContractUpdateContext.Milestone> {
        return contract.milestones?.map { milestone ->
            AwardContractUpdateContext.Milestone(
                id = milestone.id,
                title = milestone.title,
                description = milestone.description,
                type = milestone.type,
                additionalInformation = milestone.additionalInformation,
                dueDate = milestone.dueDate,
                relatedItem = milestone.relatedItems[0]
            )
        } ?: emptyList()
    }

    private fun getTransactions(planning: AwardContractUpdateData.Release.Planning?): List<AwardContractUpdateContext.Transaction> {
        return planning?.let {
            it.implementation.transactions
                ?.map { transaction ->
                    AwardContractUpdateContext.Transaction(
                        id = transaction.id,
                        type = transaction.type,
                        value = transaction.value.let { value ->
                            AwardContractUpdateContext.Transaction.Value(
                                amount = value.amount.toDouble()
                            )
                        },
                        durationInDays = transaction.executionPeriod.durationInDays,
                        relatedContractMilestone = transaction.relatedContractMilestone
                    )
                } ?: emptyList()
        } ?: emptyList()
    }

    private fun getBudget(planning: AwardContractUpdateData.Release.Planning?): AwardContractUpdateContext.Budget? {
        return planning?.let {
            it.budget.let { budget ->
                AwardContractUpdateContext.Budget(
                    description = budget.description,
                    budgetAllocations = budget.budgetAllocation.map { budgetAllocation ->
                        AwardContractUpdateContext.Budget.BudgetAllocation(
                            budgetBreakdownId = budgetAllocation.budgetBreakdownID,
                            period = budgetAllocation.period.let { period ->
                                AwardContractUpdateContext.Budget.BudgetAllocation.Period(
                                    startDate = period.startDate,
                                    endDate = period.endDate
                                )
                            },
                            amount = budgetAllocation.amount.toDouble(),
                            relatedItem = budgetAllocation.relatedItem
                        )
                    },
                    budgetSources = budget.budgetSource.map { budgetSource ->
                        AwardContractUpdateContext.Budget.BudgetSource(
                            budgetBreakdownID = budgetSource.budgetBreakdownID,
                            amount = budgetSource.amount.toDouble()
                        )
                    }
                )
            }
        }
    }

    private fun getItemUris(acPartyWithRoleBuyer: AwardContractUpdateData.Release.Party?,
                            msPartyWithRoleBuyer: MSForAwardContractUpdateData.Release.Party,
                            lang: String): AwardContractUpdateContext.ItemUris {
        return acPartyWithRoleBuyer?.let { party ->
            AwardContractUpdateContext.ItemUris(
                country = "/country/${party.address.addressDetails.country.id}?lang=$lang",
                region = "/region?lang=$lang&country=${party.address.addressDetails.country.id}",
                locality = "/locality?lang=$lang&country=${party.address.addressDetails.country.id}&region=\$region\$"
            )
        } ?: msPartyWithRoleBuyer.let { party ->
            AwardContractUpdateContext.ItemUris(
                country = "/country/${party.address.addressDetails.country.id}?lang=$lang",
                region = "/region?lang=$lang&country=${party.address.addressDetails.country.id}",
                locality = "/locality?lang=$lang&country=${party.address.addressDetails.country.id}&region=\$region\$"
            )
        }
    }

    private fun partyByRole(role: String,
                            parties: List<AwardContractUpdateData.Release.Party>): AwardContractUpdateData.Release.Party? {
        if (parties.isEmpty())
            return null

        val predicate = role.toUpperCase()
        for (party in parties) {
            val found = party.roles.any {
                it.toUpperCase() == predicate
            }

            if (found) return party
        }

        return null
    }

    private fun partiesByRole(role: String,
                              parties: List<AwardContractUpdateData.Release.Party>): List<AwardContractUpdateData.Release.Party> {
        if (parties.isEmpty())
            return emptyList()

        val predicate = role.toUpperCase()
        val result = mutableListOf<AwardContractUpdateData.Release.Party>()
        for (party in parties) {
            val found = party.roles.any {
                it.toUpperCase() == predicate
            }

            if (found) result.add(party)
        }

        return result
    }

    private fun partyByRole(role: String,
                            parties: List<MSForAwardContractUpdateData.Release.Party>): MSForAwardContractUpdateData.Release.Party? {
        if (parties.isEmpty())
            return null

        val predicate = role.toUpperCase()
        for (party in parties) {
            val found = party.roles.any {
                it.toUpperCase() == predicate
            }

            if (found) return party
        }

        return null
    }
}
