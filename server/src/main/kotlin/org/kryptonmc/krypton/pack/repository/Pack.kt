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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.metadata.FeatureFlagsMetadataSection
import org.kryptonmc.krypton.pack.metadata.PackMetadataSection
import org.kryptonmc.krypton.world.flag.FeatureFlagSet
import java.util.function.Function

class Pack(
    private val id: String,
    private val required: Boolean,
    private val resources: ResourcesSupplier,
    private val title: Component,
    info: Info,
    private val compatibility: PackCompatibility,
    private val defaultPosition: Position,
    private val fixedPosition: Boolean,
    private val source: PackSource
) {

    private val description = info.description
    private val requestedFeatures = info.requestedFeatures

    fun id(): String = id

    fun defaultPosition(): Position = defaultPosition

    fun isRequired(): Boolean = required

    fun isFixedPosition(): Boolean = fixedPosition

    fun requestedFeatures(): FeatureFlagSet = requestedFeatures

    fun getChatLink(green: Boolean): Component = Component.translatable().key("chat.square_brackets").args(source.decorate(Component.text(id)))
        .style {
            it.color(if (green) NamedTextColor.GREEN else NamedTextColor.RED)
            it.insertion(StringArgumentType.escapeIfRequired(id))
            it.hoverEvent(HoverEvent.showText(Component.text().append(title).appendNewline().append(description)))
        }
        .build()

    fun open(): PackResources = resources.open(id)

    override fun equals(other: Any?): Boolean = this === other || other is Pack && id == other.id

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Pack(id=$id, title=$title, description=$description, compatibility=$compatibility, " +
            "defaultPosition=$defaultPosition, required=$required, fixedPosition=$fixedPosition)"

    @JvmRecord
    data class Info(val description: Component, val format: Int, val requestedFeatures: FeatureFlagSet) {

        fun compatibility(type: PackType): PackCompatibility = PackCompatibility.forFormat(format, type)
    }

    enum class Position {

        TOP,
        BOTTOM;

        fun <T> insert(list: MutableList<T>, value: T, opposite: Boolean, converter: Function<T, Pack>): Int {
            val position = if (opposite) opposite() else this
            if (position == BOTTOM) {
                var index = 0
                while (index < list.size) {
                    val pack = converter.apply(list.get(index))
                    if (!pack.isFixedPosition() || pack.defaultPosition != this) break
                    ++index
                }
                list.add(index, value)
                return index
            }
            var index = list.size - 1
            while (index >= 0) {
                val pack = converter.apply(list.get(index))
                if (!pack.isFixedPosition() || pack.defaultPosition != this) break
                --index
            }
            list.add(index + 1, value)
            return index + 1
        }

        private fun opposite(): Position = if (this == TOP) BOTTOM else TOP
    }

    fun interface ResourcesSupplier {

        fun open(packId: String): PackResources
    }

    companion object {

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        fun readMetaAndCreate(id: String, title: Component, required: Boolean, resources: ResourcesSupplier, type: PackType,
                              defaultPosition: Position, source: PackSource): Pack? {
            val info = readPackInfo(id, resources)
            return if (info != null) create(id, title, required, resources, info, type, defaultPosition, false, source) else null
        }

        @JvmStatic
        private fun create(id: String, title: Component, required: Boolean, resources: ResourcesSupplier, info: Info, type: PackType,
                           defaultPosition: Position, fixedPosition: Boolean, source: PackSource): Pack {
            return Pack(id, required, resources, title, info, info.compatibility(type), defaultPosition, fixedPosition, source)
        }

        @JvmStatic
        fun readPackInfo(id: String, resourcesSupplier: ResourcesSupplier): Info? {
            try {
                resourcesSupplier.open(id).use { resources ->
                    val metadata = resources.getMetadataSection(PackMetadataSection.Serializer)
                    if (metadata == null) {
                        LOGGER.warn("Missing metadata in pack $id. Ignoring...")
                        return null
                    }
                    val featureFlagSection = resources.getMetadataSection(FeatureFlagsMetadataSection.SERIALIZER)
                    val featureFlags = featureFlagSection?.flags ?: FeatureFlagSet.of()
                    return Info(metadata.description, metadata.format, featureFlags)
                }
            } catch (exception: Exception) {
                LOGGER.warn("Failed to read pack metadata!", exception)
                return null
            }
        }
    }
}
