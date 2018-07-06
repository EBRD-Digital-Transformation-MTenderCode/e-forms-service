package com.procurement.formsservice.json.data.mdm

import com.procurement.formsservice.json.data.enumerator.StringEnumElementDefinition
import com.procurement.formsservice.json.data.source.enums.StringEnumSourceDefinition

fun documentType(entityKind: EntityKind) = when (entityKind) {
    EntityKind.TENDER -> TenderEntityKind
    EntityKind.BID -> BidEntityKind
    EntityKind.AWARD -> AwardEntityKind
    EntityKind.CONTRACT -> ContractEntityKind
}

enum class EntityKind {
    TENDER, BID, AWARD, CONTRACT
}

object DocumentType {
    val tenderNotice = StringEnumElementDefinition("tenderNotice")
    val awardNotice = StringEnumElementDefinition("awardNotice")
    val contractNotice = StringEnumElementDefinition("contractNotice")
    val completionCertificate = StringEnumElementDefinition("completionCertificate")
    val procurementPlan = StringEnumElementDefinition("procurementPlan")
    val biddingDocuments = StringEnumElementDefinition("biddingDocuments")
    val technicalSpecifications = StringEnumElementDefinition("technicalSpecifications")
    val evaluationCriteria = StringEnumElementDefinition("evaluationCriteria")
    val evaluationReports = StringEnumElementDefinition("evaluationReports")
    val contractDraft = StringEnumElementDefinition("contractDraft")
    val contractSigned = StringEnumElementDefinition("contractSigned")
    val contractArrangements = StringEnumElementDefinition("contractArrangements")
    val contractSchedule = StringEnumElementDefinition("contractSchedule")
    val physicalProgressReport = StringEnumElementDefinition("physicalProgressReport")
    val financialProgressReport = StringEnumElementDefinition("financialProgressReport")
    val finalAudit = StringEnumElementDefinition("finalAudit")
    val hearingNotice = StringEnumElementDefinition("hearingNotice")
    val marketStudies = StringEnumElementDefinition("marketStudies")
    val eligibilityCriteria = StringEnumElementDefinition("eligibilityCriteria")
    val clarifications = StringEnumElementDefinition("clarifications")
    val shortlistedFirms = StringEnumElementDefinition("shortlistedFirms")
    val environmentalImpact = StringEnumElementDefinition("environmentalImpact")
    val assetAndLiabilityAssessment = StringEnumElementDefinition("assetAndLiabilityAssessment")
    val riskProvisions = StringEnumElementDefinition("riskProvisions")
    val winningBid = StringEnumElementDefinition("winningBid")
    val complaints = StringEnumElementDefinition("complaints")
    val contractAnnexe = StringEnumElementDefinition("contractAnnexe")
    val contractGuarantees = StringEnumElementDefinition("contractGuarantees")
    val subContract = StringEnumElementDefinition("subContract")
    val needsAssessment = StringEnumElementDefinition("needsAssessment")
    val feasibilityStudy = StringEnumElementDefinition("feasibilityStudy")
    val projectPlan = StringEnumElementDefinition("projectPlan")
    val billOfQuantity = StringEnumElementDefinition("billOfQuantity")
    val bidders = StringEnumElementDefinition("bidders")
    val conflictOfInterest = StringEnumElementDefinition("conflictOfInterest")
    val debarments = StringEnumElementDefinition("debarments")
    val illustration = StringEnumElementDefinition("illustration")
    val submissionDocuments = StringEnumElementDefinition("submissionDocuments")
    val contractSummary = StringEnumElementDefinition("contractSummary")
    val cancellationDetails = StringEnumElementDefinition("cancellationDetails")
}

val TenderEntityKind = StringEnumSourceDefinition.Builder()
    .apply {
        +DocumentType.tenderNotice
        +DocumentType.procurementPlan
        +DocumentType.biddingDocuments
        +DocumentType.technicalSpecifications
        +DocumentType.evaluationCriteria
        +DocumentType.contractDraft
        +DocumentType.hearingNotice
        +DocumentType.marketStudies
        +DocumentType.eligibilityCriteria
        +DocumentType.clarifications
        +DocumentType.environmentalImpact
        +DocumentType.assetAndLiabilityAssessment
        +DocumentType.riskProvisions
        +DocumentType.complaints
        +DocumentType.needsAssessment
        +DocumentType.feasibilityStudy
        +DocumentType.projectPlan
        +DocumentType.billOfQuantity
        +DocumentType.conflictOfInterest
        +DocumentType.illustration
        +DocumentType.cancellationDetails
    }

val BidEntityKind = StringEnumSourceDefinition.Builder()
    .apply {
        +DocumentType.illustration
        +DocumentType.submissionDocuments
    }

val AwardEntityKind = StringEnumSourceDefinition.Builder()
    .apply {
        +DocumentType.awardNotice
        +DocumentType.evaluationReports
        +DocumentType.shortlistedFirms
        +DocumentType.winningBid
        +DocumentType.complaints
        +DocumentType.bidders
        +DocumentType.conflictOfInterest
        +DocumentType.cancellationDetails
    }

val ContractEntityKind = StringEnumSourceDefinition.Builder()
    .apply {
        +DocumentType.contractNotice
        +DocumentType.completionCertificate
        +DocumentType.contractDraft
        +DocumentType.contractSigned
        +DocumentType.contractArrangements
        +DocumentType.contractSchedule
        +DocumentType.physicalProgressReport
        +DocumentType.financialProgressReport
        +DocumentType.finalAudit
        +DocumentType.environmentalImpact
        +DocumentType.complaints
        +DocumentType.contractAnnexe
        +DocumentType.contractGuarantees
        +DocumentType.subContract
        +DocumentType.conflictOfInterest
        +DocumentType.debarments
        +DocumentType.illustration
        +DocumentType.contractSummary
        +DocumentType.cancellationDetails
    }
