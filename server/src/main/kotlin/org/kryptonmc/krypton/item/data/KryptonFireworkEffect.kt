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

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.data.FireworkEffectType
import org.kryptonmc.api.util.Color
import org.kryptonmc.nbt.CompoundTag
import java.util.Arrays

@JvmRecord
data class KryptonFireworkEffect(
    override val type: FireworkEffectType,
    override val hasFlicker: Boolean,
    override val hasTrail: Boolean,
    override val colors: ImmutableList<Color>,
    override val fadeColors: ImmutableList<Color>
) : FireworkEffect {

    class Builder(private val type: FireworkEffectType) : FireworkEffect.Builder {

        private var flicker = false
        private var trail = false
        private var colors = ImmutableList.of<Color>()
        private var fadeColors = ImmutableList.of<Color>()

        override fun flickers(): Builder = apply { flicker = true }

        override fun trail(): Builder = apply { trail = true }

        override fun colors(colors: Collection<Color>): Builder = apply { this.colors = ImmutableList.copyOf(colors) }

        override fun fadeColors(colors: Collection<Color>): Builder = apply { fadeColors = ImmutableList.copyOf(colors) }

        override fun build(): KryptonFireworkEffect = KryptonFireworkEffect(type, flicker, trail, colors, fadeColors)
    }

    object Factory : FireworkEffect.Factory {

        override fun builder(type: FireworkEffectType): Builder = Builder(type)
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

private fun CompoundTag.getColors(name: String): ImmutableList<Color> =
    Arrays.stream(getIntArray(name)).mapToObj(Color::of).collect(ImmutableList.toImmutableList())
