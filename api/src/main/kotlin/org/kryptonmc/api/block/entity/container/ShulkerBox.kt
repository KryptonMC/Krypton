/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.NameableBlockEntity
import org.kryptonmc.api.item.data.DyeColor

/**
 * A shulker box.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ShulkerBox : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The colour of this shulker box.
     */
    @get:JvmName("color")
    public val color: DyeColor?
}
