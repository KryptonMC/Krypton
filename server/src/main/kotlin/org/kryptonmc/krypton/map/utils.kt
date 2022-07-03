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
package org.kryptonmc.krypton.map

import kotlinx.collections.immutable.persistentSetOf
import org.kryptonmc.api.adventure.toJson
import org.kryptonmc.api.map.decoration.MapDecorationType
import org.kryptonmc.api.map.marker.BannerMarker
import org.kryptonmc.api.map.marker.ItemFrameMarker
import org.kryptonmc.krypton.entity.vector3i
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

fun BannerMarker.save(): CompoundTag = compound {
    vector3i("Pos", position)
    string("Color", color.key().asString())
    if (name != null) string("Name", name!!.toJson())
}

fun ItemFrameMarker.save(): CompoundTag = compound {
    vector3i("Pos", position)
    int("Rotation", rotation)
    int("EntityId", entity.id)
}

private val DECORATIONS_TRACKING_COUNT = persistentSetOf(
    MapDecorationType.PLAYER,
    MapDecorationType.FRAME,
    MapDecorationType.RED_MARKER,
    MapDecorationType.BLUE_MARKER,
    MapDecorationType.PLAYER_OFF_MAP,
    MapDecorationType.PLAYER_OFF_LIMITS,
    MapDecorationType.BANNER_WHITE,
    MapDecorationType.BANNER_ORANGE,
    MapDecorationType.BANNER_MAGENTA,
    MapDecorationType.BANNER_LIGHT_BLUE,
    MapDecorationType.BANNER_YELLOW,
    MapDecorationType.BANNER_LIME,
    MapDecorationType.BANNER_PINK,
    MapDecorationType.BANNER_GRAY,
    MapDecorationType.BANNER_LIGHT_GRAY,
    MapDecorationType.BANNER_CYAN,
    MapDecorationType.BANNER_PURPLE,
    MapDecorationType.BANNER_BLUE,
    MapDecorationType.BANNER_BROWN,
    MapDecorationType.BANNER_GREEN,
    MapDecorationType.BANNER_RED,
    MapDecorationType.BANNER_BLACK
)

fun MapDecorationType.shouldTrackCount(): Boolean = DECORATIONS_TRACKING_COUNT.contains(this)
