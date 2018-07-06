package com.procurement.formsservice.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.formsservice.client.execute
import com.procurement.formsservice.json.exception.DataParseException
import com.procurement.formsservice.json.exception.ParsePayloadException
import com.procurement.formsservice.json.path.PublicData
import com.procurement.formsservice.json.path.PublicDataImpl
import kotlinx.coroutines.experimental.reactive.awaitFirst
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

interface PublicPointService {
    suspend fun getDataEI(ocid: String): PublicData
}

@Service
class PublicPointServiceImpl(private val webClientBuilder: WebClient.Builder, private val objectMapper: ObjectMapper) :
    PublicPointService {
    override suspend fun getDataEI(ocid: String): PublicData {
        val uri = genBudgetsUri(ocid)
        return webClientBuilder.execute<String>(uri) { it.awaitFirst() }
            .toJsonNode()
            .getRelease()
    }

    private fun genBudgetsUri(ocid: String) = UriComponentsBuilder.fromHttpUrl("http://PUBLIC-POINT")
        .pathSegment("budgets")
        .pathSegment(ocid)
        .pathSegment(ocid)
        .toUriString()

    private fun String.toJsonNode(): JsonNode = try {
        objectMapper.readTree(this)
    } catch (exception: Exception) {
        throw ParsePayloadException("An error occurred while parsing the payload from public-point.", exception)
    }

    private fun JsonNode.getRelease(): PublicData {
        val releases = this["releases"] ?: throw DataParseException("Attribute 'releases' not found.")
        if (!releases.isArray)
            throw DataParseException("Attribute 'releases' is not an array. Current type: '${releases.nodeType}'")
        if (releases.size() != 1)
            throw DataParseException("The 'releases' attribute must have only one element. Current size: '${releases.size()}'")
        return PublicDataImpl(releases[0])
    }
}
