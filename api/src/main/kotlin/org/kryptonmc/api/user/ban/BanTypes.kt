/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
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
    @JvmField public val IP: BanType = register<Ban.IP>("ip")
    @JvmField public val PROFILE: BanType = register<Ban.Profile>("profile")

    // @formatter:on
    @JvmStatic
    private inline fun <reified T : Ban> register(name: String): BanType {
        val key = Key.key("krypton", name)
        return Registries.BAN_TYPES.register(key, BanType.of(key, T::class.java))
    }
}
