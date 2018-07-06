package com.procurement.formsservice.json.path

import com.procurement.formsservice.json.exception.PathParseException

enum class PathDestinationElementType(val kind: String) {
    OBJECT("object"),
    ARRAY_OF_OBJECTS("array[object]"),
    ARRAY_OF_STRING("array[string]"),
    ARRAY_OF_BOOLEAN("array[boolean]"),
    ARRAY_OF_INTEGER("array[integer]"),
    ARRAY_OF_NUMBER("array[number]"),
    STRING("string"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    NUMBER("number");

    companion object {
        private val map = mutableMapOf<String, PathDestinationElementType>()

        init {
            for (item in values()) {
                map[item.kind] = item
            }
        }

        fun valueOfKind(kind: String): PathDestinationElementType? = map[kind]
    }
}

data class PathDestinationElement(val name: String, val type: PathDestinationElementType)

sealed class AbstractPathDestination(private val path: String, expectedAttributeType: PathDestinationElementType) :
    Iterable<PathDestinationElement> {
    private val elements: List<PathDestinationElement>

    init {
        if (path.isNotBlank()) {
            elements = getPath(path)
            val actualAttributeType = elements[elements.size - 1].type
            if (actualAttributeType != expectedAttributeType)
                throw PathParseException("Invalid type of attribute by path '$path'. Expected type '$expectedAttributeType', actual type '$actualAttributeType'")
        } else {
            elements = emptyList()
        }
    }

    override fun iterator(): Iterator<PathDestinationElement> = elements.iterator()

    override fun toString(): String = path

    private fun getPath(path: String): List<PathDestinationElement> {
        return path.split("/").map { toPathElement(path, it) }
    }

    private fun toPathElement(path: String, element: String): PathDestinationElement {
        val posType = element.indexOf("::")
        if (posType == -1)
            throw PathParseException("Invalid format of path '$path'.")

        val name = element.substring(0, posType)
        val nameType = element.substring(posType + 2)
        val type = PathDestinationElementType.valueOfKind(nameType.toLowerCase())
            ?: throw PathParseException("Unknown type ($nameType) of element '$name' by path '$path'")
        return PathDestinationElement(name, type)
    }

    fun isEmpty() = elements.isEmpty()
    fun isNotEmpty() = elements.isNotEmpty()
}

class StringPathDestination(path: String) :
    AbstractPathDestination(path = path, expectedAttributeType = PathDestinationElementType.STRING)

class BooleanPathDestination(path: String) :
    AbstractPathDestination(path = path, expectedAttributeType = PathDestinationElementType.BOOLEAN)

class IntegerPathDestination(path: String) :
    AbstractPathDestination(path = path, expectedAttributeType = PathDestinationElementType.INTEGER)

class NumberPathDestination(path: String) :
    AbstractPathDestination(path = path, expectedAttributeType = PathDestinationElementType.NUMBER)

class StringArrayPathDestination(path: String) :
    AbstractPathDestination(path = path, expectedAttributeType = PathDestinationElementType.ARRAY_OF_STRING)
