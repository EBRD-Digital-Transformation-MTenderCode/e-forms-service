package com.procurement.formsservice.model.pn

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.pn.create.PnCreateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class PnCreateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_CREATE_PN.json")
        val obj = jsonJacksonMapper.toObject<PnCreateData>(json)
        assertNotNull(obj)
    }
}
