package me.bristermitten.minekraft.entity.entities.villager

import me.bristermitten.minekraft.registry.NamespacedKey

enum class VillagerType(val key: NamespacedKey, val id: Int) {

    DESERT(NamespacedKey(value = "desert"), 0),
    JUNGLE(NamespacedKey(value = "jungle"), 1),
    PLAINS(NamespacedKey(value = "plains"), 2),
    SAVANNA(NamespacedKey(value = "savanna"), 3),
    SNOW(NamespacedKey(value = "snow"), 4),
    SWAMP(NamespacedKey(value = "swamp"), 5),
    TAIGA(NamespacedKey(value = "taiga"), 6)
}