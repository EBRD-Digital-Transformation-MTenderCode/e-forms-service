package com.procurement.formsservice.json.path

import com.procurement.formsservice.json.exception.PathParseException

enum class PathSourceElementType(val kind: String) {
    OBJECT("object"),
    ARRAY("array"),
    STRING("string"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    NUMBER("number");

    companion object {
        private val map = mutableMapOf<String, PathSourceElementType>()

        init {
            values().forEach { map[it.kind] = it }
        }

        fun valueOfKind(kind: String): PathSourceElementType? = map[kind]
    }
}

data class PathSourceElement(val name: String, val type: PathSourceElementType, val index: Int = -1)

enum class PathKind {
    ABSOLUTE, RELATIVE, EMPTY
}

sealed class PathSource : Iterable<PathSourceElement> {
    abstract fun isEmpty(): Boolean
    abstract fun isNotEmpty(): Boolean
    abstract val kind: PathKind
}

sealed class AbstractPathSource(private val path: String,
                                expectedAttributeType: PathSourceElementType) : PathSource(),
                                                                                Iterable<PathSourceElement> {
    private val elements: List<PathSourceElement>

    private var _kind: PathKind
    override val kind: PathKind
        get() = _kind

    init {
        if (path.isNotBlank()) {
            val (kind, elements) = getPath(path)
            _kind = kind
            this.elements = elements
            val actualAttributeType = elements[elements.size - 1].type
            if (actualAttributeType != expectedAttributeType)
                throw PathParseException("Invalid type of attribute by path '$path'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
        } else {
            this.elements = emptyList()
            _kind = PathKind.EMPTY
        }
    }

    private fun getPath(path: String): Pair<PathKind, List<PathSourceElement>> {
        val split = path.split("/")
        val pathKind = when {
            split[0] == "" -> PathKind.ABSOLUTE
            split[0] == ".." -> PathKind.RELATIVE
            else -> throw PathParseException("Invalid kind of path '$path'")
        }

        val elements = split.subList(1, split.size).map { toPathElement(path, it) }
        return Pair(pathKind, elements)
    }

    private fun toPathElement(path: String, element: String): PathSourceElement {
        val posType = element.indexOf("::")
        val nameElement = element.substring(0, posType)

        val nameType = element.substring(posType + 2)
        val startIndexOfArray = nameType.indexOf("[")

        val typeElement = getType(nameElement, nameType, startIndexOfArray)

        val indexElement = if (startIndexOfArray != -1) {
            val endIndexOfArray = nameType.indexOf("]")
            if (endIndexOfArray == -1) throw PathParseException("Invalid index of array of element '$nameElement' by path '$path'")
            nameType.substring(startIndexOfArray + 1, endIndexOfArray).toInt()
        } else
            -1

        return PathSourceElement(nameElement, typeElement, indexElement)
    }

    private fun getType(nameElement: String, nameType: String, offset: Int): PathSourceElementType =
        if (offset != -1) {
            PathSourceElementType.valueOfKind(nameType.substring(0, offset).toLowerCase())
        } else {
            PathSourceElementType.valueOfKind(nameType.toLowerCase())
        } ?: throw PathParseException("Unknown type ($nameType) of element '$nameElement' by path '$path'")

    override fun iterator(): Iterator<PathSourceElement> = elements.iterator()
    override fun toString(): String = path
    override fun isEmpty() = elements.isEmpty()
    override fun isNotEmpty() = elements.isNotEmpty()
}

class StringPathSource(path: String) :
    AbstractPathSource(path = path, expectedAttributeType = PathSourceElementType.STRING)

class BooleanPathSource(path: String) :
    AbstractPathSource(path = path, expectedAttributeType = PathSourceElementType.BOOLEAN)

class IntegerPathSource(path: String) :
    AbstractPathSource(path = path, expectedAttributeType = PathSourceElementType.INTEGER)

class NumberPathSource(path: String) :
    AbstractPathSource(path = path, expectedAttributeType = PathSourceElementType.NUMBER)
