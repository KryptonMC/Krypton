package org.kryptonmc.krypton.entity

import net.kyori.adventure.nbt.CompoundBinaryTag

data class Slot(
    val isPresent: Boolean,
    val itemId: Int = 0,
    val itemCount: Byte = 0,
    val nbt: CompoundBinaryTag = CompoundBinaryTag.empty()
)