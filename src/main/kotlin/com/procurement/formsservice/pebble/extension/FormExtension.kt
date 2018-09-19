package com.procurement.formsservice.pebble.extension

import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Function
import com.procurement.formsservice.pebble.function.FindSubString

object FormExtension : AbstractExtension() {
    override fun getFunctions(): Map<String, Function> = mutableMapOf<String, Function>().apply {
        this["findSubString"] = FindSubString
    }
}
