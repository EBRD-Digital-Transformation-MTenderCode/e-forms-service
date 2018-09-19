package com.procurement.formsservice.model.cn

import com.procurement.formsservice.AbstractBase
import com.procurement.formsservice.model.cn.update.CnUpdateData
import com.procurement.formsservice.model.ei.update.EiUpdateData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CnUpdateDataTest : AbstractBase() {
    private val MS_CPID = "ocds-t1s2t3-MD-1534853430662"
    private val CN_OCID = "ocds-t1s2t3-MD-1534853430662-EV-1534853430760"

    @Test
    fun test() {
        val json = RESOURCES.load("json/DATA_FOR_UPDATE_CN.json")
        val obj = jsonJacksonMapper.toObject<CnUpdateData>(json)
        Assertions.assertNotNull(obj)


        var msSuccess = false
        var cnSuccess = false

        for(record in obj.records) {
            if(record.ocid == MS_CPID) {
                val ms = jsonJacksonMapper.toObject<CnUpdateData.Record.MS>(record.compiledRelease)
                Assertions.assertNotNull(ms)
                msSuccess = true
            }

            if(record.ocid == CN_OCID) {
                val ms = jsonJacksonMapper.toObject<CnUpdateData.Record.CN>(record.compiledRelease)
                Assertions.assertNotNull(ms)
                cnSuccess = true
            }
        }

        Assertions.assertTrue(msSuccess)
        Assertions.assertTrue(cnSuccess)
    }
}