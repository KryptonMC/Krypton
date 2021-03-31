package org.kryptonmc.krypton.packet.out.play

import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.command.argument.ArgumentTypes.writeArgumentType
import org.kryptonmc.krypton.extension.writeKey
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketOutDeclareCommands(private val root: RootCommandNode<Sender>) : PlayPacket(0x10) {

    override fun write(buf: ByteBuf) {
        val enumerations = root.enumerate()
        val ordered = enumerations.entries.associate { it.value to it.key }
        buf.writeVarInt(ordered.size)
        ordered.forEach { buf.writeNode(it.value, enumerations) }
        buf.writeVarInt(enumerations.getValue(root))
    }

    private fun ByteBuf.writeNode(node: CommandNode<*>, enumerations: Map<CommandNode<*>, Int>) {
        writeFlags(node)
        writeVarInt(node.children.size)
        node.children.forEach { writeVarInt(enumerations.getValue(it)) }
        if (node.redirect != null) writeVarInt(enumerations.getValue(node.redirect))

        if (node is ArgumentCommandNode<*, *>) {
            writeString(node.name)
            writeArgumentType(node.type)
            if (node.customSuggestions != null) writeKey(NamespacedKey(value = "ask_server"))
        } else if (node is LiteralCommandNode<*>) {
            writeString(node.name)
        }
    }

    private fun ByteBuf.writeFlags(node: CommandNode<*>) {
        var byte = 0
        if (node.redirect != null) byte = byte or 8
        if (node.command != null) byte = byte or 4
        when (node) {
            is RootCommandNode<*> -> byte = byte or 0
            is LiteralCommandNode<*> -> byte = byte or 1
            is ArgumentCommandNode<*, *> -> {
                byte = byte or 2
                if (node.customSuggestions != null) byte = byte or 0x10
            }
        }
        writeByte(byte)
    }

    private fun RootCommandNode<*>.enumerate(): Map<CommandNode<*>, Int> {
        val result = mutableMapOf<CommandNode<*>, Int>()
        val queue = ArrayDeque<CommandNode<*>>()
        queue += this

        while (queue.isNotEmpty()) {
            val element = queue.removeFirst()
            if (element in result) continue
            val size = result.size
            result[element] = size
            queue += element.children
            if (element.redirect != null) queue += element.redirect
        }

        return result
    }
}