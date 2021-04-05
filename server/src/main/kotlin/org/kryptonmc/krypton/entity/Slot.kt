package org.kryptonmc.krypton.entity

import net.kyori.adventure.nbt.CompoundBinaryTag

data class Slot(
    val isPresent: Boolean,
    val id: Int = 0,
    val count: Byte = 0,
    val nbt: CompoundBinaryTag = CompoundBinaryTag.empty()
)