package org.kryptonmc.krypton.entity

import net.kyori.adventure.nbt.CompoundBinaryTag

/**
 * A protocol slot. The API equivalent of this would be [org.kryptonmc.krypton.api.inventory.item.ItemStack]
 */
data class Slot(
    val isPresent: Boolean,
    val id: Int = 0,
    val count: Byte = 0,
    val nbt: CompoundBinaryTag = CompoundBinaryTag.empty()
)
