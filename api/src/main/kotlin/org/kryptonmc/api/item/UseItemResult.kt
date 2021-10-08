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
 * The result of using an item.
 */
public interface UseItemResult {

    /**
     * The interaction result.
     */
    public val result: InteractionResult

    /**
     * The item that was being used.
     */
    public val item: ItemStack
}
