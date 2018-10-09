package com.procurement.formsservice.repository

import com.procurement.formsservice.domain.mdm.CountryCode
import com.procurement.formsservice.domain.mdm.MDMKind
import com.procurement.formsservice.domain.mdm.PmdCode
import com.procurement.formsservice.domain.mdm.RegistrationSchemeCode
import com.procurement.formsservice.service.RemoteService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.web.util.UriComponentsBuilder

interface MDMRepository {
    fun schemeRegistration(lang: String, country: String): Set<String>
    fun countries(lang: String): Set<String>
    fun pmd(lang: String, country: String): Set<String>
}

@Repository
class MDMRepositoryImpl(private val remoteService: RemoteService) : MDMRepository {
    companion object {
        private const val MDM_DOMAIN = "http://mdm:8080"
        private val log: Logger = LoggerFactory.getLogger(MDMRepositoryImpl::class.java)
    }

    //    @Cacheable("MDM-SchemeRegistration")
    override fun schemeRegistration(lang: String, country: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.REGISTRATION_SCHEME.segment)
            .queryParam("lang", lang)
            .queryParam("country", country)
            .build(emptyMap<String, Any>())

        log.debug("MDM-SchemeRegistration [url]: ${uri.toURL()}")
        return remoteService.execute(uri, RegistrationSchemeCode::class.java)
            .data.items.asSequence().map { it.code }.toSet()
    }

    //    @Cacheable("MDM-Countries")
    override fun countries(lang: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.COUNTRY.segment)
            .queryParam("lang", lang)
            .build(emptyMap<String, Any>())

        log.debug("MDM-Countries [url]: ${uri.toURL()}")
        return remoteService.execute(uri, CountryCode::class.java)
            .data.items.asSequence().map { it.code }.toSet()
    }

    //    @Cacheable(cacheNames = ["MDM-PMDs"])
    override fun pmd(lang: String, country: String): Set<String> {
        val uri = UriComponentsBuilder.fromHttpUrl(MDM_DOMAIN)
            .pathSegment(MDMKind.PMD.segment)
            .queryParam("lang", lang)
            .queryParam("country", country)
            .build(emptyMap<String, Any>())

        log.debug("MDM-PMD [url]: ${uri.toURL()}")
        return remoteService.execute(uri, PmdCode::class.java)
            .data.items.asSequence().map { it.code }.toSet()
    }
}
