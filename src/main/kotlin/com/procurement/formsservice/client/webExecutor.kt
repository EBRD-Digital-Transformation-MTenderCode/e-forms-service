package com.procurement.formsservice.client

import com.procurement.formsservice.exception.client.RemoteServiceException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

inline fun <reified T> WebClient.Builder.execute(uri: String, transformer: (Mono<T>) -> T): T {
    try {
        val response: Mono<T> = this.build()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<T>() {})
        return transformer(response)
    } catch (ex: WebClientResponseException) {
        throw RemoteServiceException(
            code = ex.statusCode,
            payload = ex.responseBodyAsString,
            message = "Error of remote service by uri: '$uri'.",
            exception = ex
        )
    }
}
