package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.answer.create.AnswerCreateParameters
import com.procurement.formsservice.service.AnswerService
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forms")
class AnswerController(private val answerService: AnswerService) {
    @GetMapping("/answer", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(request: ServerHttpRequest): Mono<String> =
        answerService.create(
            queryParameters = AnswerCreateParameters(inSensitiveQueryParameters(
                request.queryParams))
        )
}
