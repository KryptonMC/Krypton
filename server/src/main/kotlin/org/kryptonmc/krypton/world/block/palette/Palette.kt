package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.jglrxavpok.hephaistos.nbt.NBT
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.util.nbt.Serializable

interface Palette {

    val size: Int
    val serializedSize: Int

    operator fun get(value: Block): Int

    operator fun get(id: Int): Block?

    fun write(buf: ByteBuf)

    fun load(tag: NBTList<NBTCompound>)
}
