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
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.command.argument.serializer.ArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.EmptyArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.DoubleArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.FloatArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.IntegerArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.LongArgumentSerializer
import org.kryptonmc.krypton.command.argument.serializer.brigadier.StringArgumentSerializer
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.writeKey
import kotlin.reflect.KClass

/**
 * Holder for argument serialisers used by Brigadier.
 *
 * An almost exact replica of the internal one made by Mojang, with a few changes like
 * the use of `KClass` over `Class` to make it easier for us.
 */
object ArgumentTypes {

    private val LOGGER = logger<ArgumentTypes>()
    private val BY_CLASS = mutableMapOf<KClass<*>, Entry<*>>()
    private val BY_NAME = mutableMapOf<NamespacedKey, Entry<*>>()

    init {
        // Brigadier types and serialisers
        register("brigadier:bool", BoolArgumentType::class, EmptyArgumentSerializer())
        register("brigadier:float", FloatArgumentType::class, FloatArgumentSerializer())
        register("brigadier:double", DoubleArgumentType::class, DoubleArgumentSerializer())
        register("brigadier:integer", IntegerArgumentType::class, IntegerArgumentSerializer())
        register("brigadier:long", LongArgumentType::class, LongArgumentSerializer())
        register("brigadier:string", StringArgumentType::class, StringArgumentSerializer())
    }

    operator fun get(key: NamespacedKey) = BY_NAME[key]

    /**
     * Retrieve an [Entry] for the specified [type]
     *
     * @param T the type of the [Entry] that gets returned
     * @param type the type of the argument
     * @return the [Entry] for the specified [type], or null if there isn't one
     */
    @Suppress("UNCHECKED_CAST") // this should never fail
    operator fun <T : ArgumentType<*>> get(type: ArgumentType<*>) = BY_CLASS[type::class] as? Entry<T>

    /**
     * Write an argument type to a [ByteBuf].
     *
     * @param T the type of the argument
     * @param argument the argument
     */
    fun <T : ArgumentType<*>> ByteBuf.writeArgumentType(argument: T) {
        val entry = get<T>(argument)
        if (entry == null) {
            LOGGER.error("Could not serialise $argument (Class ${argument::class})! Will not be sent to client!")
            writeKey(NamespacedKey(value = ""))
            return
        }
        writeKey(entry.name)
        entry.serializer.write(argument, this)
    }

    private fun <T : ArgumentType<*>> register(name: String, kClass: KClass<T>, serialiser: ArgumentSerializer<T>) {
        val key = name.toNamespacedKey()
        if (kClass in BY_CLASS) throw IllegalArgumentException("Class ${kClass.simpleName} already has a serialiser!")
        if (key in BY_NAME) throw IllegalArgumentException("'$name' is already a registered serialiser!")

        val entry = Entry(kClass, serialiser, key)
        BY_CLASS[kClass] = entry
        BY_NAME[key] = entry
    }

    /**
     * Represents an entry for an argument type and it's serialiser.
     *
     * This holds the class, serialiser and name for the argument type
     */
    data class Entry<T : ArgumentType<*>>(
        val kClass: KClass<T>,
        val serializer: ArgumentSerializer<T>,
        val name: NamespacedKey
    )
}
