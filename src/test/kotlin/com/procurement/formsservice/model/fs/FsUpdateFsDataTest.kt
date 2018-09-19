package com.procurement.formsservice.model.fs

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.fs.update.FsUpdateFsData
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FsUpdateFsDataTest : AbstractBase() {
    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_FS.json")
        val obj = jsonJacksonMapper.toObject<FsUpdateFsData>(json)
        assertNotNull(obj)
    }
}
