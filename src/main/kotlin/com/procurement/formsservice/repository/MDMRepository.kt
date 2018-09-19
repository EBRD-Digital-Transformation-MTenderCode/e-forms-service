package com.procurement.formsservice.repository

import com.procurement.formsservice.client.execute
import com.procurement.formsservice.domain.mdm.CountryCode
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.domain.mdm.PmdCode
import com.procurement.formsservice.domain.mdm.RegistrationSchemeCode
import kotlinx.coroutines.experimental.reactive.awaitFirst
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

interface MDMRepository {
    suspend fun schemeRegistration(lang: String, country: String): Set<String>
    suspend fun countries(lang: String): Set<String>
    suspend fun pmd(lang: String, country: String): Set<String>
}

@Repository
class MDMRepositoryImpl(private val webClientBuilder: WebClient.Builder) : MDMRepository {
    companion object {
        private const val MDM_DOMAIN = "http://E-MDM"
    }

    @Cacheable("MDM")
    override suspend  fun schemeRegistration(lang: String, country: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.REGISTRATION_SCHEME.segment)
            .queryParam("lang", lang)
            .queryParam("country", country)
            .toUriString()

        return webClientBuilder.execute<RegistrationSchemeCode>(uri) {
            it.awaitFirst()
        }.data.items.asSequence().map { it.code }.toSet()
    }

    @Cacheable("MDM")
    override suspend  fun countries(lang: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.COUNTRY.segment)
            .queryParam("lang", lang)
            .toUriString()

        return webClientBuilder.execute<CountryCode>(uri) {
            it.awaitFirst()
        }.data.items.asSequence().map { it.code }.toSet()
    }

    @Cacheable("MDM")
    override suspend  fun pmd(lang: String, country: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.PMD.segment)
            .queryParam("lang", lang)
            .queryParam("country", country)
            .toUriString()

        return webClientBuilder.execute<PmdCode>(uri) {
            it.awaitFirst()
        }.data.items.asSequence().map { it.code }.toSet()
    }
}
