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
package org.kryptonmc.krypton.util

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import it.unimi.dsi.fastutil.Hash
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTIntArray
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTString
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.adventure.toMessage
import java.math.BigInteger
import java.net.InetAddress
import java.security.AccessController
import java.security.MessageDigest
import java.security.PrivilegedAction
import java.util.Locale
import java.util.Optional
import java.util.UUID
import kotlin.math.log2
import kotlin.math.max

/**
 * Convert a [String] to an [InetAddress]
 */
fun String.toInetAddress(): InetAddress = InetAddress.getByName(this)

/**
 * Retrieve a [Logger] instance from the specified class [T]
 *
 * @param T the class to retrieve a logger for
 * @return a [Logger] with the name of the specified class [T]
 */
inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

/**
 * Retrieve a [Logger] instance with the specified [name]
 *
 * @param name the name of the logger
 * @return a [Logger] with the specified [name]
 */
fun logger(name: String): Logger = LogManager.getLogger(name)

/**
 * Calculate the amount of non-empty significant bits in this [Int]
 * For example, if the bits are 00110000, this returns 6.
 */
fun Int.calculateBits() = max(log2(takeHighestOneBit().toDouble()).toInt() + 1, 4)

/**
 * Gets the area from this view distance [Int]
 */
fun Int.toArea() = (this * 2 + 1).square()

/**
 * Square an integer. This avoids us having to use [Math.pow], which is inefficient for this.
 */
fun Int.square() = this * this

/**
 * Convert an ordinary string to a protocol string, with its length prefixed.
 */
fun String.toProtocol(): ByteArray {
    val bytes = encodeToByteArray()
    return byteArrayOf(bytes.size.toByte(), *bytes)
}

/**
 * This isn't related to the above, this turns a UUID into 4 integers sorted by significance descending
 * (most significant first, least significant last)
 */
fun UUID.serialize() = NBTIntArray(intArrayOf(
    (mostSignificantBits shr 32).toInt(),
    (mostSignificantBits and Int.MAX_VALUE.toLong()).toInt(),
    (leastSignificantBits shr 32).toInt(),
    (leastSignificantBits and Int.MAX_VALUE.toLong()).toInt()
))

fun Book.toItemStack(locale: Locale): Pair<ItemStack, NBTCompound> {
    val item = ItemStack(Material.WRITTEN_BOOK, 1)
    val tag = NBTCompound()
        .setString("title", GsonComponentSerializer.gson().serialize(title()))
        .setString("author", GsonComponentSerializer.gson().serialize(author()))
        .set("pages", NBTList<NBTString>(NBTTypes.TAG_String).apply { pages().forEach { add(NBTString(GsonComponentSerializer.gson().serialize(it))) } })
    return item to tag
}

fun calculatePositionChange(new: Double, old: Double) = ((new * 32 - old * 32) * 128).toInt().toShort()

/**
 * If this gamemode has privileges to build. Only players in survival and creative can build (break/place blocks)
 */
val Gamemode.canBuild: Boolean get() = this == Gamemode.SURVIVAL || this == Gamemode.CREATIVE

/**
 * Digest a [MessageDigest] into a hex string. This is Yggdrasil's strange way of digesting to hex
 */
fun MessageDigest.hexDigest(): String = BigInteger(digest()).toString(16)

/**
 * Perform the specified [action] with privileges.
 *
 * Analogous with [AccessController.doPrivileged].
 */
fun <T> doPrivileged(action: () -> T): T = AccessController.doPrivileged(PrivilegedAction(action))

private val ERROR_INVALID = SimpleCommandExceptionType(Component.translatable("argument.id.invalid").toMessage())

fun StringReader.readKey(): Key {
    val cursor = cursor
    while (canRead() && peek().isAllowedInKey) skip()
    return try {
        Key.key(string.substring(cursor, this.cursor))
    } catch (exception: InvalidKeyException) {
        setCursor(cursor)
        throw ERROR_INVALID.createWithContext(this)
    }
}

private val ZERO_TO_NINE_RANGE = '0'..'9'
private val A_TO_Z_RANGE = 'a'..'z'

private val Char.isAllowedInKey: Boolean
    get() = this in ZERO_TO_NINE_RANGE || this in A_TO_Z_RANGE || this == '_' || this == ':' || this == '/' || this == '.' || this == '-'

fun notSupported(message: String): Nothing = throw UnsupportedOperationException(message)

fun String.parseKey(): Key? = try {
    Key.key(this)
} catch (exception: InvalidKeyException) {
    null
}

fun <T> Optional<T>.getIfPresent(): T? = takeIf { it.isPresent }?.get()

fun <T : Map<K, V>, K, V> Optional<T>.forEachPresent(action: (K, V) -> Unit) = ifPresent { it.forEach(action) }

@Suppress("UNCHECKED_CAST")
fun <K> identityStrategy(): Hash.Strategy<K> = IdentityStrategy as Hash.Strategy<K>

private object IdentityStrategy : Hash.Strategy<Any> {

    override fun hashCode(o: Any?) = System.identityHashCode(o)
    override fun equals(a: Any?, b: Any?) = a === b
}
