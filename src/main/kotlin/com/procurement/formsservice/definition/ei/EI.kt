package com.procurement.formsservice.definition.ei

import com.procurement.formsservice.definition.CommonNamesParameters
import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.attribute.boolean
import com.procurement.formsservice.json.attribute.string
import com.procurement.formsservice.json.container.arrayObjects
import com.procurement.formsservice.json.container.obj
import com.procurement.formsservice.json.data.mdm.MDMKind
import com.procurement.formsservice.json.data.mdm.mdmCurrency
import com.procurement.formsservice.json.data.mdm.mdmRegions
import com.procurement.formsservice.json.data.mdm.mdmRegistrationScheme
import com.procurement.formsservice.json.data.source.url.mbmUrl

fun eiDefinition() = obj {
    "eiSubject" to obj {
        title = "Subject of expenditures"

        "title" to string {
            title = "Title"
            destination = destination("tender::object/title::string")
            required = TRUE
        }

        "description" to string {
            title = "Short description"
            destination = destination("tender::object/description::string")
        }

        "period" to obj {
            title = "Period of procuring"

            "startDate" to string {
                title = "From"
                destination = destination("planning::object/budget::object/period::object/startDate::string")
                required = TRUE
            }

            "endDate" to string {
                title = "Till"
                destination = destination("planning::object/budget::object/period::object/endDate::string")
                required = TRUE
            }
        }

        "parameters" to obj {
            title = "Parameters"

            "currency" to string {
                title = "Currency of fundings"
                source = mdmCurrency()
                destination = destination("planning::object/budget::object/amount::object/currency::string")
                required = TRUE
            }

            "classification" to obj {
                title = "Main CPV code"

                "scheme" to string {
                    title = "Classification scheme"
                    source = manual {
                        default = { "CPV" }
                    }
                    destination = destination("tender::object/classification::object/scheme::string")
                    required = TRUE
                }

                "id" to string {
                    title = "CPV code"
                    destination = destination("tender::object/classification::object/id::string")
                    required = TRUE
                }

                "description" to string {
                    title = "CPV name"
                    destination = destination("tender::object/classification::object/description::string")
                    required = TRUE
                }

                "mdm" to string {
                    source = mbmUrl {
                        kind = MDMKind.CPV
                    }
                    required = TRUE
                }
            }
        }

        "rationale" to string {
            title = "Rationale"
            destination = destination("planning::object/rationale::string")
        }
    }

    "eiBuyer" to obj {
        title = "Owner of Budget"
        description = "Information about Entity who will operate budget from this Expenditure Item"

        "name" to string {
            title = "Official Name"
            destination = destination("buyer::object/name::string")
            required = TRUE
        }

        "address" to obj {
            title = "Address"

            "location" to obj {
                title = "Location"

                "countryName" to string {
                    title = "Country"
                    source = manual {
                        value = { it.parameters[CommonNamesParameters.COUNTRY].value }
                        readOnly = TRUE
                    }
                    destination = destination("buyer::object/address::object/countryName::string")
                    required = TRUE
                }

                "region" to string {
                    title = "Region"
                    source = mdmRegions()
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

            "scheme" to string {
                title = "Identification scheme"
                source = manual {
                    value = { it.parameters[EiNamesParameters.IDENTIFIER_SCHEMA].value }
                    readOnly = TRUE
                }
                destination = destination("buyer::object/identifier::object/scheme::string")
                required = TRUE
            }

            "id" to string {
                title = "Identifier"
                destination = destination("buyer::object/identifier::object/id::string")
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

        "additionalIdentifiers" to arrayObjects {
            title = "Additional Identifiers"

            "scheme" to string {
                title = "Additional identifier scheme"
                source = mdmRegistrationScheme()
                destination = destination("buyer::object/additionalIdentifiers::array[object]/scheme::string")
                required = TRUE
            }

            "id" to string {
                title = "Additional identifier ID"
                destination = destination("buyer::object/additionalIdentifiers::array[object]/id::string")
                required = TRUE
            }

            "legalName" to string {
                title = "Additional identifier legal name"
                destination = destination("buyer::object/additionalIdentifiers::array[object]/legalName::string")
                required = TRUE
            }

            "uri" to string {
                title = "Additional identifier URI"
                destination = destination("buyer::object/additionalIdentifiers::array[object]/uri::string")
                required = TRUE
            }
        }

        "details" to obj {
            title = "Details"

            "typeOfBuyer" to string {
                title = "Type of buyer"
                source = enum {
                    +EiBuyer.Details.TypeOfBuyer.BODY_PUBLIC
                    +EiBuyer.Details.TypeOfBuyer.EU_INSTITUTION
                    +EiBuyer.Details.TypeOfBuyer.MINISTRY
                    +EiBuyer.Details.TypeOfBuyer.NATIONAL_AGENCY
                    +EiBuyer.Details.TypeOfBuyer.REGIONAL_AGENCY
                    +EiBuyer.Details.TypeOfBuyer.REGIONAL_AUTHORITY
                }
                destination = destination("buyer::object/details::object/typeOfBuyer::string")
                required = TRUE
            }

            "mainGeneralActivity" to string {
                title = "Main general activity"
                source = enum {
                    +EiBuyer.Details.MainGeneralActivity.DEFENCE
                    +EiBuyer.Details.MainGeneralActivity.ECONOMIC_AND_FINANCIAL_AFFAIRS
                    +EiBuyer.Details.MainGeneralActivity.EDUCATION
                    +EiBuyer.Details.MainGeneralActivity.ENVIRONMENT
                    +EiBuyer.Details.MainGeneralActivity.GENERAL_PUBLIC_SERVICES
                    +EiBuyer.Details.MainGeneralActivity.HEALTH
                    +EiBuyer.Details.MainGeneralActivity.HOUSING_AND_COMMUNITY_AMENITIES
                    +EiBuyer.Details.MainGeneralActivity.PUBLIC_ORDER_AND_SAFETY
                    +EiBuyer.Details.MainGeneralActivity.RECREATION_CULTURE_AND_RELIGION
                    +EiBuyer.Details.MainGeneralActivity.SOCIAL_PROTECTION
                }
                destination = destination("buyer::object/details::object/mainGeneralActivity::string")
                required = TRUE
            }

            "mainSectoralActivity" to string {
                title = "Main sectoral activity"
                source = enum {
                    +EiBuyer.Details.MainSectoralActivity.AIRPORT_RELATED_ACTIVITIES
                    +EiBuyer.Details.MainSectoralActivity.ELECTRICITY
                    +EiBuyer.Details.MainSectoralActivity.EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL
                    +EiBuyer.Details.MainSectoralActivity.EXPLORATION_EXTRACTION_GAS_OIL
                    +EiBuyer.Details.MainSectoralActivity.PORT_RELATED_ACTIVITIES
                    +EiBuyer.Details.MainSectoralActivity.POSTAL_SERVICES
                    +EiBuyer.Details.MainSectoralActivity.PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT
                    +EiBuyer.Details.MainSectoralActivity.RAILWAY_SERVICES
                    +EiBuyer.Details.MainSectoralActivity.URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES
                    +EiBuyer.Details.MainSectoralActivity.WATER
                }
                destination = destination("buyer::object/details::object/mainSectoralActivity::string")
                required = TRUE
            }

            "isACentralPurchasingBody" to boolean {
                title = "Is a central purchasing body"
                source = manual {
                    value = FALSE
                    readOnly = TRUE
                }
                destination = destination("buyer::object/details::object/isACentralPurchasingBody::boolean")
            }

            "scale" to string {
                title = "Scale"
                source = enum {
                    +EiBuyer.Details.Scale.MICRO
                    +EiBuyer.Details.Scale.SME
                    +EiBuyer.Details.Scale.LARGE
                }
                destination = destination("buyer::object/details::object/scale::string")
                required = TRUE
            }
        }

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
                title = "Contact person contacts"

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
}.build("")
