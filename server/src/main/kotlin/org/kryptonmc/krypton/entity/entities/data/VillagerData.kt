package org.kryptonmc.krypton.entity.entities.data

/**
 * Various data for villagers. Used for entity metadata.
 *
 * @author Callum Seabrook
 */
data class VillagerData(
    val type: VillagerType,
    val profession: VillagerProfession,
    val level: Int
)