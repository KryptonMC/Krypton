package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.RegistryManager
import org.kryptonmc.krypton.registry.tags.Tag
import org.kryptonmc.krypton.registry.tags.TagManager

class PacketOutTags(
    private val registryManager: RegistryManager,
    private val tagManager: TagManager
) : PlayPacket(0x5B) {

    private val blockRegistry: (NamespacedKey) -> Int? = {
        registryManager.registries.blocks.entries[it]?.protocolId
    }
    private val fluidRegistry: (NamespacedKey) -> Int? = {
        registryManager.registries.fluids.entries[it]?.protocolId
    }
    private val itemRegistry: (NamespacedKey) -> Int? = lambda@{
        registryManager.registries.items.entries[it]?.protocolId
    }
    private val entityRegistry: (NamespacedKey) -> Int? = {
        registryManager.registries.entityTypes.entries[it]?.protocolId
    }

    override fun write(buf: ByteBuf) {
        buf.writeTags(tagManager.blockTags, blockRegistry)
        buf.writeTags(tagManager.itemTags, itemRegistry)
        buf.writeTags(tagManager.fluidTags, fluidRegistry)
        buf.writeTags(tagManager.entityTags, entityRegistry)
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