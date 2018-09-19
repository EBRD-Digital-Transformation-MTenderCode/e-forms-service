package com.procurement.formsservice.model.award.update

class AwardUpdateContext (
    val parameters: Parameters
) {
    data class Parameters(
        val lotId: String
    )
}
