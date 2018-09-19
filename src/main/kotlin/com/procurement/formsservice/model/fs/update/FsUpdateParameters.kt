package com.procurement.formsservice.model.fs.update

import com.procurement.formsservice.domain.StringToOCIDTransformer
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder

class FsUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val OCID = binder(name = "ocid", transformer = StringToOCIDTransformer)
    }

    val lang: String = queryParameters.bind(binder = CommonQueryParametersBinder.LANG,
                                            default = { CommonQueryParametersBinder.defaultLang })
    val ocid = queryParameters.bind(OCID)
}