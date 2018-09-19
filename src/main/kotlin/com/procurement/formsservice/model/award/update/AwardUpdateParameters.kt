package com.procurement.formsservice.model.award.update

import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class AwardUpdateParameters(queryParameters: QueryParameters) {
    companion object {
        val LOT_ID = binder<String>("lot-id")
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val lotId: String = queryParameters.bind(LOT_ID)
}
