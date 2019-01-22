package com.procurement.formsservice.model.ac.create

class AwardContractCreateContext(
    val parameters: Parameters,
    val contracts: List<Contract>

) {
    data class Parameters(
        val ocid: String // ocds-t1s2t3-MD-1532010121824-EV-1532010122650
    )

    data class Contract(
        val id: String //EV.contracts[statusDetails == "contractProject"].id
    )
}