package org.kryptonmc.krypton.world.block

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.util.toKey
import org.kryptonmc.krypton.util.IdMapper
import org.kryptonmc.krypton.world.transform

val BLOCKS = IdMapper<Block>().apply {
    KryptonBlockLoader.STATE_MAP.forEach { set(it.value, it.key) }
}

fun NBTCompound.toBlock(): Block {
    if (!contains("Name", NBTTypes.TAG_String)) return Blocks.AIR
    var block = BLOCK_LOADER.fromKey(getString("Name").toKey()) ?: error("No block found with key ${getString("Name")}!")
    if (contains("Properties", NBTTypes.TAG_Compound)) {
        block = block.withProperties(getCompound("Properties").transform { it.key to (it.value as NBTString).value })
    }
    return block
}

fun Block.toNBT() = NBTCompound()
    .setString("Name", key.asString())
    .apply {
        if (properties.isNotEmpty()) {
            val propertiesTag = NBTCompound().apply { properties.forEach { setString(it.key, it.value) } }
            set("Properties", propertiesTag)
        }
    }
