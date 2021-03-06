package com.procurement.formsservice.model.ei.update

import com.procurement.formsservice.domain.CPID
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class EiUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val CPID = binder<CPID>(name = "ocid")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val cpid: CPID = queryParameters.bind(binder = CPID)
}