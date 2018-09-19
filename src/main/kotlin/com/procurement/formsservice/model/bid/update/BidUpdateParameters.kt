package com.procurement.formsservice.model.bid.update

import com.procurement.formsservice.domain.OCID
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class BidUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val OCID = binder<OCID>(name = "ocid")
        val LOT_ID = binder<String>("lot-id")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val ocid: OCID = queryParameters.bind(OCID)
    val lotid: String = queryParameters.bind(LOT_ID)
}
