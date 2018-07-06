package com.procurement.formsservice.definition.fs

import com.procurement.formsservice.json.Context
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.attribute.boolean
import com.procurement.formsservice.json.attribute.number
import com.procurement.formsservice.json.attribute.string
import com.procurement.formsservice.json.container.arrayObjects
import com.procurement.formsservice.json.container.obj
import com.procurement.formsservice.json.data.mdm.mdmCountries
import com.procurement.formsservice.json.data.mdm.mdmRegistrationScheme

fun funderIsBuyer(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.FUNDER] == FsFunderValues.BUYER
}

fun funderIsDonor(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.FUNDER] == FsFunderValues.DONOR
}

fun payerIsBuyer(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.PAYER] == FsPayerValues.BUYER
}

fun payerIsThirdPart(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.PAYER] == FsPayerValues.THIRD_PARTY
}

fun payerIsFunder(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.PAYER] == FsPayerValues.FOUNDER
}

fun isEuropeanUnionFunded(): (context: Context) -> Boolean = {
    it.parameters[FsNamesParameters.IS_EU_FUNDED] == FsIsEuropeanUnionFundedValues.TRUE
}

fun fsDefinition() = obj {
    title = "Funding Source Creation"

    "fsParent" to obj {
        title = "New Source of Financing for"
        description = "Readonly information about parent Expenditure Item"

        "eiOCID" to string {
            title = "ID"
            source = ocds {
                value = path("/ocid::string")
                readOnly = TRUE
            }
        }

        "eiTitle" to string {
            title = "Expenditure Item"
            source = ocds {
                value = path("/tender::object/title::string")
                readOnly = TRUE
            }
        }

        "eiDescription" to string {
            title = "Description"
            source = ocds {
                value = path("/tender::object/description::string")
                readOnly = TRUE
            }
        }

        "eiBudgetPeriod" to obj {
            title = "Procuring Period"

            "startDate" to string {
                title = "Start Date"
                source = ocds {
                    value = path("/planning::object/budget::object/period::object/startDate::string")
                    readOnly = TRUE
                }
            }

            "endDate" to string {
                title = "End date"
                source = ocds {
                    value = path("/planning::object/budget::object/period::object/endDate::string")
                    readOnly = TRUE
                }
            }
        }

        "eiClassification" to obj {
            title = "eiClassification"

            "scheme" to string {
                title = "Classification schema"
                source = ocds {
                    value = path("/tender::object/classification::object/scheme::string")
                    readOnly = TRUE
                }
            }

            "id" to string {
                title = "CPV code"
                source = ocds {
                    value = path("/tender::object/classification::object/id::string")
                    readOnly = TRUE
                }
            }

            "description" to string {
                title = "CPV name"
                source = ocds {
                    value = path("/tender::object/classification::object/description::string")
                    readOnly = TRUE
                }
            }
        }
    }

    "fsFunding" to obj {
        title = "Funding"
        description = "Definition of Entity to be included in POST request as 'buyer'"

        alternative(condition = funderIsBuyer()) {
            "fsFunder" to obj {
                title = "Own Money"
                description = "Budget provided by buyer from parent EI"

                "name" to string {
                    title = "Official Name"
                    source = ocds {
                        value = path("/parties::array[0]/name::string")
                    }
                    destination = destination("buyer::object/name::string")
                }

                "address" to obj {
                    title = "Address"

                    "location" to obj {
                        title = "Location"

                        "countryName" to string {
                            title = "Country"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/countryName::string")
                            }
                            destination = destination("buyer::object/address::object/countryName::string")
                            required = TRUE
                        }

                        "region" to string {
                            title = "Region"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/region::string")
                            }
                            destination = destination("buyer::object/address::object/region::string")
                            required = TRUE
                        }

                        "locality" to string {
                            title = "Locality"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/locality::string")
                            }
                            destination = destination("buyer::object/address::object/locality::string")
                            required = TRUE
                        }
                    }

                    "postalAddress" to obj {
                        title = "Postal Address"

                        "streetAddress" to string {
                            title = "Street Address"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/streetAddress::string")
                            }
                            destination = destination("buyer::object/address::object/streetAddress::string")
                            required = TRUE
                        }

                        "postalCode" to string {
                            title = "Postal Code"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/postalCode::string")
                            }
                            destination = destination("buyer::object/address::object/postalCode::string")
                        }
                    }
                }

                "identifier" to obj {
                    title = "Main Identifier"

                    "id" to string {
                        title = "Identifier"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/id::string")
                        }
                        destination = destination("buyer::object/identifier::object/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Identification scheme"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/scheme::string")
                        }
                        destination = destination("buyer::object/identifier::object/scheme::string")
                        required = TRUE
                    }

                    "legalName" to string {
                        title = "Legal Name"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/legalName::string")
                        }
                        destination = destination("buyer::object/identifier::object/legalName::string")
                        required = TRUE
                    }

                    "uri" to string {
                        title = "URI"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/uri::string")
                        }
                        destination = destination("buyer::object/identifier::object/uri::string")
                        required = TRUE
                    }
                }

