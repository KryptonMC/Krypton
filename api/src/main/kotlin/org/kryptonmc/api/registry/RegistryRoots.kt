/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key

/**
 * All the built-in registry roots.
 */
public object RegistryRoots {

    /**
     * The built-in Minecraft root, used for all of the vanilla registries.
     *
     * The key of this root is "minecraft:root".
     */
    @JvmField
    public val MINECRAFT: Key = Key.key("minecraft", "root")

    /**
     * The Krypton root, designed to be used for custom registries.
     *
     * The key of this root is "krypton:root".
     */
    @JvmField
    public val KRYPTON: Key = Key.key("krypton", "root")
}
