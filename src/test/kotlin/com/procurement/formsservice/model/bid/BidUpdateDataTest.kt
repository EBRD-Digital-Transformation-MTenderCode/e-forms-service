package com.procurement.formsservice.model.bid

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.bid.update.BidUpdateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class BidUpdateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_BID.json")
        val obj = jsonJacksonMapper.toObject<BidUpdateData>(json)
        assertNotNull(obj)
    }
}
