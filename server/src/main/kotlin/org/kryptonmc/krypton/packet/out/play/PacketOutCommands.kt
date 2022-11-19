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

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import it.unimi.dsi.fastutil.ints.IntSets
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.SuggestionProviders
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.readVarIntArray
import org.kryptonmc.krypton.util.writeArgumentType
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntArray
import java.util.function.BiPredicate
import java.util.function.Predicate

@JvmRecord
data class PacketOutCommands(val rootIndex: Int, val nodes: List<Node>) : Packet {

    constructor(root: RootCommandNode<Source>) : this(root, enumerate(root))

    private constructor(root: RootCommandNode<Source>,
                        enumerations: Object2IntMap<CommandNode<Source>>) : this(enumerations.getInt(root), createNodes(enumerations))

    constructor(buf: ByteBuf) : this(buf.readList(::readNode), buf.readVarInt())

    private constructor(nodes: List<Node>, rootIndex: Int) : this(rootIndex, nodes) {
        validateEntries(nodes, Node::canBuild)
        validateEntries(nodes, Node::canResolve)
    }

    override fun write(buf: ByteBuf) {
        buf.writeCollection(nodes) { it.write(buf) }
        buf.writeVarInt(rootIndex)
    }

    @JvmRecord
    data class Node(val flags: Int, val children: IntArray, val redirectNode: Int, val data: NodeData?) : Writable {

        fun canBuild(indices: IntSet): Boolean = if (flags and FLAG_REDIRECT != 0) !indices.contains(redirectNode) else true

        fun canResolve(indices: IntSet): Boolean {
            children.forEach { if (indices.contains(it)) return false }
            return true
        }

        override fun write(buf: ByteBuf) {
            buf.writeByte(flags)
            buf.writeVarIntArray(children)
            if (flags and FLAG_REDIRECT != 0) buf.writeVarInt(redirectNode)
            data?.write(buf)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return flags == (other as Node).flags &&
                    children.contentEquals(other.children) &&
                    redirectNode == other.redirectNode &&
                    data == other.data
        }

        override fun hashCode(): Int {
            var result = 1
            result = 31 * result + flags.hashCode()
            result = 31 * result + children.contentHashCode()
            result = 31 * result + redirectNode.hashCode()
            result = 31 * result + data.hashCode()
            return result
        }
    }

    sealed interface NodeData : Writable

    @JvmRecord
    data class LiteralNodeData(val name: String) : NodeData {

        override fun write(buf: ByteBuf) {
            buf.writeString(name)
        }
    }

    @JvmRecord
    data class ArgumentNodeData<T>(val name: String, val type: ArgumentType<T>, val suggestionId: Key?) : NodeData {

        constructor(node: ArgumentCommandNode<Source, T>) : this(node.name, node.type, node.customSuggestions.id())

        override fun write(buf: ByteBuf) {
            buf.writeString(name)
            buf.writeArgumentType(type)
            if (suggestionId != null) buf.writeKey(suggestionId)
        }
    }

    companion object {

        private const val FLAG_EXECUTABLE = 0x04
        private const val FLAG_REDIRECT = 0x08
        private const val FLAG_SUGGESTIONS = 0x10
        private const val TYPE_ROOT = 0
        private const val TYPE_LITERAL = 1
        private const val TYPE_ARGUMENT = 2

        @JvmStatic
        private fun createNodes(enumerations: Object2IntMap<CommandNode<Source>>): List<Node> {
            val nodes = ObjectArrayList<Node>(enumerations.size).apply { size(enumerations.size) }
            Object2IntMaps.fastForEach(enumerations) { nodes.set(it.intValue, createNode(it.key, enumerations)) }
            return nodes
        }

        @JvmStatic
        private fun createNode(node: CommandNode<Source>, enumerations: Object2IntMap<CommandNode<Source>>): Node {
            var flags = 0
            if (node.redirect != null) flags = flags or FLAG_REDIRECT
            val redirectNode = if (node.redirect != null) enumerations.getInt(node.redirect) else 0
            if (node.command != null) flags = flags or FLAG_EXECUTABLE
            val data = when (node) {
                is RootCommandNode<*> -> {
                    flags = flags or TYPE_ROOT
                    null
                }
                is ArgumentCommandNode<*, *> -> {
                    flags = flags or TYPE_ARGUMENT
                    if (node.customSuggestions != null) flags = flags or FLAG_SUGGESTIONS
                    ArgumentNodeData(node as ArgumentCommandNode<Source, *>)
                }
                is LiteralCommandNode<*> -> {
                    flags = flags or TYPE_LITERAL
                    LiteralNodeData(node.literal)
                }
                else -> throw UnsupportedOperationException("Unknown node type $node!")
            }
            val children = node.children.stream().mapToInt(enumerations::getInt).toArray()
            return Node(flags, children, redirectNode, data)
        }

        @JvmStatic
        private fun readNode(buf: ByteBuf): Node {
            val flags = buf.readByte().toInt()
            val children = buf.readVarIntArray()
            val redirectNode = if (flags and FLAG_REDIRECT != 0) buf.readVarInt() else 0
            return Node(flags, children, redirectNode, readNodeData(buf, flags))
        }

        @JvmStatic
        private fun readNodeData(buf: ByteBuf, flags: Int): NodeData? {
            return when (flags and 3) {
                2 -> {
                    val name = buf.readString()
                    val serializer = ArgumentSerializers.get<ArgumentType<*>>(buf.readVarInt()) ?: return null
                    val type = serializer.serializer.read(buf)
                    val suggestionsType = if (flags and FLAG_SUGGESTIONS != 0) buf.readKey() else null
                    return ArgumentNodeData(name, type, suggestionsType)
                }
                1 -> LiteralNodeData(buf.readString())
                else -> null
            }
        }

        @JvmStatic
        private fun validateEntries(nodes: List<Node>, predicate: BiPredicate<Node, IntSet>) {
            val set = IntOpenHashSet(IntSets.fromTo(0, nodes.size))
            while (set.isNotEmpty()) {
                require(set.removeIf(Predicate { predicate.test(nodes.get(it), set) })) { "Server sent an impossible command tree!" }
            }
        }

        // a breadth-first search algorithm to enumerate a root node
        @JvmStatic
        private fun enumerate(root: RootCommandNode<Source>): Object2IntMap<CommandNode<Source>> {
            val result = Object2IntOpenHashMap<CommandNode<Source>>()
            val queue = ArrayDeque<CommandNode<Source>>()
            queue.add(root)

            while (queue.isNotEmpty()) {
                val element = queue.removeFirst()
                if (result.containsKey(element)) continue
                val size = result.size
                result.put(element, size)
                queue.addAll(element.children)
                if (element.redirect != null) queue.add(element.redirect)
            }

            return result
        }
    }
}

private typealias Source = CommandSourceStack

private fun SuggestionProvider<Source>?.id(): Key? = if (this == null) null else SuggestionProviders.name(this)
