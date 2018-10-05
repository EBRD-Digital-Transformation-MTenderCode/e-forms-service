package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.ei.create.EiCreateParameters
import com.procurement.formsservice.model.ei.update.EiUpdateParameters
import com.procurement.formsservice.service.ei.EiCreateService
import com.procurement.formsservice.service.ei.EiUpdateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class EiController(private val eiCreateService: EiCreateService,
                   private val eiUpdateService: EiUpdateService) {

    @GetMapping("/ei", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: HttpServletRequest): String =
        eiCreateService.create(
            queryParameters = EiCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )

    @GetMapping("/update-ei", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: HttpServletRequest): String =
        eiUpdateService.update(
            queryParameters = EiUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )
}
