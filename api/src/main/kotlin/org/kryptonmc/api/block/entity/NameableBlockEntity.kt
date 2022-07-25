/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component

/**
 * A block entity that can be named.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface NameableBlockEntity : BlockEntity {

    /**
     * The display name of the block entity.
     */
    @get:JvmName("displayName")
    public var displayName: Component
}
