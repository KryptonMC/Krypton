/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
