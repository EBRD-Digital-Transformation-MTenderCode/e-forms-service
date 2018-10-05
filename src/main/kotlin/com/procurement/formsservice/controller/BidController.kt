package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.bid.create.BidCreateParameters
import com.procurement.formsservice.model.bid.update.BidUpdateParameters
import com.procurement.formsservice.service.bid.BidCreateService
import com.procurement.formsservice.service.bid.BidUpdateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class BidController(private val bidCreateService: BidCreateService,
                    private val bidUpdateService: BidUpdateService) {
    @GetMapping("/bid", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: HttpServletRequest): String =
        bidCreateService.create(
            queryParameters = BidCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )

    @GetMapping("/update-bid", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: HttpServletRequest): String =
        bidUpdateService.update(
            queryParameters = BidUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.parameterMap)
            )
        )
}
