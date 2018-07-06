package com.procurement.formsservice.definition.fs

import com.procurement.formsservice.definition.parameter.BooleanParameterNameDefinition
import com.procurement.formsservice.definition.parameter.BooleanParameterValueDefinition
import com.procurement.formsservice.definition.parameter.StringParameterNameDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.json.data.enumerator.BooleanEnumElementDefinition

object FsNamesParameters {
    val EI_OCID = StringParameterNameDefinition("ei-ocid")
    val FUNDER = StringParameterNameDefinition("funder")
    val PAYER = StringParameterNameDefinition("payer")
    val IS_EU_FUNDED = BooleanParameterNameDefinition("isEuropeanUnionFunded")
}

object FsFunderValues {
    val BUYER = StringParameterValueDefinition("buyer")
    val STATE = StringParameterValueDefinition("state")
    val DONOR = StringParameterValueDefinition("donor")
}

object FsPayerValues {
    val BUYER = StringParameterValueDefinition("buyer")
    val THIRD_PARTY = StringParameterValueDefinition("thirdParty")
    val FOUNDER = StringParameterValueDefinition("funder")
}

object FsIsEuropeanUnionFundedValues {
    val TRUE = BooleanParameterValueDefinition(true)
    val FALSE = BooleanParameterValueDefinition(false)
}

object FsBudget {
    object BudgetProject {
        object EUfunded {
            object IsEUfunded {
                val YES = BooleanEnumElementDefinition(
                    true,
                    "Yes, it is",
                    "This budget is not related to a project and/or programme financed by EU funds"
                )
                val NO = BooleanEnumElementDefinition(
                    false,
                    "No, it doesn't",
                    "this budget is related to a project and/or programme financed by EU funds"
                )
            }
        }
    }
}
