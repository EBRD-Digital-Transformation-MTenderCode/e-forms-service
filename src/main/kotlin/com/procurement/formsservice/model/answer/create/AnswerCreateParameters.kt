package com.procurement.formsservice.model.answer.create

import com.procurement.formsservice.domain.query.v4.QueryParameters
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.LANG
import com.procurement.formsservice.model.parameters.CommonQueryParametersBinder.defaultLang

class AnswerCreateParameters(queryParameters: QueryParameters) {
    val lang: String = queryParameters.bind(binder = LANG, default = { defaultLang })

//    companion object {
//        fun create(queryParameters: QueryParameters): AnswerCreateParameters =
//            queryParameters.let {
//                val lang: String = it.bind(binder = LANG, default = { defaultLang })
//                AnswerCreateParameters(lang)
//            }
//        fun create(queryParameters: Map<String, List<String>>): AnswerCreateParameters =
//            isSensitiveQueryParameters(queryParameters).let {
//                val lang: String = it.bind(binder = LANG, default = { defaultLang })
//                AnswerCreateParameters(lang)
//            }
//    }
}

//class AnswerCreateParameters(queryParameters: Map<String, List<String>>) : QueryParameters(queryParameters) {
//    companion object {
//        fun create(queryParameters: Map<String, List<String>>):AnswerCreateParameters {
//
//        }
//    }
//
//    val lang: String = bind(binder = LANG, default = { defaultLang })
//}
