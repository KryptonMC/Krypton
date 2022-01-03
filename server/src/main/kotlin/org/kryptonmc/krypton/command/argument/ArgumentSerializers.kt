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
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.EntityArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.StringArgumentSerializer
import org.kryptonmc.krypton.command.arguments.GameProfileArgument
import org.kryptonmc.krypton.command.arguments.NBTArgument
import org.kryptonmc.krypton.command.arguments.NBTCompoundArgument
import org.kryptonmc.krypton.command.arguments.SummonEntityArgument
import org.kryptonmc.krypton.command.arguments.VectorArgument
import org.kryptonmc.krypton.command.arguments.item.ItemStackArgumentType
import org.kryptonmc.krypton.command.arguments.item.ItemStackPredicateArgument
import java.util.concurrent.ConcurrentHashMap

/**
 * Holds all of the built-in argument serializers for all of the argument
 * types that we use that need to be sent to the client.
 */
object ArgumentSerializers {

    private val ENTRIES = ConcurrentHashMap<Class<*>, Entry<*>>()

    init {
        // Brigadier serializers
        empty<BoolArgumentType>("brigadier:bool")
        register("brigadier:double", DoubleArgumentSerializer)
        register("brigadier:float", FloatArgumentSerializer)
        register("brigadier:integer", IntegerArgumentSerializer)
        register("brigadier:long", LongArgumentSerializer)
        register("brigadier:string", StringArgumentSerializer)

        // Built-in serializers
        empty<NBTArgument>("nbt_tag")
        empty<NBTCompoundArgument>("nbt_compound_tag")
        empty<SummonEntityArgument>("entity_summon")
        empty<VectorArgument>("vec3")
        register("entity", EntityArgumentSerializer)
        empty<GameProfileArgument>("game_profile")
        empty<ItemStackArgumentType>("item_stack")
        empty<ItemStackPredicateArgument>("item_predicate")
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : ArgumentType<*>> get(type: T): Entry<T>? = ENTRIES[type::class.java] as? Entry<T>

    @JvmStatic
    private inline fun <reified T : ArgumentType<*>> register(name: String, serializer: ArgumentSerializer<T>) {
        ENTRIES[T::class.java] = Entry(Key.key(name), T::class.java, serializer)
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private inline fun <reified T : ArgumentType<*>> empty(name: String) {
        register(name, ArgumentSerializer.Empty as ArgumentSerializer<T>)
    }

    @JvmRecord
    data class Entry<T : ArgumentType<*>>(val name: Key, val clazz: Class<T>, val serializer: ArgumentSerializer<T>)
}
