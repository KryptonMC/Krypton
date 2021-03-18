package org.kryptonmc.krypton.api.block

/**
 * Represents block lighting.
 *
 * [level] is the main level of this block.
 *
 * [sky] is the amount of light this block gets from the sky.
 *
 * [block] is the amount of light this block gets from other nearby blocks.
 *
 * @author Callum Seabrook
 */
data class BlockLighting(
    val level: Byte,
    val sky: Byte,
    val block: Byte
)