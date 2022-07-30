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
package org.kryptonmc.krypton.pack.repository

import com.mojang.brigadier.arguments.StringArgumentType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.metadata.PackMetadata
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.util.function.Function
import java.util.function.Supplier

@JvmRecord
data class Pack(
    val id: String,
    val resources: Supplier<PackResources>,
    val title: Component,
    val description: Component,
    val compatibility: PackCompatibility,
    val defaultPosition: Position,
    val required: Boolean,
    val fixedPosition: Boolean,
    val source: PackSource
) {

    constructor(
        id: String,
        title: Component,
        required: Boolean,
        resources: Supplier<PackResources>,
        metadata: PackMetadata,
        defaultPosition: Position,
        source: PackSource
    ) : this(id, resources, title, metadata.description, PackCompatibility.forMetadata(metadata), defaultPosition, required, false, source)

    fun getChatLink(green: Boolean): Component = createChatLink(this, green)

    fun open(): PackResources = resources.get()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Pack).id
    }

    override fun hashCode(): Int = id.hashCode()

    fun interface Constructor {

        fun create(
            id: String,
            title: Component,
            required: Boolean,
            resources: Supplier<PackResources>,
            metadata: PackMetadata,
            defaultPosition: Position,
            source: PackSource
        ): Pack
    }

    enum class Position {

        TOP,
        BOTTOM;

        fun <T> insert(list: MutableList<T>, value: T, opposite: Boolean, converter: Function<T, Pack>): Int {
            val position = if (opposite) opposite() else this
            if (position == BOTTOM) {
                var index = 0
                while (index < list.size) {
                    val pack = converter.apply(list[index])
                    if (!pack.fixedPosition || pack.defaultPosition != this) break
                    ++index
                }
                list.add(index, value)
                return index
            }
            var index = list.size - 1
            while (index >= 0) {
                val pack = converter.apply(list[index])
                if (!pack.fixedPosition || pack.defaultPosition != this) break
                --index
            }
            list.add(index + 1, value)
            return index + 1
        }

        private fun opposite(): Position = if (this == TOP) BOTTOM else TOP
    }

    companion object {

        private val LOGGER = logger<Pack>()

        @JvmStatic
        fun create(
            id: String,
            required: Boolean,
            resourceSupplier: Supplier<PackResources>,
            factory: Constructor,
            defaultPosition: Position,
            source: PackSource
        ): Pack? {
            try {
                resourceSupplier.get().use {
                    val metadata = it.getMetadata(PackMetadata.Serializer)
                    if (metadata != null) {
                        return factory.create(id, Component.text(it.name), required, resourceSupplier, metadata, defaultPosition, source)
                    }
                }
                return null
            } catch (exception: IOException) {
                LOGGER.warn("Failed to get pack information for ID $id!", exception)
                return null
            }
        }

        @JvmStatic
        private fun createChatLink(pack: Pack, green: Boolean): Component = Component.translatable()
            .key("chat.square_brackets")
            .args(pack.source.decorate(Component.text(pack.id)))
            .color(if (green) NamedTextColor.GREEN else NamedTextColor.RED)
            .insertion(StringArgumentType.escapeIfRequired(pack.id))
            .hoverEvent(HoverEvent.showText(Component.text().append(pack.title).append(Component.newline()).append(pack.description)))
            .build()
    }
}
