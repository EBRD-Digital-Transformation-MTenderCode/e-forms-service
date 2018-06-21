package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.response.FormRS
import com.procurement.formsservice.service.EIService
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class EIController(private val eiService: EIService) {
    @GetMapping("/ei")
    fun formEI(request: ServerHttpRequest): Mono<FormRS> = eiService.form(request)
}