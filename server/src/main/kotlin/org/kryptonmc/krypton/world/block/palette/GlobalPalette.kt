package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.krypton.util.varIntSize
import org.kryptonmc.krypton.world.block.BLOCKS

object GlobalPalette : Palette {

    override val size = BLOCKS.size
    override val serializedSize = 0.varIntSize()

    override fun get(value: Block) = BLOCKS.idOf(value).takeIf { it != -1 } ?: 0

    override fun get(id: Int) = BLOCKS[id] ?: Blocks.AIR

    override fun write(buf: ByteBuf) = Unit

    override fun load(tag: NBTList<NBTCompound>) = Unit
}
