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
package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.model.IBlockObject
import com.extollit.gaming.ai.path.model.IColumnarSpace
import com.extollit.gaming.ai.path.model.IInstanceSpace
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import java.util.concurrent.ConcurrentHashMap

class KryptonInstanceSpace(private val world: KryptonWorld) : IInstanceSpace {

    private val chunkSpaceMap = ConcurrentHashMap<KryptonChunk, KryptonColumnarSpace>()

    override fun blockObjectAt(x: Int, y: Int, z: Int): IBlockObject = KryptonHydrazineBlock.get(world.getBlock(x, y, z))

    override fun columnarSpaceAt(cx: Int, cz: Int): IColumnarSpace? {
        val chunk = world.getChunk(cx, cz) ?: return null
        return chunkSpaceMap.computeIfAbsent(chunk) { KryptonColumnarSpace(this, it) }
    }
}
