/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play

import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.argument.ArgumentTypes.writeArgumentType
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Declares commands that exist on the server to the client.
 *
 * This is done by transforming the [root]'s children into a tree, then using a breadth first search
 * to put all of the nodes into an ordered list, which is then sent to the client.
 *
 * @param root the root command node
 */
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
            if (node.customSuggestions != null) writeKey(key("ask_server"))
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

    // a breadth-first search algorithm to enumerate a root node
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
