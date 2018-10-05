package com.procurement.formsservice.controller

import com.procurement.formsservice.domain.query.v4.inSensitiveQueryParameters
import com.procurement.formsservice.model.award.update.AwardUpdateParameters
import com.procurement.formsservice.service.AwardService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/forms")
class AwardController(private val awardService: AwardService) {
    @GetMapping("/update-award", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(request: HttpServletRequest): String =
        awardService.update(
            queryParameters = AwardUpdateParameters(queryParameters = inSensitiveQueryParameters(
                request.parameterMap))
        )
}
