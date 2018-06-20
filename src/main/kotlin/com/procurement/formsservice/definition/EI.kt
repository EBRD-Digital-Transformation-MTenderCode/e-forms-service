package com.procurement.formsservice.definition

import com.procurement.formsservice.json.FALSE
import com.procurement.formsservice.json.Parameters.Companion.IDENTIFIER_SCHEMA
import com.procurement.formsservice.json.TRUE
import com.procurement.formsservice.json.attribute.StringAttributeDefinition
import com.procurement.formsservice.json.attribute.boolean
import com.procurement.formsservice.json.attribute.string
import com.procurement.formsservice.json.container.array
import com.procurement.formsservice.json.container.obj
import com.procurement.formsservice.json.data.enumerator.StringEnumElementDefinition
import com.procurement.formsservice.json.data.enumerator.StringEnumElementsDefinition
import com.procurement.formsservice.json.data.mdm.mdmCPV
import com.procurement.formsservice.json.data.mdm.mdmCurrency
import com.procurement.formsservice.json.data.source.StringEnumSourceDefinition
import com.procurement.formsservice.json.data.source.manual.manualBoolean
import com.procurement.formsservice.json.data.source.manual.manualString

object EiSubject {
    object Classification {
        object Scheme {
            val CPV = StringEnumElementDefinition("CPV")
        }
    }
}

object EiBuyer {
    object Details {
        object TypeOfBuyer {
            val BODY_PUBLIC = StringEnumElementDefinition("BODY_PUBLIC")
            val EU_INSTITUTION = StringEnumElementDefinition("EU_INSTITUTION")
            val MINISTRY = StringEnumElementDefinition("MINISTRY")
            val NATIONAL_AGENCY = StringEnumElementDefinition("NATIONAL_AGENCY")
            val REGIONAL_AGENCY = StringEnumElementDefinition("REGIONAL_AGENCY")
            val REGIONAL_AUTHORITY = StringEnumElementDefinition("REGIONAL_AUTHORITY")
        }

        object MainGeneralActivity {
            val DEFENCE = StringEnumElementDefinition("DEFENCE")
            val ECONOMIC_AND_FINANCIAL_AFFAIRS =
                StringEnumElementDefinition("ECONOMIC_AND_FINANCIAL_AFFAIRS")
            val EDUCATION = StringEnumElementDefinition("EDUCATION")
            val ENVIRONMENT = StringEnumElementDefinition("ENVIRONMENT")
            val GENERAL_PUBLIC_SERVICES =
                StringEnumElementDefinition("GENERAL_PUBLIC_SERVICES")
            val HEALTH = StringEnumElementDefinition("HEALTH")
            val HOUSING_AND_COMMUNITY_AMENITIES =
                StringEnumElementDefinition("HOUSING_AND_COMMUNITY_AMENITIES")
            val PUBLIC_ORDER_AND_SAFETY =
                StringEnumElementDefinition("PUBLIC_ORDER_AND_SAFETY")
            val RECREATION_CULTURE_AND_RELIGION =
                StringEnumElementDefinition("RECREATION_CULTURE_AND_RELIGION")
            val SOCIAL_PROTECTION = StringEnumElementDefinition("SOCIAL_PROTECTION")
        }

        object MainSectoralActivity {
            val AIRPORT_RELATED_ACTIVITIES =
                StringEnumElementDefinition("AIRPORT_RELATED_ACTIVITIES")
            val ELECTRICITY = StringEnumElementDefinition("ELECTRICITY")
            val EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL =
                StringEnumElementDefinition("EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL")
            val EXPLORATION_EXTRACTION_GAS_OIL =
                StringEnumElementDefinition("EXPLORATION_EXTRACTION_GAS_OIL")
            val PORT_RELATED_ACTIVITIES =
                StringEnumElementDefinition("PORT_RELATED_ACTIVITIES")
            val POSTAL_SERVICES = StringEnumElementDefinition("POSTAL_SERVICES")
            val PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT =
                StringEnumElementDefinition("PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT")
            val RAILWAY_SERVICES = StringEnumElementDefinition("RAILWAY_SERVICES")
            val URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES =
                StringEnumElementDefinition("URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES")
            val WATER = StringEnumElementDefinition("WATER")
        }

        object Scale {
            val MICRO = StringEnumElementDefinition("micro")
            val SME = StringEnumElementDefinition("sme")
            val LARGE = StringEnumElementDefinition("large")
        }
    }
}

