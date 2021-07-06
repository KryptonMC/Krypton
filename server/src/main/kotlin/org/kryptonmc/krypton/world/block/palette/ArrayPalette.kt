package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.util.varIntSize
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.BLOCKS
import org.kryptonmc.krypton.world.block.toBlock

class ArrayPalette(private val bits: Int, private val resizer: (Int, Block) -> Int) : Palette {

    private val values = arrayOfNulls<Block>(1 shl bits)
    override var size = 0

    override fun get(value: Block): Int {
        for (i in 0 until size) if (values[i] == value) return i
        val size = size
        return if (size < values.size) {
            values[size] = value
            this.size++
            size
        } else {
            resizer(bits + 1, value)
        }
    }

    override fun get(id: Int) = if (id in 0 until size) values[id] else null

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(size)
        for (i in 0 until size) buf.writeVarInt(BLOCKS.idOf(values[i]!!))
    }

    override fun load(tag: NBTList<NBTCompound>) {
        tag.forEachIndexed { index, entry -> values[index] = entry.toBlock() }
        size = tag.size
    }

    override val serializedSize: Int
        get() {
            var temp = size.varIntSize()
            for (i in 0 until size) temp += BLOCKS.idOf(values[i]!!).varIntSize()
            return temp
        }
}
