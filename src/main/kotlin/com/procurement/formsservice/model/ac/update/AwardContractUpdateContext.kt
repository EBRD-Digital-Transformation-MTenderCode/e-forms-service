package com.procurement.formsservice.model.ac.update

class AwardContractUpdateContext(
    val parameters: Parameters,
    val lotId: String, // AC.awards[0].relatedLots[0]
    val contract: Contract,
    val award: Award,
    val terms: List<Term>,
    val buyer: Buyer,
    val buyerUris: BuyerUris,
    val suppliers: List<Supplier>,
    val supplierUris: SupplierUris,
    val milestones: List<Milestone>, // AC.contracts[0].milestones[*]?
    val transactions: List<Transaction>, // AC.planning?.implementation.transactions[*]?
    val budget: Budget?, // AC.planning?.budget
    val itemUris: ItemUris,
    val currency: String // AC.awards[0].value.currency
) {
    data class Parameters(
        val ocid: String // ocds-t1s2t3-MD-1532010121824-AC-1532010122650
    )

    data class Contract(
        val title: String?, // AC.contracts[0].title
        val description: String?, // AC.contracts[0].description
        val period: Period?,
        val documents: List<Document> // AC.contracts[0].documents
    ) {
        data class Period(
            val startDate: String, // AC.contracts[0].period.startDate
            val endDate: String // AC.contracts[0].period.endDate
        )

        data class Document(
            val id: String, // AC.contracts[0].documents[*].id
            val type: String, // AC.contracts[0].documents[*].documentType
            val title: String?, // AC.contracts[0].documents[*].title
            val description: String?, // AC.contracts[0].documents[*].description
            val relatedLot: String?, // AC.contracts[0].documents[*].relatedLots[0]
            val confirmationRequest: ConfirmationRequest? // AC.contracts[0].confirmationRequests
        ) {
            data class ConfirmationRequest(
                val id: String, // AC.contracts[0].confirmationRequests[relatedItem==document.id].id
                val relatedItem: String, // AC.contracts[0].confirmationRequests[relatedItem==document.id].relatedItem
                val source: String // AC.contracts[0].confirmationRequests[relatedItem==document.id].source
            )
        }
    }

    data class Award(
        val id: String, // AC.awards[0].id
        val items: List<Item>,
        val value: Value,
        val documents: List<Document>
    ) {
        data class Item(
            val id: String, // AC.awards[0].items[*].id
            val quantity: Double, // AC.awards[0].items[*].quantity
            val value: Value?, // AC.awards[0].items[*].unit.value?
            val deliveryAddress: DeliveryAddress? // AC.awards[0].items[*].deliveryAddress?
        ) {
            data class Value(
                val amount: Double, // AC.awards[0].items[*].unit.value?.amount
                val amountNet: Double, // AC.awards[0].items[*].unit.value?.amountNet
                val valueAddedTaxIncluded: Boolean // AC.awards[0].items[*].unit.value?.valueAddedTaxIncluded
            )

            data class DeliveryAddress(
                val country: Country,
                val region: Region,
                val locality: Locality,
                val streetAddress: String, // AC.awards[0].items[*].deliveryAddress.streetAddress
                val postalCode: String? // AC.awards[0].items[*].deliveryAddress.postalCode
            ) {
                data class Country(
                    val id: String, // AC.awards[0].items[*].deliveryAddress.addressDetails.country.id
                    val description: String // AC.awards[0].items[*].deliveryAddress.addressDetails.country.description
                )

                data class Region(
                    val id: String, // AC.awards[0].items[*].deliveryAddress.addressDetails.region.id
                    val description: String // AC.awards[0].items[*].deliveryAddress.addressDetails.region.description
                )

                data class Locality(
                    val scheme: String, // AC.awards[0].items[*].deliveryAddress.addressDetails.locality.scheme
                    val id: String, // AC.awards[0].items[*].deliveryAddress.addressDetails.locality.id
                    val description: String // AC.awards[0].items[*].deliveryAddress.addressDetails.locality.description
                )
            }
        }

        data class Value(
            val amount: Double, // AC.awards[0].value.amount
            val amountNet: Double?, // AC.awards[0].value.amountNet
            val valueAddedTaxIncluded: Boolean? // AC.awards[0].value.valueAddedTaxIncluded
        )

        data class Document(
            val id: String, // AC.awards[0].documents[*].id
            val type: String, // AC.awards[0].documents[*].documentType
            val title: String?, // AC.awards[0].documents[*].title
            val description: String?, // AC.awards[0].documents[*].description
            val relatedLot: String? // AC.awards[0].documents[*].relatedLots?[0]
        )
    }

    data class Term(
        val id: String, // AC.contracts[0].agreedMetrics[*].id
        val title: String, // AC.contracts[0].agreedMetrics[*].title
        val description: String, // AC.contracts[0].agreedMetrics[*].description
        val observations: List<Observation> // AC.contracts[0].agreedMetrics[*].observations
    ) {
        data class Observation(
            val id: String, // AC.contracts[0].agreedMetrics[*].observations[*].id
            val notes: String, // AC.contracts[0].agreedMetrics[*].observations[*].notes
            val measure: String? // AC.contracts[0].agreedMetrics[*].observations[*].measure
        )
    }

    data class Buyer(
        val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].id : AC.parties[role==buyer].id
        val name: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].name : AC.parties[role==buyer].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>,
        val contactPoint: ContactPoint,
        val details: Details?,
        val persones: List<Persone> // AC.parties[role==buyer]?.persones[*]?

    ) {
        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.streetAddress : AC.parties[role==buyer].address.streetAddress
            val postalCode: String? // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.postalCode : AC.parties[role==buyer].address.postalCode
        ) {
            data class Country(
                val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.country.id
                val description: String // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.description : AC.parties[role==buyer].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.region.id
                val description: String // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.locality.scheme
                val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.locality.id
                val description: String // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].address.addressDetails.country.id : AC.parties[role==buyer].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].identifier.scheme : AC.parties[role==buyer].identifier.scheme
            val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].identifier.id : AC.parties[role==buyer].identifier.id
            val legalName: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].identifier.legalName : AC.parties[role==buyer].identifier.legalName
            val uri: String? // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].identifier.uri : AC.parties[role==buyer].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].additionalIdentifier[*].scheme : AC.parties[role==buyer].additionalIdentifier[*].scheme
            val id: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].additionalIdentifier[*].id : AC.parties[role==buyer].additionalIdentifier[*].id
            val legalName: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].additionalIdentifier[*].legalName : AC.parties[role==buyer].additionalIdentifier[*].legalName
            val uri: String? // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].additionalIdentifier[*].uri : AC.parties[role==buyer].additionalIdentifier[*].uri
        )

        data class ContactPoint(
            val name: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].contactPoint.name : AC.parties[role==buyer].contactPoint.name
            val url: String?, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].contactPoint.url : AC.parties[role==buyer].contactPoint.url
            val telephone: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].contactPoint.telephone : AC.parties[role==buyer].contactPoint.telephone
            val email: String, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].contactPoint.email : AC.parties[role==buyer].contactPoint.email
            val faxNumber: String? // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].contactPoint.faxNumber : AC.parties[role==buyer].contactPoint.faxNumber
        )

        data class Details(
            val typeOfBuyer: String?, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].details.typeOfBuyer : AC.parties[role==buyer].details.typeOfBuyer
            val mainGeneralActivity: String?, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].details.mainGeneralActivity : AC.parties[role==buyer].details.mainGeneralActivity
            val mainSectoralActivity: String?, // AC.parties[role==buyer] is missing ? MS.parties[role==buyer].details.mainSectoralActivity : AC.parties[role==buyer].details.mainSectoralActivity
            val permits: List<Permit>, // if AC.parties[role==buyer] is missing then empty list else AC.parties[role==buyer].details.permits[*]
            val bankAccounts: List<BankAccount>, // if AC.parties[role==buyer] is missing then empty list else AC.parties[role==buyer]?.details.bankAccounts[*]
            val legalForm: LegalForm? // AC.parties[role==buyer]?.details.legalForm
        ) {
            data class Permit(
                val scheme: String, // AC.parties[role==buyer]?.details.permits[*].scheme
                val id: String, // AC.parties[role==buyer]?.details.permits[*].id
                val url: String, // AC.parties[role==buyer]?.details.permits[*].url
                val issuedBy: IssuedBy,
                val issuedThought: IssuedThought,
                val validityPeriod: ValidityPeriod
            ) {
                data class IssuedBy(
                    val id: String, // AC.parties[role==buyer]?.details.permits[*].permitDetails.issuedBy.id
                    val name: String // AC.parties[role==buyer]?.details.permits[*].permitDetails.issuedBy.name
                )

                data class IssuedThought(
                    val id: String, // AC.parties[role==buyer]?.details.permits[*].permitDetails.IssuedThought.id
                    val name: String // AC.parties[role==buyer]?.details.permits[*].permitDetails.IssuedThought.name
                )

                data class ValidityPeriod(
                    val startDate: String, // AC.parties[role==buyer]?.details.permits[*].permitDetails.validityPeriod.startDate
                    val endDate: String? // AC.parties[role==buyer]?.details.permits[*].permitDetails.validityPeriod.endDate
                )
            }

            data class BankAccount(
                val bankName: String, // AC.parties[role==buyer]?.details.bankAccounts[*].bankName
                val description: String, // AC.parties[role==buyer]?.details.bankAccounts[*].description
                val address: Address,
                val identifier: Identifier,
                val accountIdentification: AccountIdentification,
                val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>
            ) {
                data class Address(
                    val country: Country,
                    val region: Region,
                    val locality: Locality,
                    val streetAddress: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.streetAddress
                    val postalCode: String? // AC.parties[role==buyer]?.details.bankAccounts[*].address.postalCode
                ) {
                    data class Country(
                        val id: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.country.id
                        val description: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.country.description
                        val uri: String // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.country.uri
                    )

                    data class Region(
                        val id: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.region.id
                        val description: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.region.description
                        val uri: String // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.region.uri
                    )

                    data class Locality(
                        val scheme: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.locality.scheme
                        val id: String, // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.locality.id
                        val description: String // AC.parties[role==buyer]?.details.bankAccounts[*].address.addressDetails.locality.description
                    )
                }

                data class Identifier(
                    val scheme: String, // AC.parties[role==buyer]?.details.bankAccounts[*].identifier.scheme
                    val id: String // AC.parties[role==buyer]?.details.bankAccounts[*].identifier.id
                )

                data class AccountIdentification(
                    val scheme: String, // AC.parties[role==buyer]?.details.bankAccounts[*].accountIdentification.scheme
                    val id: String // AC.parties[role==buyer]?.details.bankAccounts[*].accountIdentification.id
                )

                data class AdditionalAccountIdentifier(
                    val scheme: String, // AC.parties[role==buyer]?.details.bankAccounts[*].additionalAccountIdentifiers[*].scheme
                    val id: String // AC.parties[role==buyer]?.details.bankAccounts[*].additionalAccountIdentifiers[*].id
                )
            }

            data class LegalForm(
                val scheme: String, // AC.parties[role==buyer]?.details.legalForm.scheme
                val id: String, // AC.parties[role==buyer]?.details.legalForm.id
                val description: String, // AC.parties[role==buyer]?.details.legalForm.description
                val uri: String? // AC.parties[role==buyer]?.details.legalForm.uri
            )
        }

        data class Persone(
            val title: String, // AC.parties[role==buyer]?.persones[*].title
            val name: String, // AC.parties[role==buyer]?.persones[*].name
            val identifier: Identifier,
            val businessFunctions: List<BusinessFunction>
        ) {
            data class Identifier(
                val scheme: String, // AC.parties[role==buyer]?.persones[*].identifier.scheme
                val id: String, // AC.parties[role==buyer]?.persones[*].identifier.id
                val uri: String? // AC.parties[role==buyer]?.persones[*].identifier.uri
            )

            data class BusinessFunction(
                val id: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].id
                val type: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].type
                val jobTitle: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].jobTitle
                val startDate: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].period.startDate
                val documents: List<Document>
            ) {
                data class Document(
                    val id: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].documents[*].id
                    val type: String, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].documents[*].documentType
                    val title: String?, // AC.parties[role==buyer].persones[*]?.businessFunctions[*].documents[*].title
                    val description: String? // AC.parties[role==buyer].persones[*]?.businessFunctions[*].documents[*].description
                )
            }
        }
    }

    data class BuyerUris(
        val country: String, // /country/(AC || MS).parties[role=="buyer"].address.addressDetails.country.id?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=(AC || MS).parties[role=="buyer"].address.addressDetails.country.id
        val locality: String, // /locality?lang=langFromRequest&country=(AC || MS).parties[role=="buyer"].address.addressDetails.country.id&region=(AC || MS).parties[role=="buyer"].address.addressDetails.region.id
        val registrationScheme: String  // /registration-scheme?lang=langFromRequest&country=(AC || MS).parties[role=="buyer"].address.addressDetails.country.id
    )

    data class Supplier(
        val id: String, // AC.parties[role==supplier].id
        val name: String, // AC.parties[role==supplier].name
        val address: Address,
        val identifier: Identifier,
        val additionalIdentifiers: List<AdditionalIdentifier>,
        val contactPoint: ContactPoint,
        val details: Details,
        val persones: List<Persone> // AC.parties[role==supplier].persones[*]?
    ) {
        data class Address(
            val country: Country,
            val region: Region,
            val locality: Locality,
            val streetAddress: String, // AC.parties[role==supplier].address.streetAddress
            val postalCode: String? // AC.parties[role==supplier].address.postalCode
        ) {
            data class Country(
                val id: String, // AC.parties[role==supplier].address.addressDetails.country.id
                val description: String // AC.parties[role==supplier].address.addressDetails.country.description
            )

            data class Region(
                val id: String, // AC.parties[role==supplier].address.addressDetails.region.id
                val description: String // AC.parties[role==supplier].address.addressDetails.region.description
            )

            data class Locality(
                val scheme: String, // AC.parties[role==supplier].address.addressDetails.locality.scheme
                val id: String, // AC.parties[role==supplier].address.addressDetails.locality.id
                val description: String // AC.parties[role==supplier].address.addressDetails.locality.description
            )
        }

        data class Identifier(
            val scheme: String, // AC.parties[role==supplier].identifier.scheme
            val id: String, // AC.parties[role==supplier].identifier.id
            val legalName: String, // AC.parties[role==supplier].identifier.legalName
            val uri: String? // AC.parties[role==supplier].identifier.uri
        )

        data class AdditionalIdentifier(
            val scheme: String, // AC.parties[role==supplier].additionalIdentifier[*].scheme
            val id: String, // AC.parties[role==supplier].additionalIdentifier[*].id
            val legalName: String, // AC.parties[role==supplier].additionalIdentifier[*].legalName
            val uri: String? // AC.parties[role==supplier].additionalIdentifier[*].uri
        )

        data class ContactPoint(
            val name: String, // AC.parties[role==supplier].contactPoint.name
            val url: String?, // AC.parties[role==supplier].contactPoint.url
            val telephone: String, // AC.parties[role==supplier].contactPoint.telephone
            val email: String, // AC.parties[role==supplier].contactPoint.email
            val faxNumber: String? // AC.parties[role==supplier].contactPoint.faxNumber
        )

        data class Details(
            val typeOfSupplier: String?, // AC.parties[role==supplier].details.typeOfSupplier
            val mainEconomicActivities: List<String>, // AC.parties[role==supplier].details.mainEconomicActivities[*]?
            val scale: String, // AC.parties[role==supplier].details.scale
            val permits: List<Permit>, // AC.parties[role==supplier].details.permits[*]
            val bankAccounts: List<BankAccount>, // AC.parties[role==supplier].details.bankAccounts[*]
            val legalForm: LegalForm? // AC.parties[role==supplier].details.legalForm
        ) {
            data class Permit(
                val scheme: String, // AC.parties[role==supplier].details.permits[*].scheme
                val id: String, // AC.parties[role==supplier].details.permits[*].id
                val url: String, // AC.parties[role==supplier].details.permits[*].url
                val issuedBy: IssuedBy,
                val issuedThought: IssuedThought,
                val validityPeriod: ValidityPeriod
            ) {
                data class IssuedBy(
                    val id: String, // AC.parties[role==supplier].details.permits[*].permitDetails.issuedBy.id
                    val name: String // AC.parties[role==supplier].details.permits[*].permitDetails.issuedBy.name
                )

                data class IssuedThought(
                    val id: String, // AC.parties[role==supplier].details.permits[*].permitDetails.IssuedThought.id
                    val name: String // AC.parties[role==supplier].details.permits[*].permitDetails.IssuedThought.name
                )

                data class ValidityPeriod(
                    val startDate: String, // AC.parties[role==supplier].details.permits[*].permitDetails.validityPeriod.startDate
                    val endDate: String? // AC.parties[role==supplier].details.permits[*].permitDetails.validityPeriod.endDate
                )
            }

            data class BankAccount(
                val bankName: String, // AC.parties[role==supplier].details.bankAccounts[*].bankName
                val description: String, // AC.parties[role==supplier].details.bankAccounts[*].description
                val address: Address,
                val identifier: Identifier,
                val accountIdentification: AccountIdentification,
                val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>
            ) {
                data class Address(
                    val country: Country,
                    val region: Region,
                    val locality: Locality,
                    val streetAddress: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.streetAddress
                    val postalCode: String? // AC.parties[role==supplier]?.details.bankAccounts[*].address.postalCode
                ) {
                    data class Country(
                        val id: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.country.id
                        val description: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.country.description
                        val uri: String // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.country.uri
                    )

                    data class Region(
                        val id: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.region.id
                        val description: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.region.description
                        val uri: String // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.region.uri
                    )

                    data class Locality(
                        val scheme: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.locality.scheme
                        val id: String, // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.locality.id
                        val description: String // AC.parties[role==supplier]?.details.bankAccounts[*].address.addressDetails.locality.description
                    )
                }

                data class Identifier(
                    val scheme: String, // AC.parties[role==supplier].details.bankAccounts[*].identifier.scheme
                    val id: String // AC.parties[role==supplier].details.bankAccounts[*].identifier.id
                )

                data class AccountIdentification(
                    val scheme: String, // AC.parties[role==supplier].details.bankAccounts[*].accountIdentification.scheme
                    val id: String // AC.parties[role==supplier].details.bankAccounts[*].accountIdentification.id
                )

                data class AdditionalAccountIdentifier(
                    val scheme: String, // AC.parties[role==supplier].details.bankAccounts[*].additionalAccountIdentifiers[*].scheme
                    val id: String // AC.parties[role==supplier].details.bankAccounts[*].additionalAccountIdentifiers[*].id
                )
            }

            data class LegalForm(
                val scheme: String, // AC.parties[role==supplier].details.legalForm.scheme
                val id: String, // AC.parties[role==supplier].details.legalForm.id
                val description: String, // AC.parties[role==supplier].details.legalForm.description
                val uri: String? // AC.parties[role==supplier].details.legalForm.uri
            )
        }

        data class Persone(
            val title: String, // AC.parties[role==supplier].persones[*]?.title
            val name: String, // AC.parties[role==supplier].persones[*]?.name
            val identifier: Identifier,
            val businessFunctions: List<BusinessFunction>
        ) {
            data class Identifier(
                val scheme: String, // AC.parties[role==supplier].persones[*]?.scheme
                val id: String, // AC.parties[role==supplier].persones[*]?.id
                val uri: String? // AC.parties[role==supplier].persones[*]?.uri
            )

            data class BusinessFunction(
                val id: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].id
                val type: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].type
                val jobTitle: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].jobTitle
                val startDate: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].period.startDate
                val documents: List<Document>
            ) {
                data class Document(
                    val id: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].documents[*].id
                    val type: String, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].documents[*].documentType
                    val title: String?, // AC.parties[role==supplier].persones[*]?.businessFunctions[*].documents[*].title
                    val description: String? // AC.parties[role==supplier].persones[*]?.businessFunctions[*].documents[*].description
                )
            }
        }
    }

    data class SupplierUris(
        val country: String, // /country/AC.parties[role=="supplier"].address.addressDetails.country.id?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=AC.parties[role=="supplier"].address.addressDetails.country.id
        val locality: String, // /locality?lang=langFromRequest&country=AC.parties[role=="supplier"].address.addressDetails.country.id&region=AC.parties[role=="supplier"].address.addressDetails.region.id
        val registrationScheme: String  // /registration-scheme?lang=langFromRequest&country=AC.parties[role=="supplier"].address.addressDetails.country.id
    )

    data class Milestone(
        val id: String, // AC.contracts[0].milestones[*].id
        val title: String, // AC.contracts[0].milestones[*].title
        val description: String, // AC.contracts[0].milestones[*].description
        val type: String, // AC.contracts[0].milestones[*].type
        val additionalInformation: String, // AC.contracts[0].milestones[*].additionalInformation
        val dueDate: String, // AC.contracts[0].milestones[*].dueDate
        val relatedItem: String // AC.contracts[0].milestones[*].relatedItems[0]
    )

    data class Transaction(
        val id: String, // AC.planning?.implementation.transactions[*].id
        val type: String, // AC.planning?.implementation.transactions[*].type
        val value: Value,
        val durationInDays: Int, // AC.planning?.implementation.transactions[*].executionPeriod.durationInDays
        val relatedContractMilestone: String? // AC.planning?.implementation.transactions[*].relatedContractMilestone
    ) {
        data class Value(
            val amount: Double // AC.planning?.implementation.transactions[*].value.amount
        )
    }

    data class Budget(
        val description: String, // AC.planning?.budget.description
        val budgetAllocations: List<BudgetAllocation>,
        val budgetSources: List<BudgetSource>
    ) {
        data class BudgetAllocation(
            val budgetBreakdownId: String, // AC.planning?.budget.budgetAllocation[*].budgetBreakdownId
            val period: Period,
            val amount: Double, // AC.planning?.budget.budgetAllocation[*].amount
            val relatedItem: String // AC.planning?.budget.budgetAllocation[*].relatedItem
        ) {
            data class Period(
                val startDate: String, // AC.planning?.budget.budgetAllocation[*].period.startDate
                val endDate: String // AC.planning?.budget.budgetAllocation[*].period.endDate
            )
        }

        data class BudgetSource(
            val budgetBreakdownID: String, // AC.planning?.budget.budgetSource[*].budgetBreakdownId
            val amount: Double // AC.planning?.budget.budgetSource[*].amount
        )
    }

    data class ItemUris(
        val country: String, // /country/(AC || MS).parties[role=="buyer"].address.addressDetails.country.id?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=(AC || MS).parties[role=="buyer"].address.addressDetails.country.id
        val locality: String // /locality?lang=langFromRequest&country=(AC || MS).parties[role=="buyer"].address.addressDetails.country.id&region=$region$
    )
}

