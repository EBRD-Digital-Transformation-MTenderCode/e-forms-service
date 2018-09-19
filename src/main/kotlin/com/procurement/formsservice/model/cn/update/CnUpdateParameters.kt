package com.procurement.formsservice.model.cn.update

import com.procurement.formsservice.domain.StringToOCIDTransformer
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class CnUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val OCID = binder(name = "ocid", transformer = StringToOCIDTransformer)
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val ocid = queryParameters.bind(OCID)
}