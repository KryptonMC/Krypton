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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.krypton.world.material.Material

@JvmRecord
data class BlockProperties(
    val material: Material,
    val hasCollision: Boolean,
    val soundGroup: BlockSoundGroup,
    val explosionResistance: Float,
    val destroyTime: Float,
    val requiresCorrectTool: Boolean,
    val friction: Float,
    val speedFactor: Float,
    val jumpFactor: Float,
    val drops: Key?,
    val canOcclude: Boolean,
    val isAir: Boolean,
    val hasDynamicShape: Boolean
)
