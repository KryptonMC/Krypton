/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.banner

import net.kyori.adventure.key.Keyed
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of banner pattern.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BannerPatternTypes::class)
@ImmutableType
public interface BannerPatternType : Keyed {

    /**
     * The shortened code identifying the banner pattern, as specified by
     * https://minecraft.fandom.com/wiki/Banner#Block_data
     */
    @get:JvmName("code")
    public val code: String
}
