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

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.api.inventory.item.Material
import org.kryptonmc.krypton.api.world.Gamemode
import java.net.InetAddress
import java.util.Locale
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

fun Book.toItemStack(locale: Locale): Pair<ItemStack, CompoundBinaryTag> {
    val item = ItemStack(Material.WRITTEN_BOOK, 1)
    val tag = CompoundBinaryTag.builder()
        .putString("title", GsonComponentSerializer.gson().serialize(title()))
        .putString("author", GsonComponentSerializer.gson().serialize(author()))
        .put("pages", ListBinaryTag.from(pages().map { StringBinaryTag.of(GsonComponentSerializer.gson().serialize(it)) }))
        .build()
    return item to tag
}

fun calculatePositionChange(new: Double, old: Double) = ((new * 32 - old * 32) * 128).toInt().toShort()

/**
 * If this gamemode has privileges to build. Only players in survival and creative can build (break/place blocks)
 */
val Gamemode.canBuild: Boolean get() = this == Gamemode.SURVIVAL || this == Gamemode.CREATIVE
