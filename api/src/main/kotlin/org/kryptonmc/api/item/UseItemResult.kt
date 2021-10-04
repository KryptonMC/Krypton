/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import org.kryptonmc.api.util.InteractionResult

/**
 * Represents the result of using an item.
 *
 * @param result the result
 * @param item the item
 */
@JvmRecord
public data class UseItemResult(
    public val result: InteractionResult,
    public val item: ItemStack
)
