package com.procurement.formsservice.model.enquiry.create

import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class EnquiryCreateParameters(queryParameters: QueryParameters) {
    companion object {
        val LOT_ID = binder<String>("lot-id")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val lotid: String? = queryParameters.bind(LOT_ID, default = { "" })
        .let {
            if (it.isNotEmpty())
                it
            else
                null
        }
}
