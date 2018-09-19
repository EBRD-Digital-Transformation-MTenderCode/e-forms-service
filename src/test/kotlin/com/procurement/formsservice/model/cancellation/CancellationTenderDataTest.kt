package com.procurement.formsservice.model.cancellation

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CancellationTenderDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_CANCELLATION_TENDER.json")
        val obj = jsonJacksonMapper.toObject<CancellationTenderData>(json)
        assertNotNull(obj)
    }
}
