package com.procurement.formsservice.model.pn

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.pn.update.PnUpdateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class PnUpdateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_PN.json")
        val obj = jsonJacksonMapper.toObject<PnUpdateData>(json)
        assertNotNull(obj)
    }
}
