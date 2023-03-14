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
package org.kryptonmc.krypton.world.block.entity

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.block.entity.BlockEntityType

class KryptonBlockEntityType<T : BlockEntity>(private val key: Key, override val applicableBlocks: ImmutableSet<Block>) : BlockEntityType<T> {

    override fun key(): Key = key

    override fun isApplicable(block: Block): Boolean = applicableBlocks.contains(block)

    override fun toString(): String = "BlockEntityType(applicableBlocks=$applicableBlocks)"
}
