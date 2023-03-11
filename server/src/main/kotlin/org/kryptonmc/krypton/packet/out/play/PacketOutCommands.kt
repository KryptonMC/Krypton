/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntArray
import java.util.function.BiPredicate
import java.util.function.Predicate

@JvmRecord
data class PacketOutCommands(val nodes: List<Node>, val rootIndex: Int) : Packet {

    constructor(buf: ByteBuf) : this(buf.readList(::readNode), buf.readVarInt()) {
        validateEntries(nodes) { node, indices -> node.canBuild(indices) }
        validateEntries(nodes) { node, indices -> node.canResolve(indices) }
    }

    override fun write(buf: ByteBuf) {
        buf.writeCollection(nodes) { it.write(buf) }
        buf.writeVarInt(rootIndex)
    }

    @JvmRecord
    @Suppress("ArrayInDataClass")
    data class Node(val flags: Int, val children: IntArray, val redirectNode: Int, val data: NodeData?) : Writable {

        fun canBuild(indices: IntSet): Boolean = if (flags and FLAG_REDIRECT != 0) !indices.contains(redirectNode) else true

        fun canResolve(indices: IntSet): Boolean = children.all { !indices.contains(it) }

        override fun write(buf: ByteBuf) {
            buf.writeByte(flags)
            buf.writeVarIntArray(children)
            if (flags and FLAG_REDIRECT != 0) buf.writeVarInt(redirectNode)
            data?.write(buf)
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

        constructor(node: ArgumentCommandNode<CommandSourceStack, T>) : this(node.name, node.type, getId(node.customSuggestions))

        override fun write(buf: ByteBuf) {
            buf.writeString(name)
            ArgumentSerializers.write(buf, type)
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
        fun createFromRootNode(root: RootCommandNode<CommandSourceStack>): PacketOutCommands {
            val enumerations = enumerate(root)
            return PacketOutCommands(createNodes(enumerations), enumerations.getInt(root))
        }

        @JvmStatic
        private fun createNodes(enumerations: Object2IntMap<CommandNode<CommandSourceStack>>): List<Node> {
            val nodes = ObjectArrayList<Node>(enumerations.size).apply { size(enumerations.size) }
            Object2IntMaps.fastForEach(enumerations) { nodes.set(it.intValue, createNode(it.key, enumerations)) }
            return nodes
        }

        @JvmStatic
        private fun createNode(node: CommandNode<CommandSourceStack>, enumerations: Object2IntMap<CommandNode<CommandSourceStack>>): Node {
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
                    ArgumentNodeData(node as ArgumentCommandNode<CommandSourceStack, *>)
                }
                is LiteralCommandNode<*> -> {
                    flags = flags or TYPE_LITERAL
                    LiteralNodeData(node.literal)
                }
                else -> throw UnsupportedOperationException("Unknown node type $node!")
            }
            val children = node.children.stream().mapToInt { enumerations.getInt(it) }.toArray()
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
                    val serializer = ArgumentSerializers.getById<ArgumentType<*>>(buf.readVarInt()) ?: return null
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
        private fun enumerate(root: RootCommandNode<CommandSourceStack>): Object2IntMap<CommandNode<CommandSourceStack>> {
            val result = Object2IntOpenHashMap<CommandNode<CommandSourceStack>>()
            val queue = ArrayDeque<CommandNode<CommandSourceStack>>()
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

        @JvmStatic
        private fun getId(provider: SuggestionProvider<CommandSourceStack>?): Key? = provider?.let(SuggestionProviders::getName)
    }
}
