package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.cn.create.CnCreateParameters
import com.procurement.formsservice.model.cn.update.CnUpdateParameters
import com.procurement.formsservice.service.cn.CnCreateService
import com.procurement.formsservice.service.cn.CnUpdateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class CnController(private val cnCreateService: CnCreateService,
                   private val cnUpdateService: CnUpdateService) {

    @GetMapping("/cn", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: HttpServletRequest): String =
        cnCreateService.create(
            queryParameters = CnCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap))
        )

    @GetMapping("/update-cn", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: HttpServletRequest): String =
        cnUpdateService.update(
            queryParameters = CnUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap))
        )
}
