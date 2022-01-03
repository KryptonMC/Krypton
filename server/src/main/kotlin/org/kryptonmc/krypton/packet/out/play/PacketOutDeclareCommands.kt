/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeArgumentType
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

@JvmRecord
data class PacketOutDeclareCommands(val root: RootCommandNode<Sender>) : Packet {

    override fun write(buf: ByteBuf) {
        val enumerations = enumerate(root)
        val ordered = ObjectArrayList<CommandNode<Sender>>().apply { size(enumerations.size) }
        Object2IntMaps.fastForEach(enumerations) { ordered[it.intValue] = it.key }
        buf.writeVarInt(ordered.size)
        ordered.forEach { writeNode(buf, it, enumerations) }
        buf.writeVarInt(enumerations.getValue(root))
    }

    companion object {

        @JvmStatic
        private fun writeNode(buf: ByteBuf, node: CommandNode<Sender>, enumerations: Map<CommandNode<Sender>, Int>) {
            writeFlags(buf, node)
            buf.writeCollection(node.children) { buf.writeVarInt(enumerations.getValue(it)) }
            if (node.redirect != null) buf.writeVarInt(enumerations.getValue(node.redirect))

            if (node is ArgumentCommandNode<*, *>) {
                node as ArgumentCommandNode<Sender, *>
                buf.writeString(node.name)
                buf.writeArgumentType(node.type)
                if (node.customSuggestions != null) buf.writeKey(SuggestionProviders.name(node.customSuggestions))
            } else if (node is LiteralCommandNode<*>) {
                buf.writeString(node.name)
            }
        }

        @JvmStatic
        private fun writeFlags(buf: ByteBuf, node: CommandNode<Sender>) {
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
            buf.writeByte(byte)
        }

        // a breadth-first search algorithm to enumerate a root node
        @JvmStatic
        private fun enumerate(root: RootCommandNode<Sender>): Object2IntMap<CommandNode<Sender>> {
            val result = Object2IntOpenHashMap<CommandNode<Sender>>()
            val queue = ArrayDeque<CommandNode<Sender>>()
            queue.add(root)

            while (queue.isNotEmpty()) {
                val element = queue.removeFirst()
                if (element in result) continue
                val size = result.size
                result[element] = size
                queue.addAll(element.children)
                if (element.redirect != null) queue.add(element.redirect)
            }

            return result
        }
    }
}
