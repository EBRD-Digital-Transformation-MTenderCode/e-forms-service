package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.pn.create.PnCreateParameters
import com.procurement.formsservice.model.pn.update.PnUpdateParameters
import com.procurement.formsservice.service.pn.PnCreateService
import com.procurement.formsservice.service.pn.PnUpdateService
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class PnController(private val pnCreateService: PnCreateService,
                   private val pnUpdateService: PnUpdateService) {
    @GetMapping("/pn", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: ServerHttpRequest): Mono<String> =
        pnCreateService.create(
            queryParameters = PnCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams)
            )
        )

    @GetMapping("/update-pn", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: ServerHttpRequest): Mono<String> =
        pnUpdateService.update(
            queryParameters = PnUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams)
            )
        )
}
