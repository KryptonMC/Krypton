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

import com.extollit.gaming.ai.path.model.ColumnarOcclusionFieldList
import com.extollit.gaming.ai.path.model.IBlockDescription
import com.extollit.gaming.ai.path.model.IColumnarSpace
import com.extollit.gaming.ai.path.model.IInstanceSpace
import org.kryptonmc.krypton.world.chunk.KryptonChunk

class KryptonColumnarSpace(private val instanceSpace: KryptonInstanceSpace, private val chunk: KryptonChunk) : IColumnarSpace {

    private val occlusionFieldList = ColumnarOcclusionFieldList(this)

    override fun blockAt(x: Int, y: Int, z: Int): IBlockDescription = KryptonHydrazineBlock.get(chunk.getBlock(x, y, z))

    override fun metaDataAt(x: Int, y: Int, z: Int): Int = 0

    override fun occlusionFields(): ColumnarOcclusionFieldList = occlusionFieldList

    override fun instance(): IInstanceSpace = instanceSpace
}
