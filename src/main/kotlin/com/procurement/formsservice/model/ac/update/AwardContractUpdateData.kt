package com.procurement.formsservice.model.ac.update

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class AwardContractUpdateData(
    @JsonProperty("releases") val releases: List<Release>
) {
    data class Release(
        @JsonProperty("planning") val planning: Planning?,
        @JsonProperty("contracts") val contracts: List<Contract>,
        @JsonProperty("awards") val awards: List<Award>,
        @JsonProperty("parties") val parties: List<Party>
    ) {
        data class Planning(
            @JsonProperty("implementation") val implementation: Implementation,
            @JsonProperty("budget") val budget: Budget
        ) {
            data class Budget(
                @JsonProperty("description") val description: String,
                @JsonProperty("budgetAllocation") val budgetAllocation: List<BudgetAllocation>,
                @JsonProperty("budgetSource") val budgetSource: List<BudgetSource>
            ) {
                data class BudgetAllocation(
                    @JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,
                    @JsonProperty("period") val period: Period,
                    @JsonProperty("amount") val amount: BigDecimal,
                    @JsonProperty("relatedItem") val relatedItem: String
                ) {
                    data class Period(
                        @JsonProperty("startDate") val startDate: String,
                        @JsonProperty("endDate") val endDate: String
                    )
                }

                data class BudgetSource(
                    @JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,
                    @JsonProperty("amount") val amount: BigDecimal,
                    @JsonProperty("currency") val currency: String
                )
            }

            data class Implementation(
                @JsonProperty("transactions") val transactions: List<Transaction>?
            ) {
                data class Transaction(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("type") val type: String,
                    @JsonProperty("value") val value: Value,
                    @JsonProperty("executionPeriod") val executionPeriod: ExecutionPeriod,
                    @JsonProperty("relatedContractMilestone") val relatedContractMilestone: String?
                ) {
                    data class ExecutionPeriod(
                        @JsonProperty("durationInDays") val durationInDays: Int
                    )

                    data class Value(
                        @JsonProperty("amount") val amount: BigDecimal
                    )
                }
            }
        }

        data class Contract(
            @JsonProperty("title") val title: String?,
            @JsonProperty("description") val description: String?,
            @JsonProperty("period") val period: Period?,
            @JsonProperty("documents") val documents: List<Document>?,
            @JsonProperty("milestones") val milestones: List<Milestone>?,
            @JsonProperty("confirmationRequests") val confirmationRequests: List<ConfirmationRequest>?,
            @JsonProperty("agreedMetrics") val agreedMetrics: List<AgreedMetric>
        ) {

            data class Period(
                @JsonProperty("startDate") val startDate: String,
                @JsonProperty("endDate") val endDate: String
            )

            data class Document(
                @JsonProperty("documentType") val documentType: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("title") val title: String?,
                @JsonProperty("description") val description: String?,
                @JsonProperty("relatedLots") val relatedLots: List<String>
            )

            data class Milestone(
                @JsonProperty("id") val id: String,
                @JsonProperty("relatedItems") val relatedItems: List<String>,
                @JsonProperty("additionalInformation") val additionalInformation: String,
                @JsonProperty("dueDate") val dueDate: String,
                @JsonProperty("title") val title: String,
                @JsonProperty("type") val type: String,
                @JsonProperty("description") val description: String
            )

            data class ConfirmationRequest(
                @JsonProperty("id") val id: String,
                @JsonProperty("relatedItem") val relatedItem: String,
                @JsonProperty("source") val source: String
            )

            data class AgreedMetric(
                @JsonProperty("id") val id: String,
                @JsonProperty("title") val title: String,
                @JsonProperty("description") val description: String,
                @JsonProperty("observations") val observations: List<Observation>
            ) {
                data class Observation(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("notes") val notes: String,
                    @JsonProperty("measure") val measure: Int?
                )
            }
        }

        data class Award(
            @JsonProperty("id") val id: String,
            @JsonProperty("relatedLots") val relatedLots: List<String>,
            @JsonProperty("documents") val documents: List<Document>?,
            @JsonProperty("value") val value: Value,
            @JsonProperty("items") val items: List<Item>
        ) {

            data class Document(
                @JsonProperty("documentType") val documentType: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("title") val title: String?,
                @JsonProperty("description") val description: String?,
                @JsonProperty("relatedLots") val relatedLots: List<String>?
            )

            data class Value(
                @JsonProperty("amount") val amount: BigDecimal,
                @JsonProperty("amountNet") val amountNet: BigDecimal?,
                @JsonProperty("currency") val currency: String,
                @JsonProperty("valueAddedTaxIncluded") val valueAddedTaxIncluded: Boolean?
            )

            data class Item(
                @JsonProperty("id") val id: String,
                @JsonProperty("quantity") val quantity: BigDecimal,
                @JsonProperty("unit") val unit: Unit,
                @JsonProperty("deliveryAddress") val deliveryAddress: DeliveryAddress?
            ) {
                data class Unit(
                    @JsonProperty("value") val value: Value?
                ) {
                    data class Value(
                        @JsonProperty("amount") val amount: BigDecimal,
                        @JsonProperty("amountNet") val amountNet: BigDecimal,
                        @JsonProperty("valueAddedTaxIncluded") val valueAddedTaxIncluded: Boolean
                    )
                }

                data class DeliveryAddress(
                    @JsonProperty("streetAddress") val streetAddress: String,
                    @JsonProperty("postalCode") val postalCode: String?,
                    @JsonProperty("addressDetails") val addressDetails: AddressDetails
                ) {
                    data class AddressDetails(
                        @JsonProperty("country") val country: Country,
                        @JsonProperty("region") val region: Region,
                        @JsonProperty("locality") val locality: Locality
                    ) {
                        data class Country(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )

                        data class Region(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )

                        data class Locality(
                            @JsonProperty("scheme") val scheme: String,
                            @JsonProperty("id") val id: String,
                            @JsonProperty("description") val description: String
                        )
                    }
                }
            }
        }

        data class Party(
            @JsonProperty("id") val id: String,
            @JsonProperty("name") val name: String,
            @JsonProperty("identifier") val identifier: Identifier,
            @JsonProperty("address") val address: Address,
            @JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @JsonProperty("roles") val roles: List<String>,
            @JsonProperty("persones") val persones: List<Persone>?,
            @JsonProperty("details") val details: Details?
        ) {
            data class Identifier(
                @JsonProperty("id") val id: String,
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("legalName") val legalName: String,
                @JsonProperty("uri") val uri: String?
            )

            data class Address(
                @JsonProperty("streetAddress") val streetAddress: String,
                @JsonProperty("postalCode") val postalCode: String?,
                @JsonProperty("addressDetails") val addressDetails: AddressDetails
            ) {
                data class AddressDetails(
                    @JsonProperty("country") val country: Country,
                    @JsonProperty("region") val region: Region,
                    @JsonProperty("locality") val locality: Locality
                ) {
                    data class Country(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class Region(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )

                    data class Locality(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String,
                        @JsonProperty("description") val description: String
                    )
                }
            }

            data class ContactPoint(
                @JsonProperty("name") val name: String,
                @JsonProperty("email") val email: String,
                @JsonProperty("telephone") val telephone: String,
                @JsonProperty("faxNumber") val faxNumber: String?,
                @JsonProperty("url") val url: String?
            )

            data class AdditionalIdentifier(
                @JsonProperty("scheme") val scheme: String,
                @JsonProperty("id") val id: String,
                @JsonProperty("legalName") val legalName: String,
                @JsonProperty("uri") val uri: String?
            )

            data class Persone(
                @JsonProperty("title") val title: String,
                @JsonProperty("name") val name: String,
                @JsonProperty("identifier") val identifier: Identifier,
                @JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {
                data class Identifier(
                    @JsonProperty("scheme") val scheme: String,
                    @JsonProperty("id") val id: String,
                    @JsonProperty("uri") val uri: String?
                )

                data class BusinessFunction(
                    @JsonProperty("id") val id: String,
                    @JsonProperty("type") val type: String,
                    @JsonProperty("jobTitle") val jobTitle: String,
                    @JsonProperty("period") val period: Period,
                    @JsonProperty("documents") val documents: List<Document>
                ) {
                    data class Document(
                        @JsonProperty("id") val id: String,
                        @JsonProperty("documentType") val documentType: String,
                        @JsonProperty("title") val title: String,
                        @JsonProperty("description") val description: String?
                    )

                    data class Period(
                        @JsonProperty("startDate") val startDate: String
                    )
                }
            }

            data class Details(
                @JsonProperty("typeOfBuyer") val typeOfBuyer: String?,
                @JsonProperty("typeOfSupplier") val typeOfSupplier: String?,
                @JsonProperty("mainGeneralActivity") val mainGeneralActivity: String?,
                @JsonProperty("mainSectoralActivity") val mainSectoralActivity: String?,
                @JsonProperty("mainEconomicActivities") val mainEconomicActivities: List<String>?,
                @JsonProperty("scale") val scale: String?,
                @JsonProperty("permits") val permits: List<Permit>?,
                @JsonProperty("bankAccounts") val bankAccounts: List<BankAccount>?,
                @JsonProperty("legalForm") val legalForm: LegalForm?
            ) {
                data class Permit(
                    @JsonProperty("scheme") val scheme: String,
                    @JsonProperty("id") val id: String,
                    @JsonProperty("url") val url: String,
                    @JsonProperty("permitDetails") val permitDetails: PermitDetails
                ) {
                    data class PermitDetails(
                        @JsonProperty("issuedBy") val issuedBy: IssuedBy,
                        @JsonProperty("issuedThought") val issuedThought: IssuedThought,
                        @JsonProperty("validityPeriod") val validityPeriod: ValidityPeriod
                    ) {
                        data class IssuedBy(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("name") val name: String
                        )

                        data class IssuedThought(
                            @JsonProperty("id") val id: String,
                            @JsonProperty("name") val name: String
                        )

                        data class ValidityPeriod(
                            @JsonProperty("startDate") val startDate: String,
                            @JsonProperty("endDate") val endDate: String?
                        )
                    }
                }

                data class BankAccount(
                    @JsonProperty("description") val description: String,
                    @JsonProperty("bankName") val bankName: String,
                    @JsonProperty("address") val address: Address,
                    @JsonProperty("identifier") val identifier: Identifier,
                    @JsonProperty("accountIdentification") val accountIdentification: AccountIdentification,
                    @JsonProperty("additionalAccountIdentifiers") val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>
                ) {
                    data class Address(
                        @JsonProperty("streetAddress") val streetAddress: String,
                        @JsonProperty("postalCode") val postalCode: String?,
                        @JsonProperty("addressDetails") val addressDetails: AddressDetails
                    ) {
                        data class AddressDetails(
                            @JsonProperty("country") val country: Country,
                            @JsonProperty("region") val region: Region,
                            @JsonProperty("locality") val locality: Locality
                        ) {
                            data class Country(
                                @JsonProperty("id") val id: String,
                                @JsonProperty("description") val description: String,
                                @JsonProperty("uri") val uri: String
                            )

                            data class Region(
                                @JsonProperty("id") val id: String,
                                @JsonProperty("description") val description: String,
                                @JsonProperty("uri") val uri: String
                            )

                            data class Locality(
                                @JsonProperty("scheme") val scheme: String,
                                @JsonProperty("id") val id: String,
                                @JsonProperty("description") val description: String
                            )
                        }
                    }

                    data class Identifier(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String
                    )

                    data class AccountIdentification(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String
                    )

                    data class AdditionalAccountIdentifier(
                        @JsonProperty("scheme") val scheme: String,
                        @JsonProperty("id") val id: String
                    )
                }

                data class LegalForm(
                    @JsonProperty("scheme") val scheme: String,
                    @JsonProperty("id") val id: String,
                    @JsonProperty("description") val description: String,
                    @JsonProperty("uri") val uri: String?
                )
            }
        }
    }
}