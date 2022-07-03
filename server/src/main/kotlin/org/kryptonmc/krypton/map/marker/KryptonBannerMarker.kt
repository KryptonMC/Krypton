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
package org.kryptonmc.krypton.map.marker

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors
import org.kryptonmc.api.map.decoration.MapDecorationType
import org.kryptonmc.api.map.marker.BannerMarker
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class KryptonBannerMarker(override val position: Vector3i, override val color: DyeColor, override val name: Component?) : BannerMarker {

    val decoration: MapDecorationType
        get() = DECORATION_TYPES_BY_COLOR[color] ?: MapDecorationType.BANNER_BLACK

    fun createId(): String = "banner-${position.x()},${position.y()},${position.z()}"

    object Factory : BannerMarker.Factory {

        override fun of(position: Vector3i, color: DyeColor, name: Component?): BannerMarker = KryptonBannerMarker(position, color, name)
    }

    companion object {

        private val DECORATION_TYPES_BY_COLOR = mapOf(
            DyeColors.WHITE to MapDecorationType.BANNER_WHITE,
            DyeColors.ORANGE to MapDecorationType.BANNER_ORANGE,
            DyeColors.MAGENTA to MapDecorationType.BANNER_MAGENTA,
            DyeColors.LIGHT_BLUE to MapDecorationType.BANNER_LIGHT_BLUE,
            DyeColors.YELLOW to MapDecorationType.BANNER_YELLOW,
            DyeColors.LIME to MapDecorationType.BANNER_LIME,
            DyeColors.PINK to MapDecorationType.BANNER_PINK,
            DyeColors.GRAY to MapDecorationType.BANNER_GRAY,
            DyeColors.LIGHT_GRAY to MapDecorationType.BANNER_LIGHT_GRAY,
            DyeColors.CYAN to MapDecorationType.BANNER_CYAN,
            DyeColors.PURPLE to MapDecorationType.BANNER_PURPLE,
            DyeColors.BLUE to MapDecorationType.BANNER_BLUE,
            DyeColors.BROWN to MapDecorationType.BANNER_BROWN,
            DyeColors.GREEN to MapDecorationType.BANNER_GREEN,
            DyeColors.RED to MapDecorationType.BANNER_RED,
            DyeColors.BLACK to MapDecorationType.BANNER_BLACK,
        )

        @JvmStatic
        fun from(data: CompoundTag): KryptonBannerMarker {
            val position = data.getVector3i("Pos") ?: Vector3i.ZERO
            val color = Registries.DYE_COLORS[Key.key(data.getString("Color"))] ?: DyeColors.WHITE
            val name = if (data.contains("Name", StringTag.ID)) GsonComponentSerializer.gson().deserialize(data.getString("Name")) else null
            return KryptonBannerMarker(position, color, name)
        }
    }
}
