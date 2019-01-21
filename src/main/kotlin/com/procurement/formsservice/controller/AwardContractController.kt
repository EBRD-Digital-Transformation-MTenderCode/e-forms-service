package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.ac.create.AwardContractCreateParameters
import com.procurement.formsservice.model.ac.update.AwardContractUpdateParameters
import com.procurement.formsservice.service.ac.AcCreateService
import com.procurement.formsservice.service.ac.AcUpdateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class AwardContractController(
    private val acCreateService: AcCreateService,
    private val acUpdateService: AcUpdateService
) {
    @GetMapping("/ac", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: HttpServletRequest): String =
        acCreateService.create(
            queryParameters = AwardContractCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )

    @GetMapping("/update-ac", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: HttpServletRequest): String =
        acUpdateService.update(
            queryParameters = AwardContractUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )
}