val eiDefinition = obj {
    title = "Expenditure Item Creation"
    "eiSubject" to obj {
        title = "Subject of Expenditure Item"

        "title" to string {
            title = "Title (definition of type of object)"
            destination = "[tender][title]"
            required = TRUE
        }

        "classification" to obj {
            title = "Main CPV code"

            "scheme" to string {
                title = "Classification scheme"
                destination = "[tender][classification][scheme]"
                source = StringEnumSourceDefinition(
                    enum = StringEnumElementsDefinition(
                        EiSubject.Classification.Scheme.CPV
                    )
                )
                required = TRUE
            }
            "id" to string {
                title = "CPV code"
                destination = "[tender][classification][id]"
                source = mdmCPV()
                required = TRUE
            }
            "description" to string {
                title = "CPV name"
                source = manualString { readOnly = TRUE }
                destination = "[tender][classification][description]"
                required = TRUE

            }
        }

        "description" to string {
            title = "Short description"
            destination = "[tender][description]"
        }

        "period" to obj {
            title = "Period"

            "startDate" to string {
                title = "Start date"
                format = StringAttributeDefinition.Format.DATE_TIME
                destination = "[planning][budget][period][startDate]"
                required = TRUE
            }
            "endDate" to string {
                title = "End date"
                format = StringAttributeDefinition.Format.DATE_TIME
                destination = "[planning][budget][period][endDate]"
                required = TRUE
            }
        }

        "amount" to obj {
            title = "Amount"

            "currency" to string {
                title = "Currency"
                source = mdmCurrency()
                destination = "[planning][budget][amount][currency]"
                required = TRUE
            }
        }

        "rationale" to string {
            title = "Rationale"
            destination = "[planning][rationale]"
        }
    }

    "eiBuyer" to obj {
        title = "Owner of Budget"
        description = "Information about Entity who will operate budget from this Expenditure Item"

        "name" to string {
            title = "Official Name"
            destination = "[buyer][name]"
            required = TRUE
        }

        "address" to obj {
            title = "Address"

            "location" to obj {
                title = "Location"

                "countryName" to string {
                    title = "Country"
                    destination = "[buyer][address][countryName]"
                    required = TRUE
                }

                "region" to string {
                    title = "Region"
                    destination = "[buyer][address][region]"
                    required = TRUE
                }

                "locality" to string {
                    title = "Locality"
                    destination = "[buyer][address][locality]"
                    required = TRUE
                }
            }

            "postalAddress" to obj {
                title = "Postal Address"

                "streetAddress" to string {
                    title = "Street Address"
                    destination = "[buyer][address][streetAddress]"
                    required = TRUE
                }

                "postalCode" to string {
                    title = "Postal Code"
                    destination = "[buyer][address][postalCode]"
                }
            }
        }

        "identifier" to obj {
            title = "Main Identifier"

            "id" to string {
                title = "Identifier"
                destination = "[buyer][identifier][id]"
                required = TRUE
            }

            "scheme" to string {
                title = "Identification scheme"
                source = manualString {
                    value = { it.parameters.getAsString(IDENTIFIER_SCHEMA) }
                }
                destination = "[buyer][identifier][scheme]"
                required = TRUE
            }

            "legalName" to string {
                title = "Legal Name"
                destination = "[buyer][identifier][legalName]"
                required = TRUE
            }

            "uri" to string {
                title = "URI"
                destination = "[buyer][identifier][uri]"
                required = TRUE
            }
        }

        "additionalIdentifiers" to array {
            title = "Additional Identifiers"

            "id" to string {
                title = "Additional identifier ID"
                destination = "[buyer][additionalIdentifiers][n][id]"
                required = TRUE
            }

            "scheme" to string {
                title = "Additional identifier scheme"
                destination = "[buyer][additionalIdentifiers][n][scheme]"
                required = TRUE
            }

            "legalName" to string {
                title = "Additional identifier legal name"
                destination = "[buyer][additionalIdentifiers][n][legalName]"
                required = TRUE
            }

            "uri" to string {
                title = "Additional identifier URI"
                destination = "[buyer][additionalIdentifiers][n][uri]"
                required = TRUE
            }
        }

        "details" to obj {
            title = "Details"

            "typeOfBuyer" to string {
                title = "Type of buyer"
                source = StringEnumSourceDefinition(
                    enum = StringEnumElementsDefinition(
                        EiBuyer.Details.TypeOfBuyer.BODY_PUBLIC,
                        EiBuyer.Details.TypeOfBuyer.EU_INSTITUTION,
                        EiBuyer.Details.TypeOfBuyer.MINISTRY,
                        EiBuyer.Details.TypeOfBuyer.NATIONAL_AGENCY,
                        EiBuyer.Details.TypeOfBuyer.REGIONAL_AGENCY,
                        EiBuyer.Details.TypeOfBuyer.REGIONAL_AUTHORITY
                    )
                )
                destination = "[buyer][details][typeOfBuyer]"
                required = TRUE
            }

            "mainGeneralActivity" to string {
                title = "Main general activity"
                source = StringEnumSourceDefinition(
                    enum = StringEnumElementsDefinition(
                        EiBuyer.Details.MainGeneralActivity.DEFENCE,
                        EiBuyer.Details.MainGeneralActivity.ECONOMIC_AND_FINANCIAL_AFFAIRS,
                        EiBuyer.Details.MainGeneralActivity.EDUCATION,
                        EiBuyer.Details.MainGeneralActivity.ENVIRONMENT,
                        EiBuyer.Details.MainGeneralActivity.GENERAL_PUBLIC_SERVICES,
                        EiBuyer.Details.MainGeneralActivity.HEALTH,
                        EiBuyer.Details.MainGeneralActivity.HOUSING_AND_COMMUNITY_AMENITIES,
                        EiBuyer.Details.MainGeneralActivity.PUBLIC_ORDER_AND_SAFETY,
                        EiBuyer.Details.MainGeneralActivity.RECREATION_CULTURE_AND_RELIGION,
                        EiBuyer.Details.MainGeneralActivity.SOCIAL_PROTECTION
                    )
                )
                destination = "[buyer][details][mainGeneralActivity]"
                required = TRUE
            }

            "mainSectoralActivity" to string {
                title = "Main sectoral activity"
                source = StringEnumSourceDefinition(
                    enum = StringEnumElementsDefinition(
                        EiBuyer.Details.MainSectoralActivity.AIRPORT_RELATED_ACTIVITIES,
                        EiBuyer.Details.MainSectoralActivity.ELECTRICITY,
                        EiBuyer.Details.MainSectoralActivity.EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL,
                        EiBuyer.Details.MainSectoralActivity.EXPLORATION_EXTRACTION_GAS_OIL,
                        EiBuyer.Details.MainSectoralActivity.PORT_RELATED_ACTIVITIES,
                        EiBuyer.Details.MainSectoralActivity.POSTAL_SERVICES,
                        EiBuyer.Details.MainSectoralActivity.PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT,
                        EiBuyer.Details.MainSectoralActivity.RAILWAY_SERVICES,
                        EiBuyer.Details.MainSectoralActivity.URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES,
                        EiBuyer.Details.MainSectoralActivity.WATER
                    )
                )
                destination = "[buyer][details][mainSectoralActivity]"
                required = TRUE
            }

            "isACentralPurchasingBody" to boolean {
                title = "Is a central purchasing body"
                source = manualBoolean {
                    value = FALSE
                    readOnly = TRUE
                }
                destination = "[buyer][details][isACentralPurchasingBody]"
            }

            "scale" to string {
                title = "Scale"
                source = StringEnumSourceDefinition(
                    enum = StringEnumElementsDefinition(
                        EiBuyer.Details.Scale.MICRO,
                        EiBuyer.Details.Scale.SME,
                        EiBuyer.Details.Scale.LARGE
                    )
                )
                destination = "[buyer][details][scale]"
                required = TRUE
            }
        }

        "buyerProfile" to string {
            title = "Buyer Profile"
            destination = "[buyer][buyerProfile]"
        }

        "contactPoint" to obj {
            title = "Contact point"

            "person" to obj {
                title = "Contact person"

                "name" to string {
                    title = "Name"
                    destination = "[buyer][contactPoint][name]"
                    required = TRUE
                }

                "url" to string {
                    title = "URL"
                    destination = "[buyer][contactPoint][url]"
                    required = TRUE
                }
            }

            "contacts" to obj {
                title = "Contact point"

                "telephone" to string {
                    title = "Telephone"
                    destination = "[buyer][contactPoint][telephone]"
                    required = TRUE
                }

                "email" to string {
                    title = "E-mail"
                    destination = "[buyer][contactPoint][email]"
                    required = TRUE
                }

                "faxNumber" to string {
                    title = "Fax number"
                    destination = "[buyer][contactPoint][faxNumber]"
                }
            }
        }
    }
}.build()