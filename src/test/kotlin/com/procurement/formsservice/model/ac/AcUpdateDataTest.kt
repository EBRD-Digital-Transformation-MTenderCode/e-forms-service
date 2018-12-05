package com.procurement.formsservice.model.ac

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.ac.update.AwardContractUpdateData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AcUpdateDataTest : AbstractBase() {

    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_AC.json")
        val obj = jsonJacksonMapper.toObject<AwardContractUpdateData>(json)
        Assertions.assertNotNull(obj)
    }
}