package com.procurement.formsservice.domain

import com.procurement.formsservice.domain.query.v4.TypeTransformer

private const val PREFIX_GROUP = "prefix"
private const val COUNTRY_GROUP = "country"
private const val ENTITY_GROUP = "entity"
private const val TIMESTAMP1_GROUP = "ts1"
private const val TIMESTAMP2_GROUP = "ts2"
private val processIdPattern =
    "^(?<$PREFIX_GROUP>ocds-[a-zA-Z0-9]{6})-(?<$COUNTRY_GROUP>[A-Z]{2})-(?<$TIMESTAMP1_GROUP>[0-9]{13})(-(?<$ENTITY_GROUP>[A-Z]{2,3})-(?<$TIMESTAMP2_GROUP>[0-9]{13}))?$".toRegex()

private fun matchingByGroups(value: String) = processIdPattern.matchEntire(value)?.groups

object StringToOCIDTransformer : TypeTransformer<OCID> {
    override val type: Class<OCID> = OCID::class.java
    override fun invoke(value: String): OCID? = OCID.valueOf(value)
}

object StringToCPIDTransformer : TypeTransformer<CPID> {
    override val type: Class<CPID> = CPID::class.java
    override fun invoke(value: String): CPID? = CPID.valueOf(value)
}

class CPID(val prefix: String, val country: String, private val timestamp1: String) {
    val value: String = "$prefix-$country-$timestamp1"

    override fun toString(): String = value

    companion object {
        fun valueOf(value: String): CPID? {
            val groups = matchingByGroups(value) ?: return null
            val prefix = groups[PREFIX_GROUP]!!.value
            val country = groups[COUNTRY_GROUP]!!.value
            val timestamp1 = groups[TIMESTAMP1_GROUP]!!.value

            return CPID(
                prefix = prefix,
                country = country,
                timestamp1 = timestamp1
            )
        }
    }
}

class OCID(val prefix: String,
           val country: String,
           val entity: Entity,
           private val timestamp1: String,
           private val timestamp2: String) {
    val value: String = "$prefix-$country-$timestamp1-$entity-$timestamp2"

    override fun toString(): String = value

    fun toCPID(): CPID = CPID(prefix = prefix, country = country, timestamp1 = timestamp1)

    companion object {
        fun valueOf(value: String): OCID? {
            val groups = matchingByGroups(value) ?: return null
            val entity = groups[ENTITY_GROUP]?.let {
                Entity.of(it.value)
            } ?: return null

            val prefix = groups[PREFIX_GROUP]!!.value
            val country = groups[COUNTRY_GROUP]!!.value
            val timestamp1 = groups[TIMESTAMP1_GROUP]!!.value
            val timestamp2 = groups[TIMESTAMP2_GROUP]!!.value

            return OCID(
                prefix = prefix,
                country = country,
                entity = entity,
                timestamp1 = timestamp1,
                timestamp2 = timestamp2
            )
        }
    }

    enum class Entity(val description: String) {
        NONE("N/A"),
        FS(""),
        PN("Planning notice"),
        PIN("Priore information notice"),
        PS("Preselection"),
        PQ("Prequalification"),
        EV("Evaluation"),
        AC("Awarded contracts");

        companion object {
            private val items: Map<String, Entity> = mutableMapOf<String, Entity>().apply {
                enumValues<Entity>().forEach {
                    this[it.name] = it
                }
            }

            fun of(name: String): Entity? = items[name]
        }
    }
}

