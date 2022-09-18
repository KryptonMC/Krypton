/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.ban

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in ban types.
 */
@Catalogue(BanType::class)
public object BanTypes {

    // @formatter:off
    @JvmField
    public val IP: BanType = get("ip")
    @JvmField
    public val PROFILE: BanType = get("profile")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): BanType = Registries.BAN_TYPES.get(Key.key(name))!!
}