//                "additionalIdentifiers" to array {
//                    title = "Additional Identifiers"
//
//                    "id" to string {
//                        title = "Additional identifier ID"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/id::string")
//                        required = TRUE
//                    }
//
//                    "scheme" to string {
//                        title = "Additional identifier scheme"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/scheme::string")
//                        required = TRUE
//                    }
//
//                    "legalName" to string {
//                        title = "Additional identifier legal name"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/legalName::string")
//                        required = TRUE
//                    }
//
//                    "uri" to string {
//                        title = "Additional identifier URI"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/uri::string")
//                        required = TRUE
//                    }
//                }

                "contactPoint" to obj {
                    title = "Contact point"

                    "person" to obj {
                        title = "Contact person"

                        "name" to string {
                            title = "Name"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/name::string")
                            }
                            destination = destination("buyer::object/contactPoint::object/name::string")
                            required = TRUE
                        }

                        "url" to string {
                            title = "URL"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/url::string")
                            }
                            destination = destination("buyer::object/contactPoint::object/url::string")
                            required = TRUE
                        }
                    }

                    "contacts" to obj {
                        title = "Contact person"

                        "telephone" to string {
                            title = "Telephone"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/telephone::string")
                            }
                            destination = destination("buyer::object/contactPoint::object/telephone::string")
                            required = TRUE
                        }

                        "email" to string {
                            title = "E-mail"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/email::string")
                            }
                            destination = destination("buyer::object/contactPoint::object/email::string")
                            required = TRUE
                        }

                        "faxNumber" to string {
                            title = "Fax number"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/faxNumber::string")
                            }
                            destination = destination("buyer::object/contactPoint::object/faxNumber::string")
                        }
                    }
                }
            }
        }

        alternative(condition = funderIsDonor()) {
            "fsFunder" to obj {
                title = "Donors Money"
                description = "Budget provided by external Donor"

                "name" to string {
                    title = "Official Name"
                    destination = destination("buyer::object/name::string")
                }

                "address" to obj {
                    title = "Address"

                    "location" to obj {
                        title = "Location"

                        "countryName" to string {
                            title = "Country"
                            source = mdmCountries()
                            destination = destination("buyer::object/address::object/countryName::string")
                            required = TRUE
                        }

                        "region" to string {
                            title = "Region"
                            destination = destination("buyer::object/address::object/region::string")
                            required = TRUE
                        }

                        "locality" to string {
                            title = "Locality"
                            destination = destination("buyer::object/address::object/locality::string")
                            required = TRUE
                        }
                    }

                    "postalAddress" to obj {
                        title = "Postal Address"

                        "streetAddress" to string {
                            title = "Street Address"
                            destination = destination("buyer::object/address::object/streetAddress::string")
                            required = TRUE
                        }

                        "postalCode" to string {
                            title = "Postal Code"
                            destination = destination("buyer::object/address::object/postalCode::string")
                        }
                    }
                }

                "identifier" to obj {
                    title = "Main Identifier"

                    "id" to string {
                        title = "Identifier"
                        destination = destination("buyer::object/identifier::object/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Identification scheme"
                        source = mdmRegistrationScheme()
                        destination = destination("buyer::object/identifier::object/scheme::string")
                        required = TRUE
                    }

                    "legalName" to string {
                        title = "Legal Name"
                        destination = destination("buyer::object/identifier::object/legalName::string")
                        required = TRUE
                    }

                    "uri" to string {
                        title = "URI"
                        destination = destination("buyer::object/identifier::object/uri::string")
                        required = TRUE
                    }
                }

//                "additionalIdentifiers" to array {
//                    title = "Additional Identifiers"
//
//                    "id" to string {
//                        title = "Additional identifier ID"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/id::string")
//                        required = TRUE
//                    }
//
//                    "scheme" to string {
//                        title = "Additional identifier scheme"
//                        source = mdmRegistrationScheme()
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/scheme::string")
//                        required = TRUE
//                    }
//
//                    "legalName" to string {
//                        title = "Additional identifier legal name"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/legalName::string")
//                        required = TRUE
//                    }
//
//                    "uri" to string {
//                        title = "Additional identifier URI"
//                        destination =
//                            destination("buyer::object/additionalIdentifiers::array[object]/uri::string")
//                        required = TRUE
//                    }
//                }

                "contactPoint" to obj {
                    title = "Contact point"

                    "person" to obj {
                        title = "Contact person"

                        "name" to string {
                            title = "Name"
                            destination = destination("buyer::object/contactPoint::object/name::string")
                            required = TRUE
                        }

                        "url" to string {
                            title = "URL"
                            destination = destination("buyer::object/contactPoint::object/url::string")
                            required = TRUE
                        }
                    }

                    "contacts" to obj {
                        title = "Contact person"

                        "telephone" to string {
                            title = "Telephone"
                            destination = destination("buyer::object/contactPoint::object/telephone::string")
                            required = TRUE
                        }

                        "email" to string {
                            title = "E-mail"
                            destination = destination("buyer::object/contactPoint::object/email::string")
                            required = TRUE
                        }

                        "faxNumber" to string {
                            title = "Fax number"
                            destination = destination("buyer::object/contactPoint::object/faxNumber::string")
                        }
                    }
                }
            }
        }

        alternative(condition = payerIsBuyer()) {
            "fsPayer" to obj {
                title = "Buyer"
                description =
                    "Payments will be provided by buyer from parent EI (via Treasury or directly). In that case 'buyer' in POST request should be empty. eNotice will take needed organization data from EI by itself"

                "name" to string {
                    title = "Official Name"
                    source = ocds {
                        value = path("/parties::array[0]/name::string")
                    }
                    destination = destination("tender::object/procuringEntity::object/name::string")
                }

                "address" to obj {
                    title = "Address"

                    "location" to obj {
                        title = "Location"

                        "countryName" to string {
                            title = "Country"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/countryName::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/countryName::string")
                            required = TRUE
                        }

                        "region" to string {
                            title = "Region"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/region::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/region::string")
                            required = TRUE
                        }

                        "locality" to string {
                            title = "Locality"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/locality::string")
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
                                value = path("/parties::array[0]/address::object/streetAddress::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/streetAddress::string")
                            required = TRUE
                        }

                        "postalCode" to string {
                            title = "Postal Code"
                            source = ocds {
                                value = path("/parties::array[0]/address::object/postalCode::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/postalCode::string")
                        }
                    }
                }

                "identifier" to obj {
                    title = "Main Identifier"

                    "id" to string {
                        title = "Identifier"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/id::string")
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Identification scheme"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/scheme::string")
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/scheme::string")
                        required = TRUE
                    }

                    "legalName" to string {
                        title = "Legal Name"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/legalName::string")
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/legalName::string")
                        required = TRUE
                    }

                    "uri" to string {
                        title = "URI"
                        source = ocds {
                            value = path("/parties::array[0]/identifier::object/uri::string")
                        }
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/uri::string")
                        required = TRUE
                    }
                }

                "additionalIdentifiers" to arrayObjects {
                    title = "Additional Identifiers"

                    "id" to string {
                        title = "Additional identifier ID"
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Additional identifier scheme"
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/scheme::string")
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
                    title = "Contact point"

                    "person" to obj {
                        title = "Contact person"

                        "name" to string {
                            title = "Name"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/name::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/name::string")
                            required = TRUE
                        }

                        "url" to string {
                            title = "URL"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/url::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/url::string")
                            required = TRUE
                        }
                    }

                    "contacts" to obj {
                        title = "Contact person"

                        "telephone" to string {
                            title = "Telephone"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/telephone::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/telephone::string")
                            required = TRUE
                        }

                        "email" to string {
                            title = "E-mail"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/email::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/email::string")
                            required = TRUE
                        }

                        "faxNumber" to string {
                            title = "Fax number"
                            source = ocds {
                                value = path("/parties::array[0]/contactPoint::object/faxNumber::string")
                            }
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/faxNumber::string")
                        }
                    }
                }
            }
        }

        alternative(condition = payerIsThirdPart()) {
            "fsPayer" to obj {
                title = "Third Party"
                description = "Payments will be provided by Treasury"

                "name" to string {
                    title = "Official Name"
                    destination = destination("tender::object/procuringEntity::object/name::string")
                }

                "address" to obj {
                    title = "Address"

                    "location" to obj {
                        title = "Location"

                        "countryName" to string {
                            title = "Country"
                            source = mdmCountries()
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/countryName::string")
                            required = TRUE
                        }

                        "region" to string {
                            title = "Region"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/region::string")
                            required = TRUE
                        }

                        "locality" to string {
                            title = "Locality"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/locality::string")
                            required = TRUE
                        }
                    }

                    "postalAddress" to obj {
                        title = "Postal Address"

                        "streetAddress" to string {
                            title = "Street Address"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/streetAddress::string")
                            required = TRUE
                        }

                        "postalCode" to string {
                            title = "Postal Code"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/postalCode::string")
                        }
                    }
                }

                "identifier" to obj {
                    title = "Main Identifier"

                    "id" to string {
                        title = "Identifier"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Identification scheme"
                        source = mdmRegistrationScheme()
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/scheme::string")
                        required = TRUE
                    }

                    "legalName" to string {
                        title = "Legal Name"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/legalName::string")
                        required = TRUE
                    }

                    "uri" to string {
                        title = "URI"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/uri::string")
                        required = TRUE
                    }
                }

                "additionalIdentifiers" to arrayObjects {
                    title = "Additional Identifiers"

                    "id" to string {
                        title = "Additional identifier ID"
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Additional identifier scheme"
                        source = mdmRegistrationScheme()
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/scheme::string")
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
                    title = "Contact point"

                    "person" to obj {
                        title = "Contact person"

                        "name" to string {
                            title = "Name"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/name::string")
                            required = TRUE
                        }

                        "url" to string {
                            title = "URL"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/url::string")
                            required = TRUE
                        }
                    }

                    "contacts" to obj {
                        title = "Contact person"

                        "telephone" to string {
                            title = "Telephone"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/telephone::string")
                            required = TRUE
                        }

                        "email" to string {
                            title = "E-mail"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/email::string")
                            required = TRUE
                        }

                        "faxNumber" to string {
                            title = "Fax number"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/faxNumber::string")
                        }
                    }
                }
            }
        }

        alternative(condition = payerIsFunder()) {
            "fsPayer" to obj {
                title = "Funder"
                description = "Payments will be provided by funder"

                "name" to string {
                    title = "Official Name"
                    destination = destination("tender::object/procuringEntity::object/name::string")
                }

                "address" to obj {
                    title = "Address"

                    "location" to obj {
                        title = "Location"

                        "countryName" to string {
                            title = "Country"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/countryName::string")
                            required = TRUE
                        }

                        "region" to string {
                            title = "Region"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/region::string")
                            required = TRUE
                        }

                        "locality" to string {
                            title = "Locality"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/locality::string")
                            required = TRUE
                        }
                    }

                    "postalAddress" to obj {
                        title = "Postal Address"

                        "streetAddress" to string {
                            title = "Street Address"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/streetAddress::string")
                            required = TRUE
                        }

                        "postalCode" to string {
                            title = "Postal Code"
                            destination =
                                destination("tender::object/procuringEntity::object/address::object/postalCode::string")
                        }
                    }
                }

                "identifier" to obj {
                    title = "Main Identifier"

                    "id" to string {
                        title = "Identifier"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Identification scheme"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/scheme::string")
                        required = TRUE
                    }

                    "legalName" to string {
                        title = "Legal Name"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/legalName::string")
                        required = TRUE
                    }

                    "uri" to string {
                        title = "URI"
                        destination =
                            destination("tender::object/procuringEntity::object/identifier::object/uri::string")
                        required = TRUE
                    }
                }

                "additionalIdentifiers" to arrayObjects {
                    title = "Additional Identifiers"

                    "id" to string {
                        title = "Additional identifier ID"
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/id::string")
                        required = TRUE
                    }

                    "scheme" to string {
                        title = "Additional identifier scheme"
                        destination =
                            destination("tender::object/procuringEntity::object/additionalIdentifiers::array[object]/scheme::string")
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
                    title = "Contact point"

                    "person" to obj {
                        title = "Contact person"

                        "name" to string {
                            title = "Name"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/name::string")
                            required = TRUE
                        }

                        "url" to string {
                            title = "URL"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/url::string")
                            required = TRUE
                        }
                    }

                    "contacts" to obj {
                        title = "Contact person"

                        "telephone" to string {
                            title = "Telephone"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/telephone::string")
                            required = TRUE
                        }

                        "email" to string {
                            title = "E-mail"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/email::string")
                            required = TRUE
                        }

                        "faxNumber" to string {
                            title = "Fax number"
                            destination =
                                destination("tender::object/procuringEntity::object/contactPoint::object/faxNumber::string")
                        }
                    }
                }
            }
        }
    }

    "fsBudget" to obj {
        title = "Budget"

        "rationale" to string {
            title = "Rationale"
            description = "rationale description"
            destination = destination("planning::object/rationale::string")
        }

        "budgetDetails" to obj {
            title = "Budget details"


            "description" to string {
                title = "Description"
                description = "A short free text description of the budget source"
                destination = destination("planning::object/budget::object/description::string")
            }

            "id" to string {
                title = "Budget ID"
                description = "An identifier for the budget line item"
                destination = destination("planning::object/budget::object/id::string")
            }
        }

        "budgetAmount" to obj {
            title = "Budget Amount"

            "amount" to number {
                title = "Value"
                description = "Value description"
                destination = destination("planning::object/budget::object/amount::object/amount::number")
                required = TRUE
            }
            "currency" to string {
                title = "Currency"
                description = "Currency description"

                source = ocds {
                    value = path("/planning::object/budget::object/amount::object/currency::string")
                    readOnly = TRUE
                }
                destination = destination("planning::object/budget::object/amount::object/currency::string")
                required = TRUE
            }
        }

        "budgetPeriod" to obj {
            title = "Budget Period"

            "startDate" to string {
                title = "From"
                destination = destination("planning::object/budget::object/period::object/startDate::string")
                required = TRUE
            }

            "endDate" to string {
                title = "To"
                destination = destination("planning::object/budget::object/period::object/endDate::string")
                required = TRUE
            }
        }

        "budgetProject" to obj {
            title = "Project"

            "projectDetails" to obj {
                title = "Project Details"

                "EUfunded" to obj {
                    "isEUfunded" to boolean {
                        title = "Is European Union Funded"
                        source = enum {
                            +FsBudget.BudgetProject.EUfunded.IsEUfunded.YES
                            +FsBudget.BudgetProject.EUfunded.IsEUfunded.NO

                            value = { it.parameters[FsNamesParameters.IS_EU_FUNDED].value }
                            readOnly = TRUE
                        }
                        destination =
                            destination("planning::object/budget::object/isEuropeanUnionFunded::boolean")
                        required = TRUE
                    }

                    alternative(condition = isEuropeanUnionFunded()) {
                        "projectIdentifier" to string {
                            title = "Project Identifier"
                            description = "National identifier of the EU project providing partial or full funding"
                            destination =
                                destination("planning::object/budget::object/europeanUnionFunding::object/projectIdentifier::string")
                            required = TRUE
                        }

                        "projectName" to string {
                            title = "Project Name"
                            description =
                                "Name or other national identification of the project providing full or partial funding."
                            destination =
                                destination("planning::object/budget::object/europeanUnionFunding::object/projectName::string")
                            required = TRUE
                        }

                        "uri" to string {
                            title = "Project URI"
                            description = "URI of the project providing full or partial funding."
                            destination =
                                destination("planning::object/budget::object/europeanUnionFunding::object/uri::string")
                        }
                    }
                }
            }
        }
    }
}.build("")
