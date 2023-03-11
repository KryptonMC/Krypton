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
package org.kryptonmc.api.entity.animal.type

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.RegistryReference

/**
 * A variant of mooshroom, which represents a particular type of mushroom,
 * either brown mushrooms, or red mushrooms.
 *
 * @property item The mushroom item this mooshroom variant represents.
 * @property block The mushroom block this mooshroom variant represents.
 */
public enum class MooshroomVariant(public val item: RegistryReference<ItemType>, public val block: RegistryReference<Block>) {

    BROWN(ItemTypes.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK),
    RED(ItemTypes.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK)
}
