package com.procurement.formsservice.definition.cn

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.attribute.number
import com.procurement.formsservice.json.attribute.string
import com.procurement.formsservice.json.container.arrayObjects
import com.procurement.formsservice.json.container.arrayStrings
import com.procurement.formsservice.json.container.obj
import com.procurement.formsservice.json.data.mdm.EntityKind
import com.procurement.formsservice.json.data.mdm.documentType
import com.procurement.formsservice.json.data.mdm.mdmCountries
import com.procurement.formsservice.json.data.mdm.mdmLanguage
import com.procurement.formsservice.json.data.mdm.mdmUnitClasses
import com.procurement.formsservice.json.path.StringPathSource

fun writeDataIfProcuringEntityIsBuyer(): (context: Context) -> Boolean = {
    it.parameters[CnNamesParameters.PROCURING_ENTITY] == CnProcuringEntity.BUYER
}

fun writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer(): (context: Context) -> Boolean = {
    it.parameters[CnNamesParameters.PROCURING_ENTITY] == CnProcuringEntity.BUYER
        && it.parameters[CnNamesParameters.RESPONSIBLE_CONTRACT_PERSON] == CnResponsibleContactPerson.BUYER
}

fun cnDefinition() = obj {
    title = "New Contract Notice"

    "section1" to obj {
        title = "Section I: Procedure Parties"
        description = "Information about all Parties involved in future Contracting Process"

        "procuringEntity" to obj {
            title = "Procuring Entity"
            description = "I.1) Name and addresses"

            "name" to string {
                title = "Official Name"
                source = ocds {
                    value = path("/parties::array[0]/name::string")
                }
                destination = destination("tender::object/procuringEntity::object/name::string")
                required = TRUE
            }

            "address" to obj {
                title = "Address"

                "location" to obj {
                    title = "Location"

                    "countryName" to string {
                        title = "Country"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/address::object/countryName::string",
                                usage = writeDataIfProcuringEntityIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/address::object/countryName::string")
                        required = TRUE
                    }

                    "region" to string {
                        title = "Region"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/address::object/region::string",
                                usage = writeDataIfProcuringEntityIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/address::object/region::string")
                        required = TRUE
                    }

                    "locality" to string {
                        title = "Locality"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/address::object/locality::string",
                                usage = writeDataIfProcuringEntityIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/address::object/locality::string")
                        required = TRUE
                    }
                }

                "postalAddress" to obj {
                    title = "Postal Address"

                    "streetAddress" to string {
                        title = "Street Address"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/address::object/streetAddress::string",
                                usage = writeDataIfProcuringEntityIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/address::object/streetAddress::string")
                        required = TRUE
                    }

                    "postalCode" to string {
                        title = "Postal Code"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/address::object/postalCode::string",
                                usage = writeDataIfProcuringEntityIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/address::object/postalCode::string")
                    }
                }
            }

            "identifier" to obj {
                title = "Main Identifier"

                "scheme" to string {
                    title = "Identification scheme"
                    source = ocds {
                        value = path(
                            path = "/parties::array[0]/identifier::object/scheme::string",
                            usage = writeDataIfProcuringEntityIsBuyer()
                        )
                    }
                    destination =
                        destination("tender::object/procuringEntity::object/identifier::object/scheme::string")
                    required = TRUE
                }

                "id" to string {
                    title = "Identifier"
                    source = ocds {
                        value = path(
                            path = "/parties::array[0]/identifier::object/id::string",
                            usage = writeDataIfProcuringEntityIsBuyer()
                        )
                    }
                    destination = destination("tender::object/procuringEntity::object/identifier::object/id::string")
                    required = TRUE
                }

                "legalName" to string {
                    title = "Legal Name"
                    source = ocds {
                        value = path(
                            path = "/parties::array[0]/identifier::object/legalName::string",
                            usage = writeDataIfProcuringEntityIsBuyer()
                        )
                    }
                    destination =
                        destination("tender::object/procuringEntity::object/identifier::object/legalName::string")
                    required = TRUE
                }

                "uri" to string {
                    title = "URI"
                    source = ocds {
                        value = path(
                            path = "/parties::array[0]/identifier::object/uri::string",
                            usage = writeDataIfProcuringEntityIsBuyer()
                        )
                    }
                    destination = destination("tender::object/procuringEntity::object/identifier::object/uri::string")
                    required = TRUE
                }
            }

            "additionalIdentifiers" to arrayObjects {
                title = "Additional Identifiers"

                "scheme" to string {
                    title = "Additional identifier scheme"
                    destination =
                        destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/scheme::string")
                    required = TRUE
                }

                "id" to string {
                    title = "Additional identifier ID"
                    destination =
                        destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/id::string")
                    required = TRUE
                }

                "legalName" to string {
                    title = "Additional identifier legal name"
                    destination =
                        destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/legalName::string")
                    required = TRUE
                }

                "uri" to string {
                    title = "Additional identifier URI"
                    destination =
                        destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/uri::string")
                    required = TRUE
                }
            }

            "contactPoint" to obj {
                title = "Contact Person"

                "person" to string {
                    title = "Contact person Full Name"
                    source = ocds {
                        value = path(
                            path = "/parties::array[0]/contactPoint::object/name::string",
                            usage = writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer()
                        )
                    }
                    destination =
                        destination("tender::object/procuringEntity::object/contactPoint::object/name::string")
                    required = TRUE
                }

                "contacts" to obj {
                    title = "Contact person contacts"

                    "telephone" to string {
                        title = "Telephone"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/contactPoint::object/telephone::string",
                                usage = writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/contactPoint::object/telephone::string")
                        required = TRUE
                    }

                    "email" to string {
                        title = "E-mail"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/contactPoint::object/email::string",
                                usage = writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/contactPoint::object/email::string")
                        required = TRUE
                    }

                    "url" to string {
                        title = "URL"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/contactPoint::object/url::string",
                                usage = writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/contactPoint::object/url::string")
                        required = TRUE
                    }

                    "faxNumber" to string {
                        title = "Fax number"
                        source = ocds {
                            value = path(
                                path = "/parties::array[0]/contactPoint::object/faxNumber::string",
                                usage = writeDataIfProcuringEntityIsBuyerAndResponsibleContactPersonIsBuyer()
                            )
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/contactPoint::object/faxNumber::string")
                    }
                }
            }
        }
    }

    "section2" to obj {
        title = "Section II: Object"

        "procurementScope" to obj {
            title = "Scope of procurement"
            description = "II.1) Information about lots"

            "title" to string {
                title = "Title"
                description = "II.1.1) Title"
                destination = destination("tender::object/title::string")
                required = TRUE
            }

            "description" to string {
                title = "Short description"
                description = "II.1.4) Short description"
                destination = destination("tender::object/description::string")
                required = TRUE
            }
        }

        "documents" to arrayObjects {
            title = "Documents of Tender"
            description = "All documents and attachments related to the tender, including any notices"

            "file" to string {
                title = "Select file"
            }
            "documentId" to string {
                destination = destination("tender::object/documents::array[object]/id::string")
                required = TRUE
            }
            "title" to string {
                title = "The document title"
                destination = destination("tender::object/documents::array[object]/title::string")
            }
            "description" to string {
                title = "A short description of the document"
                destination = destination("tender::object/documents::array[object]/description::string")
            }
            "documentType" to string {
                title = "Document type"
                source = documentType(EntityKind.TENDER)
                destination = destination("tender::object/documents::array[object]/documentType::string")
                required = TRUE
            }
            "language" to string {
                title = "Language"
                source = mdmLanguage()
                destination = destination("tender::object/documents::array[object]/language::string")
                required = TRUE
            }
        }

        "lots" to arrayObjects {
            title = "Information about lots"
            description = "II.1.6) Information about lots"

            "description" to obj {
                title = "Description"
                description = "II.2.) Description"

                "lotId" to string {
                    destination = destination("tender::object/lots::array[object]/id::string")
                    required = TRUE
                }

                "title" to string {
                    title = "Lot Title"
                    description = "II.2.1) Title"
                    destination = destination("tender::object/lots::array[object]/title::string")
                    required = TRUE
                }

                "description" to string {
                    title = "Short description of lot"
                    description = "II.2.4) Description of the procurement"
                    destination = destination("tender::object/lots::array[object]/description::string")
                    required = TRUE
                }

                "value" to obj {
                    title = "Estimated total value of lot"
                    description = "II.2.6) Estimated value"

                    "amount" to number {
                        title = "Amount"
                        destination =
                            destination("tender::object/lots::array[object]/value::object/amount::number")
                        required = TRUE
                    }
                    "currency" to string {
                        title = "Currency"

                        source = manual {
                            val pathValue =
                                StringPathSource("/planning::object/budget::object/amount::object/currency::string")
                            default = { context ->
                                context.publicData[pathValue]
                            }
                            readOnly = TRUE
                        }
                        destination =
                            destination("tender::object/lots::array[object]/value::object/currency::string")
                        required = TRUE
                    }
                }
            }

            "performance" to obj {
                title = "Performance and Delivery"
                description = "II.2.3) Place of performance"

                "placeOfPerformance" to obj {
                    "address" to obj {
                        title = "Address"

                        "location" to obj {
                            title = "Location"

                            "countryName" to string {
                                title = "Country"
                                source = mdmCountries()
                                destination =
                                    destination("tender::object/lots::array[object]/placeOfPerformance::object/address::object/countryName::string")
                                required = TRUE
                            }

                            "region" to string {
                                title = "Region"
                                destination =
                                    destination("tender::object/lots::array[object]/placeOfPerformance::object/address::object/region::string")
                                required = TRUE
                            }

                            "locality" to string {
                                title = "Locality"
                                destination =
                                    destination("tender::object/lots::array[object]/placeOfPerformance::object/address::object/locality::string")
                                required = TRUE
                            }
                        }

                        "postalAddress" to obj {
                            title = "Postal Address"

                            "streetAddress" to string {
                                title = "Street Address"
                                destination =
                                    destination("tender::object/lots::array[object]/placeOfPerformance::object/address::object/streetAddress::string")
                                required = TRUE
                            }

                            "postalCode" to string {
                                title = "Postal Code"
                                destination =
                                    destination("tender::object/lots::array[object]/placeOfPerformance::object/address::object/postalCode::string")
                            }
                        }
                    }

                    "description" to string {
                        title = "Description"
                        description = "Further description of the place of performance of the contract"
                        destination =
                            destination("tender::object/lots::array[object]/placeOfPerformance::object/description::string")
                    }
                }

                "deliveryPeriod" to obj {
                    title = "Duration of the contract, FA or DPS"
                    description = "II.2.7) Duration of the contract, FA, DPS"

                    "startDate" to string {
                        title = "Delivery start date"
                        destination =
                            destination("tender::object/lots::array[object]/contractPeriod::object/startDate::string")
                        required = TRUE
                    }

                    "endDate" to string {
                        title = "Delivery end date"
                        destination =
                            destination("tender::object/lots::array[object]/contractPeriod::object/endDate::string")
                        required = TRUE
                    }
                }
            }

            "items" to arrayObjects {
                title = "Nomenclature of lot"

                "itemId" to string {
                    destination = destination("tender::object/items::array[object]/id::string")
                    required = TRUE
                }

                "description" to string {
                    title = "Short description of item"
                    destination = destination("tender::object/items::array[object]/description::string")
                    required = TRUE
                }

                "quantity" to obj {
                    title = "Quantity"

                    "quantity" to number {
                        title = "Quantity"
                        destination = destination("tender::object/items::array[object]/quantity::number")
                        required = TRUE
                    }

                    "unit" to obj {
                        title = "Unit"

                        "class" to string {
                            title = "Class of unit"
                            source = mdmUnitClasses()
                            required = TRUE
                        }
                        "id" to string {
                            title = "Unit id"
                            destination =
                                destination("tender::object/items::array[object]/unit::object/id::string")
                            required = TRUE
                        }

                        "name" to string {
                            title = "Unit name"
                            destination =
                                destination("tender::object/items::array[object]/unit::object/name::string")
                            required = TRUE
                        }
                    }
                }

                "classification" to obj {
                    title = "Main CPV code"

                    "scheme" to string {
                        title = "Classification scheme"
                        source = manual {
                            default = { "CPV" }
                        }
                        destination =
                            destination("tender::object/items::array[object]/classification::object/scheme::string")
                        required = TRUE
                    }

                    "id" to string {
                        title = "CPV code"
                        destination =
                            destination("tender::object/items::array[object]/classification::object/id::string")
                        required = TRUE
                    }

                    "description" to string {
                        title = "CPV name"
                        source = manual {
                            readOnly = TRUE
                        }
                        destination =
                            destination("tender::object/items::array[object]/classification::object/description::string")
                        required = TRUE
                    }

                    "mdm" to string {
                        //                        source = mbmUrl {
//                            kind = MDMKind.CPV
//                        }
//                        destination = emptyPathDestination()
                    }
                }

                "additionalClassification" to arrayObjects {
                    title = "Supplementary CPV code"

                    "scheme" to string {
                        title = "Classification scheme"
                        source = manual {
                            default = { "CPVs" }
                        }
                        destination =
                            destination("tender::object/items::array[object]/classification::object/additionalClassification::array[object]/scheme::string")
                        required = TRUE
                    }

                    "id" to string {
                        title = "CPVs code"
                        destination =
                            destination("tender::object/items::array[object]/classification::object/additionalClassification::array[object]/id::string")
                        required = TRUE
                    }

                    "description" to string {
                        title = "CPVs Name"
                        destination =
                            destination("tender::object/items::array[object]/classification::object/additionalClassification::array[object]/description::string")
                        required = TRUE
                    }

                    "mdm" to string {
                        //                        source = mbmUrl {
//                            kind = MDMKind.CPV
//                        }
//                        destination = emptyPathDestination()
                    }
                }

                "relatedLot" to string {
                    destination = destination("tender::object/items::array[object]/relatedLot::string")
                    required = TRUE
                }
            }

            "documents" to arrayObjects {
                title = "Documents of Lot"

                "file" to string {
                    title = "Select file"
                }

                "documentId" to string {
                    destination = destination("tender::object/documents::array[object]/id::string")
                    required = TRUE
                }

                "title" to string {
                    title = "The document title"
                    destination = destination("tender::object/documents::array[object]/title::string")
                }

                "description" to string {
                    title = "A short description of the document"
                    destination = destination("tender::object/documents::array[object]/description::string")
                }

                "documentType" to string {
                    title = "Document type"
                    source = documentType(EntityKind.TENDER)
                    destination = destination("tender::object/documents::array[object]/documentType::string")
                    required = TRUE
                }

                "language" to string {
                    title = "Language"
                    source = mdmLanguage()
                    destination = destination("tender::object/documents::array[object]/language::string")
                    required = TRUE
                }

                "relatedLots" to arrayStrings {
                    minItems = 1
                    destination =
                        destination("tender::object/documents::array[object]/relatedLots::array[string]")
                }
            }
        }
    }

    "section4" to obj {
        title = "Section IV: Procedure"

        "method" to obj {
            "procurementMethodDetails" to string {
                title = "Type of Procurement Procedure"
                description = "IV.1.1) Type of procedure"
                source = manual {
                    value = { context -> context.parameters[CnNamesParameters.PMD].value }
                    readOnly = TRUE
                }
                destination = destination("tender::object/procurementMethodDetails::string")
            }

            "legalBasis" to string {
                title = "Legal base coveres"
                description = "IV.1.8) Information about the GPA"
                source = enum {
                    +Section4.Method.LegalBasis.DIRECTIVE_2014_23_EU
                    +Section4.Method.LegalBasis.DIRECTIVE_2014_24_EU
                    +Section4.Method.LegalBasis.DIRECTIVE_2014_25_EU
                    +Section4.Method.LegalBasis.DIRECTIVE_2009_81_EC
                    +Section4.Method.LegalBasis.REGULATION_966_2012
                    +Section4.Method.LegalBasis.NATIONAL_PROCUREMENT_LAW
                    +Section4.Method.LegalBasis.NULL
                }
                destination = destination("tender::object/legalBasis::string")
                required = TRUE
            }
        }

        "administrative" to obj {
            title = "Administrative information"
            description = "IV.2) Administrative information"

            "validityPeriod" to obj {
                "endDate" to string {
                    title = "Tender end date"
                    destination = destination("tender::object/tenderPeriod::object/endDate::string")
                    required = TRUE
                }
            }

            "submissionLanguage" to arrayStrings {
                title = "Languages of submission"
                description = "IV.2.4) Languages in which may be submitted"
                minItems = 1
                uniqueItems = true
                source = mdmLanguage()
                destination = destination("tender::object/submissionLanguages::array[string]")
            }
        }
    }

    "budget" to obj {
        "budgetBreakdown" to arrayObjects {
            "id" to string {
                destination = destination("planning::object/budget::object/budgetBreakdown::array[object]/id::string")
            }

            "budgetBreakdownAmount" to obj {
                "amount" to number {
                    destination =
                        destination("planning::object/budget::object/budgetBreakdown::array[object]/amount::object/amount::number")
                }

                "currency" to string {
                    source = ocds {
                        default =
                            path("/planning::object/budget::object/amount::object/currency::string")
                    }
                    destination =
                        destination("planning::object/budget::object/budgetBreakdown::array[object]/amount::object/currency::string")
                }
            }

        }
    }
}.build("")
