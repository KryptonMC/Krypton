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
package org.kryptonmc.krypton.item.data

import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.data.FireworkEffectType
import org.kryptonmc.nbt.CompoundTag
import java.awt.Color

@JvmRecord
data class KryptonFireworkEffect(
    override val type: FireworkEffectType,
    override val hasFlicker: Boolean,
    override val hasTrail: Boolean,
    override val colors: List<Color>,
    override val fadeColors: List<Color>
) : FireworkEffect {

    constructor(tag: CompoundTag) : this(
        FireworkEffectType.fromId(tag.getByte("Type").toInt())!!,
        tag.getBoolean("Flicker"),
        tag.getBoolean("Trail"),
        tag.getIntArray("Colors").map(::Color),
        tag.getIntArray("FadeColors").map(::Color)
    )

    override fun toBuilder(): FireworkEffect.Builder = Builder(this)

    class Builder(private var type: FireworkEffectType) : FireworkEffect.Builder {

        private var flicker = false
        private var trail = false
        private var colors = mutableListOf<Color>()
        private var fadeColors = mutableListOf<Color>()

        constructor(effect: FireworkEffect) : this(effect.type) {
            flicker = effect.hasFlicker
            trail = effect.hasTrail
            colors.addAll(effect.colors)
            fadeColors.addAll(effect.fadeColors)
        }

        override fun type(type: FireworkEffectType): FireworkEffect.Builder = apply { this.type = type }

        override fun flicker(value: Boolean): FireworkEffect.Builder = apply { flicker = value }

        override fun trail(value: Boolean): FireworkEffect.Builder = apply { trail = value }

        override fun colors(colors: Iterable<Color>): FireworkEffect.Builder = apply {
            this.colors.clear()
            this.colors.addAll(colors)
        }

        override fun addColor(color: Color): FireworkEffect.Builder = apply { colors.add(color) }

        override fun fadeColors(colors: Iterable<Color>): FireworkEffect.Builder = apply {
            fadeColors.clear()
            fadeColors.addAll(colors)
        }

        override fun addFadeColor(color: Color): FireworkEffect.Builder = apply { fadeColors.add(color) }

        override fun build(): FireworkEffect = KryptonFireworkEffect(type, flicker, trail, colors, fadeColors)
    }

    object Factory : FireworkEffect.Factory {

        override fun builder(type: FireworkEffectType): FireworkEffect.Builder = Builder(type)
    }
}
