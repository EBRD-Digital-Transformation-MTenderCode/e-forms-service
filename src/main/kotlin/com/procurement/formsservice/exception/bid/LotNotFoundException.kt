package com.procurement.formsservice.exception.bid

class LotNotFoundException(val ocid: String, val lotid: String) : RuntimeException("The lot by id '$lotid' not found.")
