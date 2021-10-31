/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla objective render types.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(ObjectiveRenderType::class)
public object ObjectiveRenderTypes {

    // @formatter:off
    @JvmField public val INTEGER: ObjectiveRenderType = register("integer")
    @JvmField public val HEARTS: ObjectiveRenderType = register("hearts")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): ObjectiveRenderType {
        val key = Key.key("krypton", name)
        return Registries.OBJECTIVE_RENDER_TYPES.register(key, ObjectiveRenderType.of(key))
    }
}
