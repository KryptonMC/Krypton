/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
