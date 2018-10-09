package com.procurement.formsservice.service

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
        return remoteService.execute(uri, BidCreateData::class.java)
    }

    override fun getCnCreateData(cpid: String): CnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create CN) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, CnCreateData::class.java)
    }

    override fun getEnquiryCreateData(cpid: String, ocid: String): EnquiryCreateData {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Create Enquiry) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, EnquiryCreateData::class.java)
    }

    override fun getFsCreateData(cpid: String): FsCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create Fs) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, FsCreateData::class.java)
    }

    override fun getPnCreateData(cpid: String): PnCreateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Create Pn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, PnCreateData::class.java)
    }

    override fun getEiUpdateData(cpid: String): EiUpdateData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Update Ei) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, EiUpdateData::class.java)
    }

    override fun getFsUpdateEiData(cpid: String): FsUpdateEiData {
        val uri = genBudgetsUri(cpid, cpid)
        log.debug("Public Point (Update FS from Ei) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, FsUpdateEiData::class.java)
    }

    override fun getFsUpdateFsData(cpid: String, ocid: String): FsUpdateFsData {
        val uri = genBudgetsUri(cpid = cpid, ocid = ocid)
        log.debug("Public Point (Update Fs from Fs) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, FsUpdateFsData::class.java)
    }

    override fun getCnUpdateData(cpid: String): CnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Cn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, CnUpdateData::class.java)
    }

    override fun getPnUpdateData(cpid: String): PnUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Pn) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, PnUpdateData::class.java)
    }

    override fun getBidUpdateData(cpid: String): BidUpdateData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Update Bid) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, BidUpdateData::class.java)
    }

    override fun getCancellationTenderData(cpid: String): CancellationTenderData {
        val uri = genTendersUri(cpid = cpid)
        log.debug("Public Point (Tender Cancellation) [url]: ${uri.toURL()}")
        return remoteService.execute(uri, CancellationTenderData::class.java)
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
