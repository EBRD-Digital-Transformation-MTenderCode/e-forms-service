package com.procurement.formsservice.json.path

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.STRING
import com.procurement.formsservice.json.exception.DataParseException

sealed class PublicData {
    abstract operator fun get(path: StringPathSource): STRING?
    abstract operator fun get(path: BooleanPathSource): BOOLEAN?
    abstract operator fun get(path: IntegerPathSource): INTEGER?
    abstract operator fun get(path: NumberPathSource): NUMBER?
    abstract fun getNode(): JsonNode

    abstract fun pushRelativePath(path: PathSource)
    abstract fun popRelativePath(path: PathSource)
}

object EmptyPublicData : PublicData() {
    override fun get(path: StringPathSource): STRING? =
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")

    override fun get(path: BooleanPathSource): BOOLEAN? =
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")

    override fun get(path: IntegerPathSource): INTEGER? =
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")

    override fun get(path: NumberPathSource): NUMBER? =
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")

    override fun getNode(): JsonNode {
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")
    }

    override fun pushRelativePath(path: PathSource) {
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")
    }

    override fun popRelativePath(path: PathSource) {
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")
    }
}

class PublicDataImpl(private val node: JsonNode) : PublicData() {
    private var relativeNode = node

    override operator fun get(path: StringPathSource): STRING? = getNodeByPath(path)?.asText()
    override operator fun get(path: BooleanPathSource): BOOLEAN? = getNodeByPath(path)?.asBoolean()
    override operator fun get(path: IntegerPathSource): INTEGER? = getNodeByPath(path)?.asLong()
    override operator fun get(path: NumberPathSource): NUMBER? = getNodeByPath(path)?.asDouble()

    private fun getNodeByPath(path: PathSource) = when (path.kind) {
        PathKind.ABSOLUTE -> getAbsoluteNode(parent = node, path = path)
        PathKind.RELATIVE -> getRelativeNode(parent = relativeNode, path = path)
        PathKind.EMPTY -> null
    }

    override fun getNode(): JsonNode = node

    private fun getAbsoluteNode(parent: JsonNode, path: PathSource): JsonNode? {
        var current = parent
        for (element in path) {
            val name = element.name

            current = current.get(element.name) ?: return null

            val expectedAttributeType = element.type
            val actualAttributeType = current.actualAttributeType()
            if (expectedAttributeType != actualAttributeType)
                throw DataParseException("Element '$name' in data by path '$path' is invalid type. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")

            if (expectedAttributeType == PathSourceElementType.ARRAY && element.index != -1)
                current = current[element.index]
        }
        return current
    }

    private fun getRelativeNode(parent: JsonNode, path: PathSource): JsonNode? {
        var current = parent
        for (element in path) {
            val name = element.name

            current = current.get(element.name) ?: return null

            val expectedAttributeType = element.type
            val actualAttributeType = current.actualAttributeType()
            if (expectedAttributeType != actualAttributeType)
                throw DataParseException("Element '$name' in data by path '$path' is invalid type. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
        }
        return current
    }

    private fun JsonNode.actualAttributeType(): PathSourceElementType? =
        when {
            this.isObject -> PathSourceElementType.OBJECT
            this.isArray -> PathSourceElementType.ARRAY
            this.isTextual -> PathSourceElementType.STRING
            this.isBoolean -> PathSourceElementType.BOOLEAN
            this.isNumber && (this.isLong || this.isInt) -> PathSourceElementType.INTEGER
            this.isNumber && (this.isFloat || this.isDouble) -> PathSourceElementType.NUMBER
            else -> null
        }

    override fun pushRelativePath(path: PathSource) {
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")
    }

    override fun popRelativePath(path: PathSource) {
        throw NotImplementedError("An operation is not implemented for empty 'Public Data'")
    }
}
