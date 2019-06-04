package com.procurement.formsservice.model.award.create

class AwardCreateContext (
    val parameters: Parameters,
    val uris: Uris,
    val amount: Amount?
) {
    data class Parameters(
        val ocid: String, // ocds-t1s2t3-MD-1532010121824-EV-1532010122650
        val lotId: String // 1a8e2f80-8b5f-11e8-a48b-9f2980d5bbdd
    )

    data class Uris(
        val country: String, // /country?lang=langFromRequest
        val region: String, // /region?lang=langFromRequest&country=$country$
        val locality: String, // /locality?lang=langFromRequest&country=$country$&region=$region$
        val registrationScheme: String, // /registration-scheme?lang=langFromRequest&country=$country$
        val currency: String // /currency?lang=langFromRequest&country=(ocds-t1s2t3->MD<-1532010121824-EV-1532010122650)
    )

    data class Amount(
        val currency: String, // for current lotId CN.tender.lots[id].value.currency
        val maxAmount: Double // for current lotId CN.tender.lots[id].value.amount
    )
}