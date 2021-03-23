package org.kryptonmc.krypton.entity.entities.data

import org.kryptonmc.krypton.api.registry.NamespacedKey

enum class VillagerProfession(val id: Int) {

    NONE(0),
    ARMORER(1),
    BUTCHER(2),
    CARTOGRAPHER(3),
    CLERIC(4),
    FARMER(5),
    FISHERMAN(6),
    FLETCHER(7),
    LEATHERWORKER(8),
    LIBRARIAN(9),
    MASON(10),
    NITWIT(11),
    SHEPHERD(12),
    TOOLSMITH(13),
    WEAPONSMITH(14);

    val key by lazy { NamespacedKey(value = name.toLowerCase()) }
}