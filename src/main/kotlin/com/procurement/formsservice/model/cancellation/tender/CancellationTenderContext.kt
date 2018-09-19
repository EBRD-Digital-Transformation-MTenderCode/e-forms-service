package com.procurement.formsservice.model.cancellation.tender

class CancellationTenderContext(
    val parameters: Parameters,
    val tender: Tender
) {
    data class Parameters(
        val ocid: String // ocds-t1s2t3-MD-1532010121824
    )

    data class Tender(
        val title: String, // MS.tender.title
        val description: String // MS.tender.description
    )
}
