package com.procurement.formsservice.model.fs

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.fs.update.FsUpdateEiData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FsUpdateEiDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_EI.json")
        val obj = jsonJacksonMapper.toObject<FsUpdateEiData>(json)
        assertNotNull(obj)
    }
}
