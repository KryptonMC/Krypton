package org.kryptonmc.krypton.entity.entities.data

import org.kryptonmc.krypton.registry.NamespacedKey

enum class VillagerProfession(val key: NamespacedKey, val id: Int) {

    NONE(NamespacedKey(value = "none"), 0),
    ARMORER(NamespacedKey(value = "armorer"), 1),
    BUTCHER(NamespacedKey(value = "butcher"), 2),
    CARTOGRAPHER(NamespacedKey(value = "cartographer"), 3),
    CLERIC(NamespacedKey(value = "cleric"), 4),
    FARMER(NamespacedKey(value = "farmer"), 5),
    FISHERMAN(NamespacedKey(value = "fisherman"), 6),
    FLETCHER(NamespacedKey(value = "fletcher"), 7),
    LEATHERWORKER(NamespacedKey(value = "leatherworker"), 8),
    LIBRARIAN(NamespacedKey(value = "librarian"), 9),
    MASON(NamespacedKey(value = "mason"), 10),
    NITWIT(NamespacedKey(value = "nitwit"), 11),
    SHEPHERD(NamespacedKey(value = "shepherd"), 12),
    TOOLSMITH(NamespacedKey(value = "toolsmith"), 13),
    WEAPONSMITH(NamespacedKey(value = "weaponsmith"), 14)
}