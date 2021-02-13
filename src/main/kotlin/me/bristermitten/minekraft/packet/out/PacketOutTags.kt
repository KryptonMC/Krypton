package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket
import me.bristermitten.minekraft.registry.*
import me.bristermitten.minekraft.registry.tags.Tag
import me.bristermitten.minekraft.registry.tags.TagManager

class PacketOutTags(private val registryManager: RegistryManager, private val tagManager: TagManager) : PlayPacket(0x5B) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(tagManager.blockTags.size) // number of block tags
        buf.writeBlockTags() // block tags

        buf.writeVarInt(tagManager.entityTypeTags.size) // number of entity tags
        buf.writeEntityTags()

        buf.writeVarInt(tagManager.fluidTags.size) // number of fluid tags
        buf.writeFluidTags()

        buf.writeVarInt(tagManager.itemTags.size)
        buf.writeItemTags()
    }

    private fun ByteBuf.writeBlockTags() {
        val blockRegistry = registryManager.registries.blocks
        for (tag in tagManager.blockTags) {
            writeString(tag.name)
            writeVarInt(tag.data.values.size)

            writeTags(tagManager.getDeepTags(tag), blockRegistry)
        }
    }

    private fun ByteBuf.writeEntityTags() {
        val entityTypeRegistry = registryManager.registries.entityTypes
        for (tag in tagManager.entityTypeTags) {
            writeString(tag.name)
            writeVarInt(tag.data.values.size)

            writeTags(tagManager.getDeepTags(tag), entityTypeRegistry)
        }
    }

    private fun ByteBuf.writeFluidTags() {
        val fluidRegistry = registryManager.registries.fluids
        for (tag in tagManager.fluidTags) {
            writeString(tag.name)
            writeVarInt(tag.data.values.size)

            writeTags(tagManager.getDeepTags(tag), fluidRegistry)
        }
    }

    private fun ByteBuf.writeItemTags() {
        val itemRegistry = registryManager.registries.items
        for (tag in tagManager.itemTags) {
            writeString(tag.name)
            writeVarInt(tag.data.values.size)

            writeTags(tagManager.getDeepTags(tag), itemRegistry)
        }
    }

//    private fun ByteBuf.writeTags(entries: List<String>) {
//        val blockRegistry = registryManager.registries.blocks
//        for (entry in entries) {
//            if (entry.startsWith("#")) {
//                writeTags(tagManager.blockTags[entry.split(":")[1]]!!.values)
//            } else {
//                writeVarInt(blockRegistry.entries[entry.toNamespacedKey()]!!.protocolId)
//            }
//        }
//    }

    private fun ByteBuf.writeTags(tags: List<Tag>, registry: Registry) {
        for (tag in tags) {
            writeVarInt(registry.entries.getValue(NamespacedKey("minecraft", tag.name)).protocolId)
        }
    }

    private fun ByteBuf.writeTags(tags: List<Tag>, registry: DefaultedRegistry) =
        writeTags(tags, Registry(registry.protocolId, registry.entries))
}
