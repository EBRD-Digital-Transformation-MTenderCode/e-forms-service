package com.procurement.formsservice.model.cn.create

import com.procurement.formsservice.domain.CPID
import com.procurement.formsservice.domain.StringToCPIDTransformer
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.domain.query.v4.transformer
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

enum class CnProcuringEntity {
    BUYER, THIRDPARTY;
}

enum class CnResponsibleContactPerson {
    BUYER, THIRDPARTY;
}

enum class CnPmd {
    OT, TEST_OT, SV, TEST_SV, MV, TEST_MV
}

class CnCreateParameters(queryParameters: QueryParameters) {
    companion object {
        val CPID = binder(name = "ocid", transformer = StringToCPIDTransformer)

        private val allowableProcuringEntity = enumValues<CnProcuringEntity>().toSet()
        val PROCURING_ENTITY = binder(
            name = "procuringEntity",
            transformer = transformer(enumValues<CnProcuringEntity>()),
            validator = { allowableProcuringEntity.contains(it) }
        )

        private val allowableResponsibleContactPerson = enumValues<CnResponsibleContactPerson>().toSet()
        val RESPONSIBLE_CONTRACT_PERSON = binder(
            name = "responsibleContactPerson",
            transformer = transformer(enumValues<CnResponsibleContactPerson>()),
            validator = { allowableResponsibleContactPerson.contains(it) }
        )

        private val allowablePmd = enumValues<CnPmd>().toSet()
        val PMD = binder(
            name = "pmd",
            transformer = transformer(enumValues<CnPmd>()),
            validator = { allowablePmd.contains(it) }
        )
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val cpid: CPID = queryParameters.bind(CPID)
    val procuringEntity: CnProcuringEntity = queryParameters.bind(PROCURING_ENTITY)
    val responsibleContactPerson: CnResponsibleContactPerson = queryParameters.bind(RESPONSIBLE_CONTRACT_PERSON)
    val pmd: CnPmd = queryParameters.bind(PMD)
}
