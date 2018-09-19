package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.fs.create.FsCreateParameters
import com.procurement.formsservice.model.fs.update.FsUpdateParameters
import com.procurement.formsservice.service.fs.FsCreateService
import com.procurement.formsservice.service.fs.FsUpdateService
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class FsController(private val fsCreateService: FsCreateService,
                   private val fsUpdateService: FsUpdateService) {

    @GetMapping("/fs", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: ServerHttpRequest): Mono<String> =
        fsCreateService.create(
            queryParameters = FsCreateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams))
        )

    @GetMapping("/update-fs", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: ServerHttpRequest): Mono<String> =
        fsUpdateService.update(
            queryParameters = FsUpdateParameters(
                queryParameters = inSensitiveQueryParameters(request.queryParams)
            )
        )
}
