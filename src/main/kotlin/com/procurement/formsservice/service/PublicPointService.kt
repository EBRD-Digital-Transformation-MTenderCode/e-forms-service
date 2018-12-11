package com.procurement.formsservice.service

import com.procurement.formsservice.model.ac.update.AwardContractUpdateData
import com.procurement.formsservice.model.ac.update.MSForAwardContractUpdateData
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

interface PublicPointService {
    fun getBidCreateData(cpid: String, ocid: String): BidCreateData
    fun getCnCreateData(cpid: String): CnCreateData
    fun getEnquiryCreateData(cpid: String, ocid: String): EnquiryCreateData
    fun getFsCreateData(cpid: String): FsCreateData
    fun getPnCreateData(cpid: String): PnCreateData

    fun getEiUpdateData(cpid: String): EiUpdateData
    fun getFsUpdateEiData(cpid: String): FsUpdateEiData
    fun getFsUpdateFsData(cpid: String, ocid: String): FsUpdateFsData
    fun getCnUpdateData(cpid: String): CnUpdateData
    fun getPnUpdateData(cpid: String): PnUpdateData
    fun getBidUpdateData(cpid: String): BidUpdateData

    fun getCancellationTenderData(cpid: String): CancellationTenderData

    fun getAwardContractUpdateData(cpid: String, ocid: String): AwardContractUpdateData
    fun getMSForAwardContractUpdateData(cpid: String): MSForAwardContractUpdateData
}

@Service
class PublicPointServiceImpl(private val remoteService: RemoteService) : PublicPointService {
    companion object {
        private const val PUBLIC_POINT_DOMAIN = "http://public-point:8080"
        private val log: Logger = LoggerFactory.getLogger(PublicPointServiceImpl::class.java)
    }

    override fun getBidCreateData(cpid: String, ocid: String): BidCreateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Create Bid) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getCnCreateData(cpid: String): CnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create CN) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getEnquiryCreateData(cpid: String, ocid: String): EnquiryCreateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Create Enquiry) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getFsCreateData(cpid: String): FsCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create Fs) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getPnCreateData(cpid: String): PnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create Pn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getEiUpdateData(cpid: String): EiUpdateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Update Ei) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getFsUpdateEiData(cpid: String): FsUpdateEiData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Update FS from Ei) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getFsUpdateFsData(cpid: String, ocid: String): FsUpdateFsData {
        val uri = genBudgetsUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Update Fs from Fs) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getCnUpdateData(cpid: String): CnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Cn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getPnUpdateData(cpid: String): PnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Pn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getBidUpdateData(cpid: String): BidUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Bid) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getCancellationTenderData(cpid: String): CancellationTenderData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Tender Cancellation) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getAwardContractUpdateData(cpid: String, ocid: String): AwardContractUpdateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Update Award Contract) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    override fun getMSForAwardContractUpdateData(cpid: String): MSForAwardContractUpdateData {
        val uri = genTendersUri(cpid = cpid, ocid = cpid)
        log.debug("Public Point (MS for Update Award Contract) [url]: ${uri.toURL()}")
        return remoteService.execute(uri)
    }

    private fun genBudgetsUri(cpid: String, ocid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("budgets")
        .pathSegment(cpid)
        .pathSegment(ocid)
        .build(emptyMap<String, Any>())

    private fun genTendersUri(cpid: String, ocid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("tenders")
        .pathSegment(cpid)
        .pathSegment(ocid)
        .build(emptyMap<String, Any>())

    private fun genTendersUri(cpid: String) = UriComponentsBuilder.fromHttpUrl(PUBLIC_POINT_DOMAIN)
        .pathSegment("tenders")
        .pathSegment(cpid)
        .build(emptyMap<String, Any>())
}
