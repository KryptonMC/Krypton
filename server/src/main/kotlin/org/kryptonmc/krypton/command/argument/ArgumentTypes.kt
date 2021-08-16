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
package org.kryptonmc.krypton.command.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.EmptyArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.StringArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.minecraft.EntityArgumentSerializer
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.NBTArgument
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.itemstack.ItemStackPredicateArgument
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.writeKey
import java.util.concurrent.ConcurrentHashMap

object ArgumentTypes {

    private val LOGGER = logger<ArgumentTypes>()
    private val BY_CLASS = ConcurrentHashMap<Class<*>, Entry<*>>()
    private val BY_NAME = ConcurrentHashMap<Key, Entry<*>>()

    init {
        // Brigadier types and serializers
        register<BoolArgumentType>("brigadier:bool", EmptyArgumentSerializer())
        register("brigadier:float", FloatArgumentSerializer())
        register("brigadier:double", DoubleArgumentSerializer())
        register("brigadier:integer", IntegerArgumentSerializer())
        register("brigadier:long", LongArgumentSerializer())
        register("brigadier:string", StringArgumentSerializer())

        // Vanilla serializers
        register<NBTArgument>("nbt_tag", EmptyArgumentSerializer())
        register<NBTCompoundArgument>("nbt_compound_tag", EmptyArgumentSerializer())
        register<SummonEntityArgument>("entity_summon", EmptyArgumentSerializer())
        register<VectorArgument>("vec3", EmptyArgumentSerializer())
        register("entity", EntityArgumentSerializer())
        register<GameProfileArgument>("game_profile", EmptyArgumentSerializer())
        register<ItemStackArgumentType>("item_stack", EmptyArgumentSerializer())
        register<ItemStackPredicateArgument>("item_predicate", EmptyArgumentSerializer())
    }

    operator fun get(key: Key) = BY_NAME[key]

    @Suppress("UNCHECKED_CAST") // this should never fail
    operator fun <T : ArgumentType<*>> get(type: ArgumentType<*>) = BY_CLASS[type::class.java] as? Entry<T>

    fun <T : ArgumentType<*>> ByteBuf.writeArgumentType(argument: T) {
        val entry = get<T>(argument)
        if (entry == null) {
            Messages.ARGUMENT_ERROR.error(LOGGER, argument.toString(), argument::class.java.simpleName)
            writeKey(key(""))
            return
        }
        writeKey(entry.name)
        entry.serializer.write(argument, this)
    }

    private inline fun <reified T : ArgumentType<*>> register(name: String, serializer: ArgumentSerializer<T>) {
        val key = key(name)
        require(!BY_CLASS.containsKey(T::class.java)) { "Class ${T::class.java.simpleName} already has a serializer!" }
        require(!BY_NAME.containsKey(key)) { "'$name' is already a registered serializer!" }

        val entry = Entry(T::class.java, serializer, key)
        BY_CLASS[T::class.java] = entry
        BY_NAME[key] = entry
    }

    data class Entry<T : ArgumentType<*>>(
        val clazz: Class<T>,
        val serializer: ArgumentSerializer<T>,
        val name: Key
    )
}
