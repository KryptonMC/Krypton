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
package org.kryptonmc.krypton.util

import org.kryptonmc.api.util.Position

object Worlds {

    /**
     * Determines whether the given position is both inside the maximum spawnable
     * height **and** inside the maximum world bounds.
     */
    @JvmStatic
    fun isInSpawnableBounds(pos: Position): Boolean = !isOutsideSpawnableHeight(pos.y) && isInHorizontalWorldBounds(pos)

    @JvmStatic
    private fun isOutsideSpawnableHeight(y: Double): Boolean = y < -20000000 || y >= 20000000

    @JvmStatic
    private fun isInHorizontalWorldBounds(pos: Position): Boolean {
        return pos.x >= -30000000 && pos.z >= -30000000 && pos.x < 30000000 && pos.z < 30000000
    }
}
