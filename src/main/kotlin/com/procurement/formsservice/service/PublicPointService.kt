package com.procurement.formsservice.service

import com.procurement.formsservice.client.execute
import com.procurement.formsservice.model.bid.create.BidCreateData
import com.procurement.formsservice.model.bid.update.BidUpdateData
import com.procurement.formsservice.model.cancellation.tender.CancellationTenderData
import com.procurement.formsservice.model.cn.create.CnCreateData
import com.procurement.formsservice.model.cn.update.CnUpdateData
import com.procurement.formsservice.model.ei.update.EiUpdateData
import com.procurement.formsservice.model.enquiry.create.EnquiryCreateData
import com.procurement.formsservice.model.fs.create.FsCreateData
import com.procurement.formsservice.model.fs.update.FsUpdateEiData
import com.procurement.formsservice.model.fs.update.FsUpdateFsData
import com.procurement.formsservice.model.pn.create.PnCreateData
import com.procurement.formsservice.model.pn.update.PnUpdateData
import kotlinx.coroutines.experimental.reactive.awaitFirst
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder

interface PublicPointService {
    suspend fun getBidCreateData(cpid: String, ocid: String): BidCreateData
    suspend fun getCnCreateData(cpid: String): CnCreateData
    suspend fun getEnquiryCreateData(cpid: String, ocid: String): EnquiryCreateData
    suspend fun getFsCreateData(cpid: String): FsCreateData
    suspend fun getPnCreateData(cpid: String): PnCreateData

    suspend fun getEiUpdateData(cpid: String): EiUpdateData
    suspend fun getFsUpdateEiData(cpid: String): FsUpdateEiData
    suspend fun getFsUpdateFsData(cpid: String, ocid: String): FsUpdateFsData
    suspend fun getCnUpdateData(cpid: String): CnUpdateData
    suspend fun getPnUpdateData(cpid: String): PnUpdateData
    suspend fun getBidUpdateData(cpid: String): BidUpdateData

    suspend fun getCancellationTenderData(cpid: String): CancellationTenderData
}

@Service
class PublicPointServiceImpl(private val webClientBuilder: WebClient.Builder) : PublicPointService {
    companion object {
        private const val PUBLIC_POINT_DOMAIN = "http://public-point:8080"
    }

    override suspend fun getBidCreateData(cpid: String, ocid: String): BidCreateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getCnCreateData(cpid: String): CnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getEnquiryCreateData(cpid: String, ocid: String): EnquiryCreateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getFsCreateData(cpid: String): FsCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getPnCreateData(cpid: String): PnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getEiUpdateData(cpid: String): EiUpdateData {
        val uri = genBudgetsUri(cpid, cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getFsUpdateEiData(cpid: String): FsUpdateEiData {
        val uri = genBudgetsUri(cpid, cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getFsUpdateFsData(cpid: String, ocid: String): FsUpdateFsData {
        val uri = genBudgetsUri(cpid = cpid, ocid = ocid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getCnUpdateData(cpid: String): CnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getPnUpdateData(cpid: String): PnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getBidUpdateData(cpid: String): BidUpdateData {
        val uri = genTendersUri(cpid = cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    override suspend fun getCancellationTenderData(cpid: String): CancellationTenderData {
        val uri = genTendersUri(cpid = cpid)
        return webClientBuilder.execute(uri) { it.awaitFirst() }
    }

    private fun genBudgetsUri(cpid: String, ocid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("budgets")
        .pathSegment(cpid)
        .pathSegment(ocid)
        .toUriString()

    private fun genTendersUri(cpid: String, ocid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("tenders")
        .pathSegment(cpid)
        .pathSegment(ocid)
        .toUriString()

    private fun genTendersUri(cpid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("tenders")
        .pathSegment(cpid)
        .toUriString()
}
