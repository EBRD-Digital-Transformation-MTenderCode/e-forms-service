package com.procurement.formsservice.exception.query

class BinderQueryParameterException(val name: String,
                                    val type: Class<*>,
                                    msg: String = "Error definition of binder for parameter '$name'. A transformer for type '${type.typeName}' not found.") :
    RuntimeException(msg)
