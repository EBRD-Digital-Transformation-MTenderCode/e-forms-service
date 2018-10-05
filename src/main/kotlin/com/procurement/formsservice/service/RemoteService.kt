package com.procurement.formsservice.service

import com.procurement.formsservice.exception.client.RemoteServiceException
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

interface RemoteService {
    fun <T> execute(uri: URI, type: Class<T>): T
}

@Service
class RemoteServiceImpl(private val webClient: RestTemplate) : RemoteService {
    override fun <T> execute(uri: URI, type: Class<T>) = try {
        webClient.getForEntity(uri, type).body!!
    } catch (exception: HttpClientErrorException) {
        val code = exception.statusCode
        val payload = exception.responseBodyAsString
        val message = "Error [$code:$payload] of remote service by uri: '$uri'."

        throw RemoteServiceException(
            code = code,
            payload = payload,
            message = message,
            exception = exception
        )
    } catch (exception: Exception) {
        throw RemoteServiceException(
            message = "Error of remote service by uri: '$uri'.",
            exception = exception
        )
    }
}