package com.procurement.formsservice.model.ei

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.ei.update.EiUpdateData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EiUpdateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_EI.json")
        val obj = jsonJacksonMapper.toObject<EiUpdateData>(json)
        Assertions.assertNotNull(obj)
    }
}