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
package org.kryptonmc.krypton.world.chunk.flag

object SetBlockFlag {

    const val UPDATE_NEIGHBOURS: Int = 1
    const val NOTIFY_CLIENTS: Int = 2
    const val UPDATE_NEIGHBOUR_SHAPES: Int = 16
    const val NEIGHBOUR_DROPS: Int = 32
    const val BLOCK_MOVING: Int = 64
    const val LIGHTING: Int = 128

    const val UPDATE_NOTIFY: Int = UPDATE_NEIGHBOURS or NOTIFY_CLIENTS
    const val NO_NEIGHBOUR_DROPS: Int = NEIGHBOUR_DROPS.inv()
    const val ALL: Int = UPDATE_NEIGHBOURS or NOTIFY_CLIENTS or UPDATE_NEIGHBOUR_SHAPES or NEIGHBOUR_DROPS or BLOCK_MOVING or LIGHTING
}
