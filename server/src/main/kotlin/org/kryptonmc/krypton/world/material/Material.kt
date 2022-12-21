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
package org.kryptonmc.krypton.world.material

import org.kryptonmc.api.block.PushReaction

class Material(
    val color: MaterialColor,
    val pushReaction: PushReaction,
    val blocksMotion: Boolean,
    val flammable: Boolean,
    val liquid: Boolean,
    val solidBlocking: Boolean,
    val replaceable: Boolean,
    val solid: Boolean
) {

    override fun toString(): String = "Material(color=$color, pushReaction=$pushReaction, blocksMotion=$blocksMotion, flammable=$flammable, " +
            "liquid=$liquid, solidBlocking=$solidBlocking, replaceable=$replaceable, solid=$solid)"

    class Builder(private val color: MaterialColor) {

        private var pushReaction = PushReaction.NORMAL
        private var blocksMotion = true
        private var flammable = false
        private var liquid = false
        private var solidBlocking = true
        private var replaceable = false
        private var solid = true

        fun liquid(): Builder = apply { liquid = true }

        fun notSolid(): Builder = apply { solid = false }

        fun noCollision(): Builder = apply { blocksMotion = false }

        fun notSolidBlocking(): Builder = apply { solidBlocking = false }

        fun flammable(): Builder = apply { flammable = true }

        fun replaceable(): Builder = apply { replaceable = true }

        fun destroyOnPush(): Builder = apply { pushReaction = PushReaction.DESTROY }

        fun notPushable(): Builder = apply { pushReaction = PushReaction.BLOCK }

        fun build(): Material = Material(color, pushReaction, blocksMotion, flammable, liquid, solidBlocking, replaceable, solid)
    }

    companion object {

        @JvmStatic
        fun builder(color: MaterialColor): Builder = Builder(color)
    }
}
