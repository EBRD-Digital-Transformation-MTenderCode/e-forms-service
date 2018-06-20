package com.procurement.formsservice.json.data.enumerator

import com.procurement.formsservice.json.exception.EnumDefinitionException

abstract class AbstractEnumElementsDefinition<V, T : EnumElementDefinition<V>>(elements: Array<out T>) {
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
        for (element in elements.toSet()) {
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

class BooleanEnumElementsDefinition(vararg elements: BooleanEnumElementDefinition) :
    AbstractEnumElementsDefinition<Boolean, BooleanEnumElementDefinition>(elements)

class StringEnumElementsDefinition(vararg elements: StringEnumElementDefinition) :
    AbstractEnumElementsDefinition<String, StringEnumElementDefinition>(elements)

class IntegerEnumElementsDefinition(vararg elements: IntegerEnumElementDefinition) :
    AbstractEnumElementsDefinition<Long, IntegerEnumElementDefinition>(elements)

class NumberEnumElementsDefinition(vararg elements: NumberEnumElementDefinition) :
    AbstractEnumElementsDefinition<Float, NumberEnumElementDefinition>(elements)
