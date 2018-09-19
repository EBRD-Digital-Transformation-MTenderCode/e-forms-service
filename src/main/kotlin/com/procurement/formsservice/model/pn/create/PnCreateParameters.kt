package com.procurement.formsservice.model.pn.create

import com.procurement.formsservice.domain.CPID
import com.procurement.formsservice.domain.StringToCPIDTransformer
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.domain.query.v4.transformer
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

enum class PnProcuringEntity {
    BUYER, THIRDPARTY;
}

enum class PnResponsibleContactPerson {
    BUYER, THIRDPARTY;
}

enum class PnPmd {
    OT, RT, TEST_OT, TEST_RT;
}

//class PnParameters private constructor(val lang: String,
//                                       val cpid: CPID,
//                                       val procuringEntity: PnProcuringEntity,
//                                       val responsibleContactPerson: PnResponsibleContactPerson,
//                                       val pmd: PnPmd) {
//    companion object {
//        val CPID = binder(name = "ocid", transformer = StringToCPIDTransformer)
//
//        private val allowableProcuringEntity = enumValues<PnProcuringEntity>().toSet()
//        val PROCURING_ENTITY = binder(
//            name = "procuringEntity",
//            transformer = transformer(enumValues<PnProcuringEntity>()),
//            validator = { allowableProcuringEntity.contains(it) }
//        )
//
//        private val allowableResponsibleContactPerson = enumValues<PnResponsibleContactPerson>().toSet()
//        val RESPONSIBLE_CONTRACT_PERSON = binder(
//            name = "responsibleContactPerson",
//            transformer = transformer(enumValues<PnResponsibleContactPerson>()),
//            validator = { allowableResponsibleContactPerson.contains(it) }
//        )
//
//        val PMD = binder(
//            name = "pmd",
//            transformer = transformer(enumValues<PnPmd>())
//        )
//
//        fun create(queryParameters: QueryParameters,
//                   mdmRepository: MDMRepository): PnParameters = queryParameters.let {
//            val lang: String = it.bind(binder = LANG, default = { defaultLang })
//            val cpid: CPID = it.bind(CPID)
//            val procuringEntity: PnProcuringEntity = it.bind(PROCURING_ENTITY)
//            val responsibleContactPerson: PnResponsibleContactPerson = it.bind(RESPONSIBLE_CONTRACT_PERSON)
//            val pmd: PnPmd = it.bind(binder = PMD,
//                validator = { value ->
//                    runBlocking {
//                        mdmRepository.schemeRegistration(lang, country).contains(value)
//                    }
//                }
//            )
//
//            PnParameters(lang, cpid, procuringEntity, responsibleContactPerson, pmd)
//        }
//    }
//}

class PnCreateParameters(queryParameters: QueryParameters) {
    companion object {
        val CPID = binder(name = "ocid", transformer = StringToCPIDTransformer)

        private val allowableProcuringEntity = enumValues<PnProcuringEntity>().toSet()
        val PROCURING_ENTITY = binder(
            name = "procuringEntity",
            transformer = transformer(enumValues<PnProcuringEntity>()),
            validator = { allowableProcuringEntity.contains(it) }
        )

        private val allowableResponsibleContactPerson = enumValues<PnResponsibleContactPerson>().toSet()
        val RESPONSIBLE_CONTRACT_PERSON = binder(
            name = "responsibleContactPerson",
            transformer = transformer(enumValues<PnResponsibleContactPerson>()),
            validator = { allowableResponsibleContactPerson.contains(it) }
        )

        private val allowablePmd = enumValues<PnPmd>().toSet()
        val PMD = binder(
            name = "pmd",
            transformer = transformer(enumValues<PnPmd>()),
            validator = { allowablePmd.contains(it) }
        )
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val cpid: CPID = queryParameters.bind(CPID)
    val procuringEntity: PnProcuringEntity = queryParameters.bind(PROCURING_ENTITY)
    val responsibleContactPerson: PnResponsibleContactPerson = queryParameters.bind(RESPONSIBLE_CONTRACT_PERSON)
    val pmd: PnPmd = queryParameters.bind(PMD)
}
