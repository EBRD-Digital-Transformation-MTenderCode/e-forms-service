package com.procurement.formsservice.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import com.mitchellbosecke.pebble.template.PebbleTemplate
import com.procurement.formsservice.exception.TemplateEvaluateException
import com.procurement.formsservice.pebble.extension.FormExtension
import org.springframework.stereotype.Service
import java.io.StringWriter
import java.util.*

enum class KindEntity(val value: String) {
    ANSWER("answer"),
    AWARD("award"),
    BID("bid"),
    CN("cn"),
    EI("ei"),
    ENQUIRY("enquiry"),
    FS("fs"),
    PN("pn"),
    TENDER("tender");

    override fun toString(): String = value
}

enum class KindTemplate(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    CANCELLATION("cancellation");

    override fun toString(): String = value
}

interface FormTemplateService {
    operator fun get(kind: KindTemplate, entity: KindEntity): PebbleTemplate

    fun evaluate(template: PebbleTemplate, context: Map<String, Any>, locale: Locale): String
}

@Service
class FormTemplateServiceImpl(private val mapper: ObjectMapper) : FormTemplateService {
    private val engine: PebbleEngine

    init {
        val classpathLoader: ClasspathLoader = ClasspathLoader().apply {
            prefix = "templates"
            suffix = ".peb"
        }

        engine = PebbleEngine.Builder()
            .loader(classpathLoader)
            .strictVariables(true)
            .extension(FormExtension)
            .defaultEscapingStrategy("js")
//            .autoEscaping(false)
            .autoEscaping(true)
            .build()
    }

    override fun get(kind: KindTemplate, entity: KindEntity): PebbleTemplate =
        engine.getTemplate("$kind-$entity-template")

    override fun evaluate(template: PebbleTemplate, context: Map<String, Any>, locale: Locale): String = try {
        val writer = StringWriter()
        template.evaluate(writer, context, locale)
        val preparedJson = writer.toString()

        postProcessing(nameTemplate = template.name, json = preparedJson)
    } catch (exception: Exception) {

        throw TemplateEvaluateException(
            msg = "Error of evaluate template '${template.name}' with context '${toJson(context)}'",
            exception = exception
        )
    }

    private fun postProcessing(nameTemplate: String, json: String): String = try {
        val readTree = mapper.readTree(json)
        mapper.writeValueAsString(readTree)
    } catch (exception: Exception) {
        throw TemplateEvaluateException(
            msg = "Invalid JSON based on the template '$nameTemplate':\n$json",
            exception = exception
        )
    }

    fun toJson(obj: Map<String, Any>): String = try {
        mapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }
}
