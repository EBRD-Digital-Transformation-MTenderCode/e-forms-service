package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.bid.create.BidCreateParameters
import com.procurement.formsservice.model.bid.update.BidUpdateParameters
import com.procurement.formsservice.service.bid.BidCreateService
import com.procurement.formsservice.service.bid.BidUpdateService
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class BidController(private val bidCreateService: BidCreateService,
                    private val bidUpdateService: BidUpdateService) {
    @GetMapping("/bid", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: ServerHttpRequest): Mono<String> =
        bidCreateService.create(
            queryParameters = BidCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams)
            )
        )

    @GetMapping("/update-bid", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: ServerHttpRequest): Mono<String> =
        bidUpdateService.update(
            queryParameters = BidUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams)
            )
        )
}
