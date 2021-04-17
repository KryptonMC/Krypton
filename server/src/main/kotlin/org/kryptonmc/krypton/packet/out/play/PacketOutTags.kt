package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.Registries
import org.kryptonmc.krypton.registry.tags.Tag
import org.kryptonmc.krypton.registry.tags.TagManager

/**
 * Tells the client all the tags present on the server. This is an object as it only needs to be instantiated once
 *
 * @author Callum Seabrook
 */
object PacketOutTags : PlayPacket(0x5B) {

    private val blockRegistry: (NamespacedKey) -> Int? = {
        Registries.BLOCKS.idOf(it)
    }
    private val fluidRegistry: (NamespacedKey) -> Int? = {
        Registries.FLUIDS.idOf(it)
    }
    private val itemRegistry: (NamespacedKey) -> Int? = {
        Registries.ITEMS.idOf(it)
    }
    private val entityRegistry: (NamespacedKey) -> Int? = {
        Registries.ENTITY_TYPES.idOf(it)
    }

    override fun write(buf: ByteBuf) {
        buf.writeTags(TagManager.blockTags, blockRegistry)
        buf.writeTags(TagManager.itemTags, itemRegistry)
        buf.writeTags(TagManager.fluidTags, fluidRegistry)
        buf.writeTags(TagManager.entityTags, entityRegistry)
    }

    private fun ByteBuf.writeTags(tags: Set<Tag>, registry: (NamespacedKey) -> Int?) {
        writeVarInt(tags.size)
        tags.forEach { tag ->
            writeString(tag.name.toString())

            val values = tag.values.filter { registry(it) != null }
            writeVarInt(values.size)
            values.forEach { writeVarInt(registry(it)!!) }
        }
    }
}