package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.service.CNService
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class CNController(private val cnService: CNService) {
    @GetMapping("/cn")
    fun formFS(request: ServerHttpRequest): Mono<FormRS> = cnService.form(request)
}
