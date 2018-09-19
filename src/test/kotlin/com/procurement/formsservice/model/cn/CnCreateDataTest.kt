package com.procurement.formsservice.model.cn

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.cn.create.CnCreateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CnCreateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_CREATE_CN.json")
        val obj = jsonJacksonMapper.toObject<CnCreateData>(json)
        assertNotNull(obj)
    }
}
