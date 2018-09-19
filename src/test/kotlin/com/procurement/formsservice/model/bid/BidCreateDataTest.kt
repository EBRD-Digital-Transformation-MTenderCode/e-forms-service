package com.procurement.formsservice.model.bid

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.bid.create.BidCreateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class BidCreateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_CREATE_BID.json")
        val obj = jsonJacksonMapper.toObject<BidCreateData>(json)
        assertNotNull(obj)
    }
}
