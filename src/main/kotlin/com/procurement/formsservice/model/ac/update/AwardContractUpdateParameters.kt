package com.procurement.formsservice.model.ac.update

import com.procurement.formsservice.domain.OCID
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class AwardContractUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val OCID = binder<OCID>(name = "ocid")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val ocid: OCID = queryParameters.bind(OCID)
}