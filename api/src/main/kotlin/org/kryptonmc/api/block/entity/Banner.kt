/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.item.data.DyeColor

/**
 * A banner.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Banner : NameableBlockEntity {

    /**
     * The base colour of this banner.
     */
    @get:JvmName("baseColor")
    public val baseColor: DyeColor

    /**
     * The patterns that are on this banner.
     */
    @get:JvmName("patterns")
    public val patterns: List<BannerPattern>
}
