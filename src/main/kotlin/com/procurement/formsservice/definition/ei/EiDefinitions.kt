package com.procurement.formsservice.definition.ei

import com.procurement.formsservice.definition.parameter.StringParameterNameDefinition
import com.procurement.formsservice.json.data.enumerator.StringEnumElementDefinition

object EiNamesParameters {
    val IDENTIFIER_SCHEMA =
        StringParameterNameDefinition("identifierSchema")
}

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
            val BODY_PUBLIC = StringEnumElementDefinition("BODY_PUBLIC", "Body governed by public law")
            val EU_INSTITUTION = StringEnumElementDefinition(
                "EU_INSTITUTION",
                "European institution/agency or international organisation"
            )
            val MINISTRY = StringEnumElementDefinition(
                "MINISTRY",
                "Ministry or any other national or federal authority, including their regional or local subdivisions"
            )
            val NATIONAL_AGENCY = StringEnumElementDefinition("NATIONAL_AGENCY", "National or federal agency/office")
            val REGIONAL_AGENCY = StringEnumElementDefinition("REGIONAL_AGENCY", "Regional or local agency/office")
            val REGIONAL_AUTHORITY = StringEnumElementDefinition("REGIONAL_AUTHORITY", "Regional or local authority")
        }

        object MainGeneralActivity {
            val DEFENCE = StringEnumElementDefinition("DEFENCE", "Defence")
            val ECONOMIC_AND_FINANCIAL_AFFAIRS =
                StringEnumElementDefinition("ECONOMIC_AND_FINANCIAL_AFFAIRS", "Economic and financial affairs")
            val EDUCATION = StringEnumElementDefinition("EDUCATION", "Education")
            val ENVIRONMENT = StringEnumElementDefinition("ENVIRONMENT", "Environment")
            val GENERAL_PUBLIC_SERVICES =
                StringEnumElementDefinition("GENERAL_PUBLIC_SERVICES", "General public services")
            val HEALTH = StringEnumElementDefinition("HEALTH", "Health")
            val HOUSING_AND_COMMUNITY_AMENITIES =
                StringEnumElementDefinition("HOUSING_AND_COMMUNITY_AMENITIES", "Housing and community amenities")
            val PUBLIC_ORDER_AND_SAFETY =
                StringEnumElementDefinition("PUBLIC_ORDER_AND_SAFETY", "Public order and safety")
            val RECREATION_CULTURE_AND_RELIGION =
                StringEnumElementDefinition("RECREATION_CULTURE_AND_RELIGION", "Recreation, culture and religion")
            val SOCIAL_PROTECTION = StringEnumElementDefinition("SOCIAL_PROTECTION", "Social protection")
        }

        object MainSectoralActivity {
            val AIRPORT_RELATED_ACTIVITIES =
                StringEnumElementDefinition("AIRPORT_RELATED_ACTIVITIES", "Airports related activities")
            val ELECTRICITY = StringEnumElementDefinition("ELECTRICITY", "Electricity")
            val EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL =
                StringEnumElementDefinition(
                    "EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL",
                    "Exploration extraction coal and other solid fuel"
                )
            val EXPLORATION_EXTRACTION_GAS_OIL =
                StringEnumElementDefinition("EXPLORATION_EXTRACTION_GAS_OIL", "Exploration extraction gas and oil")
            val PORT_RELATED_ACTIVITIES =
                StringEnumElementDefinition("PORT_RELATED_ACTIVITIES", "Port related activities")
            val POSTAL_SERVICES = StringEnumElementDefinition("POSTAL_SERVICES", "Postal services")
            val PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT =
                StringEnumElementDefinition(
                    "PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT",
                    "Production transport distribution gas and heat"
                )
            val RAILWAY_SERVICES = StringEnumElementDefinition("RAILWAY_SERVICES", "Railway services")
            val URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES =
                StringEnumElementDefinition(
                    "URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES",
                    "Urban railway, tramway, trolleybus, bus services"
                )
            val WATER = StringEnumElementDefinition("WATER", "Water")
        }

        object Scale {
            val MICRO = StringEnumElementDefinition("micro", "Micro Enterprise")
            val SME = StringEnumElementDefinition("sme", "Small or Medium Enterprise")
            val LARGE = StringEnumElementDefinition("large", "Large Enterprise")
        }
    }
}
