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
package org.kryptonmc.krypton.world

import org.kryptonmc.api.world.WorldBorder

@JvmRecord
data class KryptonWorldBorder(
    override val size: Double,
    override val centerX: Double,
    override val centerZ: Double,
    override val damageMultiplier: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Int,
    val warningTime: Int
) : WorldBorder {

    companion object {

        @JvmField
        val DEFAULT: KryptonWorldBorder = KryptonWorldBorder(5.9999968E7, 0.0, 0.0, 0.2, 5.0, 0.0, 0L, 5, 15)
    }
}
