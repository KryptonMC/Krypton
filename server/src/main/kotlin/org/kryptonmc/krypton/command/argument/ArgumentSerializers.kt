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
package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.EntityArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.SingletonArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.StringArgumentSerializer
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.NBTArgument
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicateArgument
import org.kryptonmc.krypton.util.writeVarInt
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds all of the built-in argument serializers for all of the argument
 * types that we use that need to be sent to the client.
 */
object ArgumentSerializers {

    private val BY_CLASS = ConcurrentHashMap<Class<*>, Entry<*>>()
    private val BY_ID = Int2ObjectOpenHashMap<Entry<*>>()

    @JvmStatic
    fun bootstrap() {
        // Brigadier serializers
        singleton(0, "brigadier:bool", BoolArgumentType.bool())
        register(1, "brigadier:float", FloatArgumentSerializer)
        register(2, "brigadier:double", DoubleArgumentSerializer)
        register(3, "brigadier:integer", IntegerArgumentSerializer)
        register(4, "brigadier:long", LongArgumentSerializer)
        register(5, "brigadier:string", StringArgumentSerializer)

        // Built-in serializers
        register(6, "entity", EntityArgumentSerializer)
        singleton(7, "game_profile", GameProfileArgument)
        singleton(10, "vec3", VectorArgument.normal())
        singleton(14, "item_stack", ItemStackArgumentType)
        singleton(15, "item_predicate", ItemStackPredicateArgument)
        singleton(19, "nbt_compound_tag", NBTCompoundArgument)
        singleton(20, "nbt_tag", NBTArgument)
        singleton(40, "entity_summon", SummonEntityArgument)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : ArgumentType<*>> getByType(type: T): Entry<T>? = BY_CLASS.get(type.javaClass) as? Entry<T>

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : ArgumentType<*>> getById(id: Int): Entry<T>? = BY_ID.get(id) as? Entry<T>

    @JvmStatic
    private inline fun <reified T : ArgumentType<*>> register(id: Int, name: String, serializer: ArgumentSerializer<T>) {
        register(id, name, T::class.java, serializer)
    }

    @JvmStatic
    private fun <T : ArgumentType<*>> register(id: Int, name: String, type: Class<T>, serializer: ArgumentSerializer<T>) {
        val entry = Entry(id, Key.key(name), type, serializer)
        BY_CLASS.put(type, entry)
        BY_ID.put(id, entry)
    }

    @JvmStatic
    private inline fun <reified T : ArgumentType<*>> singleton(id: Int, name: String, value: T) {
        register(id, name, SingletonArgumentSerializer(value))
    }

    @JvmStatic
    fun <T : ArgumentType<*>> write(buf: ByteBuf, type: T) {
        val entry = checkNotNull(getByType(type)) { "Argument type for node must have registered serializer!" }
        buf.writeVarInt(entry.id)
        entry.serializer.write(buf, type)
    }

    @JvmRecord
    data class Entry<T : ArgumentType<*>>(val id: Int, val name: Key, val clazz: Class<T>, val serializer: ArgumentSerializer<T>)
}
