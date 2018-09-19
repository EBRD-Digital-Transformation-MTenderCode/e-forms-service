package com.procurement.formsservice.model.fs.create

import com.procurement.formsservice.domain.CPID
import com.procurement.formsservice.domain.StringToCPIDTransformer
import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.domain.query.v4.StringToBooleanTransformer
import com.procurement.formsservice.domain.query.v4.binder
import com.procurement.formsservice.domain.query.v4.transformer
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

enum class FsFunder {
    BUYER, STATE, DONOR;
}

enum class FsPayer {
    BUYER, THIRDPARTY, FUNDER;
}

class FsCreateParameters(queryParameters: QueryParameters) {
    companion object {
        val CPID = binder(name = "ocid", transformer = StringToCPIDTransformer)

        private val allowableFunder = enumValues<FsFunder>().toSet()
        val FUNDER = binder(
            name = "funder",
            transformer = transformer(enumValues<FsFunder>()),
            validator = { allowableFunder.contains(it) }
        )

        private val allowablePayer = enumValues<FsPayer>().toSet()
        val PAYER = binder(
            name = "payer",
            transformer = transformer(enumValues<FsPayer>()),
            validator = { allowablePayer.contains(it) }
        )

        val IS_EU_FUNDED = binder(
            name = "isEuropeanUnionFunded",
            transformer = StringToBooleanTransformer
        )
    }

    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })
    val cpid: CPID = queryParameters.bind(CPID)
    val funder: FsFunder = queryParameters.bind(FUNDER)
    val payer: FsPayer = queryParameters.bind(PAYER)
    val isEuropeanUnionFunded: Boolean = queryParameters.bind(IS_EU_FUNDED)
}
