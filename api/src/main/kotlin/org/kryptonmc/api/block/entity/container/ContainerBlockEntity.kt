/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.inventory.Inventory

/**
 * A block entity that has a container.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ContainerBlockEntity : BlockEntity {

    /**
     * The inventory that this block entity has.
     */
    @get:JvmName("inventory")
    public val inventory: Inventory

    /**
     * The token that this container block entity is locked with.
     */
    @get:JvmName("lockToken")
    public var lockToken: String?
}
