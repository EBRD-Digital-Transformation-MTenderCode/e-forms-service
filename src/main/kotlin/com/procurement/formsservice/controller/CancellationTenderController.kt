package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderParameters
import com.procurement.formsservice.service.cancellation.CancellationTenderService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class CancellationTenderController(private val cancellationService: CancellationTenderService) {

    @GetMapping("/cancellation-tender", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: HttpServletRequest): String =
        cancellationService.cancel(
            queryParameters = CancellationTenderParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )
}
