package com.procurement.formsservice.model

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.fs.create.FsCreateData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FsCreateDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_CREATE_FS.json")
        val obj = jsonJacksonMapper.toObject<FsCreateData>(json)
        assertNotNull(obj)
    }
}
