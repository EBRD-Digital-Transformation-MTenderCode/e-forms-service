package com.procurement.formsservice.json.data.enumerator

import com.procurement.formsservice.json.BOOLEAN
import com.procurement.formsservice.json.INTEGER
import com.procurement.formsservice.json.NUMBER
import com.procurement.formsservice.json.STRING
import com.procurement.formsservice.json.exception.EnumDefinitionException

abstract class AbstractEnumElementsDefinition<V, T : EnumElementDefinition<V>>(elements: Set<T>) {
    private val _values = linkedSetOf<V>()
    private val _names = mutableListOf<String>()
    private val _descriptions = mutableListOf<String>()

    val values: Set<V>
        get() = _values
    val names: List<String>
        get() = _names
    val descriptions: List<String>
        get() = _descriptions

    init {
        if (elements.isEmpty()) {
            throw EnumDefinitionException("List of the elements is empty.")
        }
        for (element in elements) {
            _values.add(element.value)

            if (element.name.isNotEmpty()) {
                _names.add(element.name)
            }

            if (element.description.isNotBlank()) {
                _descriptions.add(element.description)
            }
        }
        if (names.isNotEmpty() && names.size != elements.size) {
            throw EnumDefinitionException("The number of elements of the list of the names: [${names.size}] does not match the number of values: [${elements.size}].")
        }
        if (descriptions.isNotEmpty() && descriptions.size != elements.size) {
            throw EnumDefinitionException("The number of elements of the list of the descriptions: [${descriptions.size}] does not match the number of values: [${elements.size}].")
        }
    }
}

class BooleanEnumElementsDefinition(elements: Set<BooleanEnumElementDefinition>) :
    AbstractEnumElementsDefinition<BOOLEAN, BooleanEnumElementDefinition>(elements)

class StringEnumElementsDefinition(elements: Set<StringEnumElementDefinition>) :
    AbstractEnumElementsDefinition<STRING, StringEnumElementDefinition>(elements)

class IntegerEnumElementsDefinition(elements: Set<IntegerEnumElementDefinition>) :
    AbstractEnumElementsDefinition<INTEGER, IntegerEnumElementDefinition>(elements)

class NumberEnumElementsDefinition(elements: Set<NumberEnumElementDefinition>) :
    AbstractEnumElementsDefinition<NUMBER, NumberEnumElementDefinition>(elements)
