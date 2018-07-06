package com.procurement.formsservice.json

import com.procurement.formsservice.client.execute
import com.procurement.formsservice.definition.parameter.StringParameterValueDefinition
import com.procurement.formsservice.json.data.mdm.MdmCPV
import com.procurement.formsservice.json.data.mdm.MdmCPVS
import com.procurement.formsservice.json.data.mdm.MdmCountries
import com.procurement.formsservice.json.data.mdm.MdmCurrency
import com.procurement.formsservice.json.data.mdm.MdmLanguage
import com.procurement.formsservice.json.data.mdm.MdmRegions
import com.procurement.formsservice.json.data.mdm.MdmRegistrationScheme
import com.procurement.formsservice.json.data.mdm.MdmUnitClasses
import kotlinx.coroutines.experimental.reactive.awaitFirst
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

interface MDMRepository {
    suspend fun cpv(lang: StringParameterValueDefinition): MdmCPV

    suspend fun cpvs(lang: StringParameterValueDefinition): MdmCPVS

    suspend fun currency(lang: StringParameterValueDefinition,
                         country: StringParameterValueDefinition): MdmCurrency

    suspend fun schemeRegistration(lang: StringParameterValueDefinition,
                                   country: StringParameterValueDefinition): MdmRegistrationScheme

    suspend fun countries(lang: StringParameterValueDefinition): MdmCountries

    suspend fun regions(lang: StringParameterValueDefinition,
                        country: StringParameterValueDefinition): MdmRegions

    suspend fun language(): MdmLanguage

    suspend fun unitClasses(lang: StringParameterValueDefinition): MdmUnitClasses
}

class MDMRepositoryImpl(private val webClientBuilder: WebClient.Builder) : MDMRepository {
    override suspend fun cpv(lang: StringParameterValueDefinition): MdmCPV {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("cpv")
            .queryParam("lang", lang.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun cpvs(lang: StringParameterValueDefinition): MdmCPVS {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("cpvs")
            .queryParam("lang", lang.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun currency(lang: StringParameterValueDefinition,
                                  country: StringParameterValueDefinition): MdmCurrency {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("currency")
            .queryParam("lang", lang.value)
            .queryParam("country", country.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun schemeRegistration(lang: StringParameterValueDefinition,
                                            country: StringParameterValueDefinition): MdmRegistrationScheme {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("registration-scheme")
            .queryParam("lang", lang.value)
            .queryParam("country", country.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun countries(lang: StringParameterValueDefinition): MdmCountries {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("country")
            .queryParam("lang", lang.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun regions(lang: StringParameterValueDefinition,
                                 country: StringParameterValueDefinition): MdmRegions {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("region")
            .queryParam("lang", lang.value)
            .queryParam("country", country.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun language(): MdmLanguage {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("language")
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun unitClasses(lang: StringParameterValueDefinition): MdmUnitClasses {
        val uri = UriComponentsBuilder.fromHttpUrl("http://E-MDM")
            .pathSegment("unit-class")
            .queryParam("lang", lang.value)
            .toUriString()

        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }
}
