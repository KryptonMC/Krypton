package org.kryptonmc.krypton.entity.entities.data

import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * Types of villagers
 *
 * @author Callum Seabrook
 */
enum class VillagerType(val id: Int) {

    DESERT(0),
    JUNGLE(1),
    PLAINS(2),
    SAVANNA(3),
    SNOW(4),
    SWAMP(5),
    TAIGA(6);

    val key by lazy { NamespacedKey(value = name.toLowerCase()) }
}