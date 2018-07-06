package com.procurement.formsservice.definition.cn

import com.procurement.formsservice.definition.parameter.StringParameterNameDefinition
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.json.data.enumerator.StringEnumElementDefinition

object CnNamesParameters {
    val EI_OCID = StringParameterNameDefinition("ei-ocid")
    val PROCURING_ENTITY = StringParameterNameDefinition("procuringEntity")
    val RESPONSIBLE_CONTRACT_PERSON = StringParameterNameDefinition("responsibleContactPerson")
    val PMD = StringParameterNameDefinition("pmd")
}

object CnProcuringEntity {
    val BUYER = StringParameterValueDefinition("buyer")
    val THIRD_PARTY = StringParameterValueDefinition("thirdParty")
}

object CnResponsibleContactPerson {
    val BUYER = StringParameterValueDefinition("buyer")
    val THIRD_PARTY = StringParameterValueDefinition("thirdParty")
}

object CnPmd {
    val OT = StringParameterValueDefinition("OT")
    val RT = StringParameterValueDefinition("RT")
}

object Section4 {
    object Method {
        object LegalBasis {
            val DIRECTIVE_2014_23_EU = StringEnumElementDefinition("DIRECTIVE_2014_23_EU")
            val DIRECTIVE_2014_24_EU = StringEnumElementDefinition("DIRECTIVE_2014_24_EU")
            val DIRECTIVE_2014_25_EU = StringEnumElementDefinition("DIRECTIVE_2014_25_EU")
            val DIRECTIVE_2009_81_EC = StringEnumElementDefinition("DIRECTIVE_2009_81_EC")
            val REGULATION_966_2012 = StringEnumElementDefinition("REGULATION_966_2012")
            val NATIONAL_PROCUREMENT_LAW = StringEnumElementDefinition("NATIONAL_PROCUREMENT_LAW")
            val NULL = StringEnumElementDefinition("NULL")
        }
    }
}
