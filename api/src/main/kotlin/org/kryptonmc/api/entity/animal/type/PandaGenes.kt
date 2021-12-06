/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla panda genes.
 */
@Catalogue(PandaGene::class)
public object PandaGenes {

    // @formatter:off
    @JvmField public val NORMAL: PandaGene = register("normal", false)
    @JvmField public val LAZY: PandaGene = register("lazy", false)
    @JvmField public val WORRIED: PandaGene = register("worried", false)
    @JvmField public val PLAYFUL: PandaGene = register("playful", false)
    @JvmField public val BROWN: PandaGene = register("brown", true)
    @JvmField public val WEAK: PandaGene = register("weak", true)
    @JvmField public val AGGRESSIVE: PandaGene = register("aggressive", false)

    // @formatter:on
    @JvmStatic
    private fun register(name: String, isRecessive: Boolean): PandaGene {
        val key = Key.key(name)
        return Registries.PANDA_GENES.register(key, PandaGene.of(key, isRecessive))
    }
}
