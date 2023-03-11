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
package org.kryptonmc.krypton.world.block.data

import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.util.BooleanOperator
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter

enum class SupportType {

    FULL {

        override fun isSupporting(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, face: Direction): Boolean {
            return KryptonBlock.isFaceFull(state.getBlockSupportShape(world, pos), face)
        }
    },
    CENTER {

        private val supportShape = KryptonBlock.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0)

        override fun isSupporting(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, face: Direction): Boolean {
            return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(world, pos).getFaceShape(face), supportShape, BooleanOperator.ONLY_SECOND)
        }
    },
    RIGID {

        private val supportShape = Shapes.join(Shapes.block(), KryptonBlock.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), BooleanOperator.ONLY_FIRST)

        override fun isSupporting(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, face: Direction): Boolean {
            return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(world, pos).getFaceShape(face), supportShape, BooleanOperator.ONLY_FIRST)
        }
    };

    abstract fun isSupporting(state: KryptonBlockState, world: BlockGetter, pos: Vec3i, face: Direction): Boolean
}
