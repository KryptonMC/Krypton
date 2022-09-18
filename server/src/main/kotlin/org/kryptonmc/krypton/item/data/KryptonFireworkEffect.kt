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
package org.kryptonmc.krypton.item.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.data.FireworkEffectType
import org.kryptonmc.api.util.Color
import org.kryptonmc.nbt.CompoundTag

@JvmRecord
data class KryptonFireworkEffect(
    override val type: FireworkEffectType,
    override val hasFlicker: Boolean,
    override val hasTrail: Boolean,
    override val colors: PersistentList<Color>,
    override val fadeColors: PersistentList<Color>
) : FireworkEffect {

    override fun withType(type: FireworkEffectType): FireworkEffect = copy(type = type)

    override fun withFlicker(flickers: Boolean): FireworkEffect = copy(hasFlicker = flickers)

    override fun withTrail(trail: Boolean): FireworkEffect = copy(hasTrail = trail)

    override fun withColors(colors: Iterable<Color>): FireworkEffect = copy(colors = colors.toPersistentList())

    override fun addColor(color: Color): FireworkEffect = copy(colors = colors.add(color))

    override fun removeColor(index: Int): FireworkEffect = copy(colors = colors.removeAt(index))

    override fun removeColor(color: Color): FireworkEffect = copy(colors = colors.remove(color))

    override fun withFadeColors(fadeColors: Iterable<Color>): FireworkEffect = copy(fadeColors = fadeColors.toPersistentList())

    override fun addFadeColor(color: Color): FireworkEffect = copy(fadeColors = fadeColors.add(color))

    override fun removeFadeColor(index: Int): FireworkEffect = copy(fadeColors = fadeColors.removeAt(index))

    override fun removeFadeColor(color: Color): FireworkEffect = copy(fadeColors = fadeColors.remove(color))

    override fun toBuilder(): FireworkEffect.Builder = Builder(this)

    class Builder(private var type: FireworkEffectType) : FireworkEffect.Builder {

        private var flicker = false
        private var trail = false
        private var colors = persistentListOf<Color>().builder()
        private var fadeColors = persistentListOf<Color>().builder()

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

        override fun build(): FireworkEffect = KryptonFireworkEffect(type, flicker, trail, colors.build(), fadeColors.build())
    }

    object Factory : FireworkEffect.Factory {

        override fun builder(type: FireworkEffectType): FireworkEffect.Builder = Builder(type)
    }

    companion object {

        private val EFFECT_TYPES = FireworkEffectType.values()

        @JvmStatic
        fun from(data: CompoundTag): KryptonFireworkEffect {
            val type = EFFECT_TYPES[data.getByte("Type").toInt()]
            val colors = data.getColors("Colors")
            val fadeColors = data.getColors("FadeColors")
            return KryptonFireworkEffect(type, data.getBoolean("Flicker"), data.getBoolean("Trail"), colors, fadeColors)
        }
    }
}

private fun CompoundTag.getColors(name: String): PersistentList<Color> = getIntArray(name).map(Color::of).toPersistentList()
